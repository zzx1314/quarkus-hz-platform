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
      <el-button @click="handleClose">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from "vue";

const visible = ref(false);
const recording = ref(false);
const status = ref("未开始");
const result = ref("");

const SERVER_IP = "192.168.41.227";
const SERVER_PORT = 8000;
const WS_URL = `ws://${SERVER_IP}:${SERVER_PORT}/ws`;
const TARGET_SAMPLE_RATE = 16000;

let audioContext;
let mediaStream;
let sourceNode;
let processorNode;
let ws;
let audioChunks = [];

// 用户点击按钮触发录音
const startRecording = async () => {
  if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
    alert("浏览器不支持麦克风访问，请使用 Chrome/Edge/Firefox 最新版本");
    return;
  }

  result.value = "";
  audioChunks = [];
  status.value = "获取麦克风...";

  // 用户手势触发 getUserMedia
  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({ audio: true });
  } catch (err) {
    console.error("获取麦克风失败:", err);
    alert("获取麦克风失败，请允许浏览器访问麦克风");
    status.value = "未开始";
    return;
  }

  // 连接 WebSocket
  ws = new WebSocket(WS_URL);
  ws.binaryType = "arraybuffer";

  ws.onmessage = e => {
    try {
      const data = JSON.parse(e.data);
      if (data.text && data.text.trim() !== "") {
        result.value = data.text;
        status.value = "识别完成";
      }
    } catch (err) {
      console.error("解析返回数据失败:", err);
    }
  };

  ws.onopen = () => {
    status.value = "录音中...";
    recording.value = true;

    // 创建 AudioContext
    audioContext = new AudioContext({ sampleRate: TARGET_SAMPLE_RATE });
    sourceNode = audioContext.createMediaStreamSource(mediaStream);

    // ScriptProcessorNode 收集音频
    processorNode = audioContext.createScriptProcessor(4096, 1, 1);
    processorNode.onaudioprocess = e => {
      const inputBuffer = e.inputBuffer.getChannelData(0);
      audioChunks.push(new Float32Array(inputBuffer));
    };

    sourceNode.connect(processorNode);
    processorNode.connect(audioContext.destination);
  };
};

const stopRecording = () => {
  if (!recording.value) return;

  recording.value = false;
  status.value = "发送音频中...";

  // 停止音频流
  sourceNode?.disconnect();
  processorNode?.disconnect();
  mediaStream?.getTracks().forEach(t => t.stop());

  // 合并所有 chunk
  const totalLength = audioChunks.reduce((s, a) => s + a.length, 0);
  const audioData = new Float32Array(totalLength);
  let offset = 0;
  for (const chunk of audioChunks) {
    audioData.set(chunk, offset);
    offset += chunk.length;
  }

  // 发送音频
  ws.send(audioData.buffer);

  // 延迟发送 EOF，确保音频先到后端
  setTimeout(() => {
    ws.send("EOF");
  }, 50);

  // 超时兜底，防止后端没返回 text
  setTimeout(() => {
    if (status.value === "发送音频中...") {
      status.value = "识别完成（未检测到语音）";
    }
  }, 3000);
};

const handleClose = () => {
  recording.value = false;
  status.value = "未开始";
  audioChunks = [];
  if (mediaStream) mediaStream.getTracks().forEach(t => t.stop());
  processorNode?.disconnect();
  sourceNode?.disconnect();
  ws?.close();
  visible.value = false;
};
</script>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.buttons {
  display: flex;
  gap: 12px;
}

.status {
  font-size: 14px;
}
</style>
