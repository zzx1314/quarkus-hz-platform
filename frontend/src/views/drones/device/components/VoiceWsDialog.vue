<template>
  <el-dialog
    v-model="visible"
    title="🎙️ 语音识别"
    width="520px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="content">
      <div class="buttons">
        <el-button type="primary" :disabled="recording" @click="startRecording">
          开始录音
        </el-button>

        <el-button type="danger" :disabled="!recording" @click="stopRecording">
          停止并发送
        </el-button>
      </div>

      <div class="status">
        状态：<strong>{{ status }}</strong>
      </div>

      <el-input
        v-model="result"
        type="textarea"
        :rows="5"
        placeholder="识别结果将在这里显示"
        readonly
      />
    </div>

    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onUnmounted } from "vue";

const recording = ref(false);
const status = ref("未开始");
const result = ref("");
const visible = defineModel<boolean>("visible", { required: true });

const SERVER_IP = "192.168.41.227";
const SERVER_PORT = 8000;
const WS_URL = `ws://${SERVER_IP}:${SERVER_PORT}/ws`;
const TARGET_SAMPLE_RATE = 16000;

let audioContext: AudioContext | null = null;
let mediaStream: MediaStream | null = null;
let sourceNode: MediaStreamAudioSourceNode | null = null;
let workletNode: AudioWorkletNode | null = null;
let ws: WebSocket | null = null;
let audioChunks: Float32Array[] = [];

const cleanup = () => {
  sourceNode?.disconnect();
  workletNode?.disconnect();
  mediaStream?.getTracks().forEach(t => t.stop());
  audioContext?.close();
  if (ws?.readyState === WebSocket.OPEN) {
    ws.close();
  }
  ws = null;
  audioContext = null;
  mediaStream = null;
  sourceNode = null;
  workletNode = null;
  audioChunks = [];
  recording.value = false;
};

const handleClose = () => {
  cleanup();
  status.value = "未开始";
  result.value = "";
  visible.value = false;
};

const handleError = (msg: string) => {
  status.value = msg;
  recording.value = false;
};

const startRecording = async () => {
  result.value = "";
  audioChunks = [];
  status.value = "连接服务器...";

  try {
    ws = new WebSocket(WS_URL);
    ws.binaryType = "arraybuffer";

    ws.onerror = () => handleError("连接失败");
    ws.onclose = () => {
      if (status.value !== "识别完成") {
        handleError("连接已关闭");
      }
    };

    await new Promise<void>((resolve, reject) => {
      ws!.onopen = () => resolve();
      ws!.onerror = reject;
    });

    status.value = "录音中...";
    recording.value = true;

    audioContext = new AudioContext({ sampleRate: TARGET_SAMPLE_RATE });
    await audioContext.audioWorklet.addModule("/audio-processor.js");

    mediaStream = await navigator.mediaDevices.getUserMedia({ audio: true });
    sourceNode = audioContext.createMediaStreamSource(mediaStream);

    workletNode = new AudioWorkletNode(audioContext, "audio-processor");
    workletNode.port.onmessage = e => {
      audioChunks.push(e.data);
    };

    sourceNode.connect(workletNode);
    workletNode.connect(audioContext.destination);
  } catch {
    handleError("初始化失败");
  }
};

const stopRecording = () => {
  if (!recording.value) return;
  recording.value = false;
  status.value = "发送音频中...";

  sourceNode?.disconnect();
  workletNode?.disconnect();
  mediaStream?.getTracks().forEach(t => t.stop());
  sourceNode = null;
  workletNode = null;
  mediaStream = null;

  if (!ws || ws.readyState !== WebSocket.OPEN) {
    handleError("连接已断开");
    return;
  }

  const totalLength = audioChunks.reduce((s, a) => s + a.length, 0);
  const audioData = new Float32Array(totalLength);

  let offset = 0;
  for (const chunk of audioChunks) {
    audioData.set(chunk, offset);
    offset += chunk.length;
  }

  ws.send(audioData.buffer);
  ws.send("EOF");

  ws.onmessage = e => {
    try {
      const data = JSON.parse(e.data);
      result.value = data.text || data.result || "";
      status.value = "识别完成";
    } catch {
      result.value = "解析失败";
      status.value = "识别失败";
    }
  };
};

onUnmounted(cleanup);
</script>
