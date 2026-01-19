import asyncio
import websockets
import sounddevice as sd
import numpy as np
import queue
import sys
import json
from scipy.signal import resample

# -------------------------------
# 配置
# -------------------------------
# 如果服务端在另一台电脑，请输入其局域网IP (如 192.168.31.x)
# 如果是同一台电脑，使用 127.0.0.1
SERVER_IP = "192.168.41.227" 
SERVER_PORT = 8000
URI = f"ws://{SERVER_IP}:{SERVER_PORT}/ws"

# 音频配置
TARGET_SAMPLE_RATE = 16000 # 发送给服务端的采样率 (必须是16000)
BLOCK_SIZE = 4096 
CHANNELS = 1

# 线程安全的队列
audio_queue = queue.Queue()

# -------------------------------
# 1. 音频回调与处理
# -------------------------------
def audio_callback(indata, frames, time, status):
    """麦克风回调：将原始数据放入队列"""
    if status:
        print(status, file=sys.stderr)
    audio_queue.put(indata.copy())

def resample_audio(audio_chunk, original_rate, target_rate):
    """简单的重采样函数"""
    if original_rate != target_rate:
        num_samples = int(len(audio_chunk) * target_rate / original_rate)
        audio_chunk = resample(audio_chunk, num_samples)
    return audio_chunk.astype(np.float32)

# -------------------------------
# 2. 异步任务：发送音频
# -------------------------------
async def send_audio(websocket):
    print(">>> 开始采集音频并发送...")
    
    # 自动查询设备默认采样率 (通常是 44100 或 48000)
    device_info = sd.query_devices(sd.default.device[0], 'input')
    actual_rate = int(device_info['default_samplerate'])
    print(f"麦克风采样率: {actual_rate} Hz -> 目标采样率: {TARGET_SAMPLE_RATE} Hz")

    # 打开麦克风流
    with sd.InputStream(samplerate=actual_rate, channels=CHANNELS, blocksize=BLOCK_SIZE, callback=audio_callback):
        while True:
            # 从队列中取数据，如果有堆积则循环取出
            while not audio_queue.empty():
                data = audio_queue.get()
                
                # 展平单声道
                data = data.flatten()
                
                # 客户端本地重采样 (减少网络带宽占用)
                if actual_rate != TARGET_SAMPLE_RATE:
                    data = resample_audio(data, actual_rate, TARGET_SAMPLE_RATE)
                
                # 发送二进制流 (float32 bytes)
                try:
                    await websocket.send(data.tobytes())
                except websockets.exceptions.ConnectionClosed:
                    print("连接已关闭，停止发送")
                    return
            
            # 稍微休眠，让出控制权给接收协程
            await asyncio.sleep(0.01)

# -------------------------------
# 3. 异步任务：接收结果
# -------------------------------
async def receive_text(websocket):
    print(">>> 监听识别结果...")
    try:
        while True:
            # 等待服务端返回消息
            message = await websocket.recv()
            data = json.loads(message)
            
            if "text" in data:
                text = data["text"]
                # 打印结果，\r 可以让输出比较整洁（可选）
                print(f"识别结果: {text}")
                
    except websockets.exceptions.ConnectionClosed:
        print("与服务器断开连接")
    except Exception as e:
        print(f"接收错误: {e}")

# -------------------------------
# 4. 主程序
# -------------------------------
async def main():
    print(f"正在连接到服务器: {URI} ...")
    try:
        # 建立 WebSocket 连接
        async with websockets.connect(URI) as websocket:
            print("连接成功！请开始说话...")
            
            # 创建并发任务：一个发，一个收
            send_task = asyncio.create_task(send_audio(websocket))
            recv_task = asyncio.create_task(receive_text(websocket))
            
            # 等待任务执行
            await asyncio.gather(send_task, recv_task)
            
    except ConnectionRefusedError:
        print("错误：无法连接到服务器。请检查 server.py 是否运行，IP/端口是否正确。")
    except Exception as e:
        print(f"发生未知错误: {e}")

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\n程序已停止")