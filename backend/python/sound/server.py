import uvicorn
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
import numpy as np
from funasr import AutoModel
from funasr.utils.postprocess_utils import rich_transcription_postprocess
import asyncio
import json

# -------------------------------
# 1. 全局加载模型 (只加载一次)
# -------------------------------
MODEL_DIR = "FunAudioLLM/SenseVoiceSmall"
print("正在加载模型，请稍候...")

# 如果有N卡，device设为 "cuda"，否则设为 "cpu"
# disable_update=True 防止每次启动都去 huggingface 查更新
model = AutoModel(
    model=MODEL_DIR,
    trust_remote_code=True,
    vad_kwargs={"max_single_segment_time": 30},
    device="cuda", 
    hub="hf",
    disable_update=True
)
print("模型加载完毕！服务准备就绪。")

app = FastAPI()

# -------------------------------
# 2. 参数配置
# -------------------------------
SERVER_SAMPLE_RATE = 16000  # 模型要求的采样率
SILENCE_THRESHOLD = 0.01    # [关键] 静音阈值，低于此数值认为是噪音，不识别
PROCESS_INTERVAL = 2.0      # [关键] 积攒多少秒音频处理一次 (SenseVoice非流式，建议2-3秒)

# -------------------------------
# 3. 推理逻辑
# -------------------------------
def run_inference_sync(audio_float32):
    """同步推理函数，将被放入线程池执行"""
    try:
        # 再次检查长度，防止过短
        if len(audio_float32) < 1600: # 少于0.1秒
            return ""

        res = model.generate(
            input=audio_float32,
            cache={},
            language="zh",  # 必须指定 zh，否则容易乱码
            use_itn=True,
            batch_size_s=60,
            merge_vad=True,
            merge_length_s=15,
        )
        # 提取文本
        text = rich_transcription_postprocess(res[0]["text"])
        return text
    except Exception as e:
        print(f"推理出错: {e}")
        return ""

# -------------------------------
# 4. WebSocket 接口
# -------------------------------
@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    print(f"客户端已连接: {websocket.client}")
    
    # 字节缓冲区
    buffer = bytearray()
    # 计算触发阈值 (字节数 = 采样率 * 4字节(float32) * 秒数)
    bytes_threshold = int(SERVER_SAMPLE_RATE * 4 * PROCESS_INTERVAL)
    
    try:
        while True:
            # 接收客户端发来的二进制音频数据
            data = await websocket.receive_bytes()
            buffer.extend(data)
            
            # 如果缓冲区满了，就开始处理
            if len(buffer) >= bytes_threshold:
                # 1. 将字节流转为 numpy float32
                audio_chunk = np.frombuffer(buffer, dtype=np.float32)
                
                # 2. 清空缓冲区 (简单切分模式)
                buffer = bytearray()
                
                # 3. [核心] 静音检测 (RMS能量检测)
                # 如果全是底噪，直接跳过，防止模型输出 "그" 或幻觉
                rms = np.sqrt(np.mean(audio_chunk**2))
                if rms < SILENCE_THRESHOLD:
                    print(f"静音跳过 (RMS: {rms:.4f})")
                    continue
                
                # 4. 在线程池中运行推理 (防止阻塞 WebSocket 心跳)
                loop = asyncio.get_event_loop()
                text = await loop.run_in_executor(None, run_inference_sync, audio_chunk)
                
                # 5. 如果有结果，发送回客户端
                if text and text.strip():
                    response = {"text": text}
                    await websocket.send_text(json.dumps(response))

    except WebSocketDisconnect:
        print("客户端断开连接")
    except Exception as e:
        print(f"连接异常: {e}")

if __name__ == "__main__":
    # host="0.0.0.0" 允许局域网访问
    uvicorn.run(app, host="0.0.0.0", port=8000)