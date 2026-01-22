import asyncio
import websockets
import sounddevice as sd
import numpy as np
import sys
import json
from scipy.signal import resample

# -------------------------------
# 配置
# -------------------------------
SERVER_IP = "192.168.41.227"  # 请修改为你的服务器IP
SERVER_PORT = 8000
URI = f"ws://{SERVER_IP}:{SERVER_PORT}/ws"

TARGET_SAMPLE_RATE = 16000 
fs = 44100  # 默认麦克风采样率 (会自动检测，这里给个初始值)
CHANNELS = 1

# -------------------------------
# 辅助函数：录音与重采样
# -------------------------------
def record_audio_until_enter():
    """
    阻塞式录音函数：
    开始录音 -> 等待用户按回车 -> 停止录音 -> 返回处理好的音频二进制数据
    """
    print("-" * 30)
    print("🔴 正在录音... (请说话，说完后按【回车键】发送)")
    print("-" * 30)
    
    # 自动查询设备采样率
    device_info = sd.query_devices(sd.default.device[0], 'input')
    actual_rate = int(device_info['default_samplerate'])
    
    # 开始录音 (使用 sounddevice 的 InputStream 配合列表缓存)
    recorded_frames = []
    
    def callback(indata, frames, time, status):
        if status:
            print(status, file=sys.stderr)
        recorded_frames.append(indata.copy())

    # 开启流
    stream = sd.InputStream(samplerate=actual_rate, channels=CHANNELS, callback=callback)
    stream.start()
    
    # 阻塞等待用户输入回车
    input() 
    
    # 停止流
    stream.stop()
    stream.close()
    print("⏹️ 录音结束，正在处理...")
    
    # 合并数据
    if not recorded_frames:
        return None
        
    audio_data = np.concatenate(recorded_frames, axis=0)
    audio_data = audio_data.flatten()
    
    # 重采样 (如果本地不是16k)
    if actual_rate != TARGET_SAMPLE_RATE:
        num_samples = int(len(audio_data) * TARGET_SAMPLE_RATE / actual_rate)
        audio_data = resample(audio_data, num_samples)
        
    return audio_data.astype(np.float32)

# -------------------------------
# 主逻辑
# -------------------------------
async def main():
    print(f"正在连接到服务器: {URI} ...")
    try:
        async with websockets.connect(URI, ping_interval=None) as websocket:
            print("✅ 连接成功！")
            
            while True:
                # 1. 交互提示
                print("\nReady? 按【回车键】开始录音 (输入 'q' 退出): ", end="")
                user_input = await asyncio.to_thread(input) # 使用 asyncio.to_thread 防止阻塞心跳
                
                if user_input.strip().lower() == 'q':
                    break
                
                # 2. 开始录音 (阻塞直到再次回车)
                # 注意：record_audio_until_enter 内部有 input()，为了不卡死 websocket 保持活跃，
                # 也可以放入 thread，但为了简单，这里直接运行即可，因为录音通常很短
                audio_bytes = await asyncio.to_thread(record_audio_until_enter)
                
                if audio_bytes is None or len(audio_bytes) == 0:
                    print("未检测到音频数据")
                    continue
                
                # 3. 发送音频数据 (二进制)
                print(f"🚀 发送音频数据 ({len(audio_bytes)} 采样点)...")
                await websocket.send(audio_bytes.tobytes())
                
                # 4. 发送结束指令 (告诉服务器该开始识别了)
                await websocket.send("EOF")
                
                # 5. 等待接收结果
                print("⏳ 等待识别结果...")
                response = await websocket.recv()
                data = json.loads(response)
                
                print("\n" + "="*30)
                print(f"📝 识别结果: {data['text']}")
                print("="*30 + "\n")

    except ConnectionRefusedError:
        print("❌ 无法连接到服务器。请检查IP/端口是否正确。")
    except websockets.exceptions.ConnectionClosed:
        print("❌ 服务器断开了连接。")
    except Exception as e:
        print(f"❌ 发生错误: {e}")

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n程序退出")