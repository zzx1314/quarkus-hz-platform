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
@app.websocket("/api/wssound")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    print(f"客户端已连接: {websocket.client}")

    buffer = bytearray()
    bytes_threshold = int(SERVER_SAMPLE_RATE * 4 * PROCESS_INTERVAL)

    try:
        while True:
            #  必须用 receive()
            msg = await websocket.receive()

            # ===============================
            #  接收二进制音频
            # ===============================
            if msg.get("bytes") is not None:
                data = msg["bytes"]
                buffer.extend(data)

                # 达到处理阈值
                if len(buffer) >= bytes_threshold:
                    audio_chunk = np.frombuffer(buffer, dtype=np.float32)
                    buffer = bytearray()

                    # 静音检测
                    rms = np.sqrt(np.mean(audio_chunk ** 2))
                    if rms < SILENCE_THRESHOLD:
                        print(f"静音跳过 (RMS: {rms:.4f})")
                        continue

                    # 在线程池跑模型
                    loop = asyncio.get_event_loop()
                    text = await loop.run_in_executor(
                        None, run_inference_sync, audio_chunk
                    )

                    if text and text.strip():
                        await websocket.send_text(
                            json.dumps({"text": text})
                        )

            # ===============================
            # 接收文本控制指令
            # ===============================
            elif msg.get("text") is not None:
                text = msg["text"]

                if text == "EOF":
                    print("收到 EOF，结束识别")

                    # EOF 前如果还有残余音频，处理一次
                    if buffer:
                        audio_chunk = np.frombuffer(buffer, dtype=np.float32)
                        buffer = bytearray()

                        rms = np.sqrt(np.mean(audio_chunk ** 2))
                        if rms >= SILENCE_THRESHOLD:
                            loop = asyncio.get_event_loop()
                            final_text = await loop.run_in_executor(
                                None, run_inference_sync, audio_chunk
                            )
                            if final_text:
                                await websocket.send_text(
                                    json.dumps({"text": final_text})
                                )

                    break  # 退出 while，结束会话

    except WebSocketDisconnect:
        print("客户端断开连接")
    except Exception as e:
        print(f"连接异常: {e}")

if __name__ == "__main__":
    # host="0.0.0.0" 允许局域网访问
    uvicorn.run(app, host="0.0.0.0", port=8000)