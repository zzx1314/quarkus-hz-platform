import cv2
import subprocess
import numpy as np
from ultralytics import YOLO

# -------------------------
# 1. YOLO 模型
# -------------------------
model = YOLO("yolov8n.pt")  # 第一次会下载模型

# -------------------------
# 2. 打开摄像头
# -------------------------
cap = cv2.VideoCapture(0)
if not cap.isOpened():
    cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)
if not cap.isOpened():
    print("摄像头打开失败")
    exit()

width  = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
fps = 25

# -------------------------
# 3. RTSP 推流地址
# -------------------------
rtsp_url = "rtsp://192.168.41.227:8554/mystream"

# -------------------------
# 4. FFmpeg 命令
# -------------------------
ffmpeg_cmd = [
    "ffmpeg",
    "-y",
    "-f", "rawvideo",
    "-pixel_format", "bgr24",
    "-video_size", f"{width}x{height}",
    "-framerate", str(fps),
    "-i", "-",
    "-c:v", "libx264",
    "-preset", "ultrafast",
    "-tune", "zerolatency",
    "-pix_fmt", "yuv420p",
    "-f", "rtsp",
    rtsp_url
]

process = subprocess.Popen(ffmpeg_cmd, stdin=subprocess.PIPE)

# -------------------------
# 5. 主循环
# -------------------------
try:
    while True:
        ret, frame = cap.read()
        if not ret:
            print("摄像头读取失败")
            break

        # YOLO 推理（只检测人体）
        results = model(frame, classes=[0], conf=0.5)
        annotated_frame = results[0].plot()

        # -------------------------
        # 确保连续内存（避免 Windows 管道写入报错）
        # -------------------------
        annotated_frame = np.ascontiguousarray(annotated_frame)

        # -------------------------
        # 推送给 FFmpeg
        # -------------------------
        try:
            process.stdin.write(annotated_frame.tobytes())
        except (BrokenPipeError, OSError) as e:
            print("FFmpeg 管道写入失败:", e)
            break

        # -------------------------
        # 本地显示调试
        # -------------------------
        cv2.imshow("YOLO 人体识别", annotated_frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

finally:
    # -------------------------
    # 6. 释放资源
    # -------------------------
    cap.release()
    if process.stdin:
        process.stdin.close()
    process.wait()
    cv2.destroyAllWindows()
