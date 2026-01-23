<template>
  <el-dialog
    v-model="dialogVisible"
    title="语音识别"
    width="520px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div class="content">
      <div class="buttons">
        <el-button
          type="primary"
          :loading="loading"
          :disabled="recording"
          @click="startRecording"
        >
          {{ recording ? "录音中..." : "开始录音" }}
        </el-button>

        <el-button type="danger" :disabled="!recording" @click="stopRecording">
          停止并发送
        </el-button>
      </div>

      <div class="status">
        <el-tag :type="statusTagType">{{ status }}</el-tag>
      </div>

      <el-input
        v-model="result"
        type="textarea"
        :rows="5"
        placeholder="识别结果将在这里显示"
        readonly
        show-word-limit
        maxlength="500"
      />
    </div>

    <template #footer>
      <el-button @click="handleClose">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch, onUnmounted, onMounted } from "vue";
import { ElMessage } from "element-plus";
import type { TagProps } from "element-plus";

interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
}>();

const dialogVisible = computed({
  get: () => props.modelValue,
  set: val => emit("update:modelValue", val)
});

watch(dialogVisible, val => {
  if (!val && recording.value) {
    cleanupResources();
    status.value = "未开始";
    result.value = "";
  }
});

onMounted(() => {
  window.addEventListener("beforeunload", cleanupResources);
});

onUnmounted(() => {
  window.removeEventListener("beforeunload", cleanupResources);
  cleanupResources();
});

watch(dialogVisible, val => {
  if (!val && recording.value) {
    cleanupResources();
    status.value = "未开始";
    result.value = "";
  }
});

const recording = ref(false);
const loading = ref(false);
const status = ref("未开始");
const result = ref("");

type StatusType = "primary" | "success" | "warning" | "danger" | "info";
const statusTagType = computed<TagProps["type"]>(() => {
  const map: Record<string, StatusType> = {
    未开始: "info",
    获取麦克风: "warning",
    录音中: "primary",
    发送音频中: "warning",
    识别完成: "success",
    "识别完成（未检测到语音）": "warning",
    获取麦克风失败: "danger"
  };
  return map[status.value] || "info";
});

const SERVER_IP = "192.168.41.227";
const SERVER_PORT = 4433;
const WS_URL = `wss://${SERVER_IP}:${SERVER_PORT}/api/wssound`;
const TARGET_SAMPLE_RATE = 16000;
const BUFFER_SIZE = 4096;

let audioContext: AudioContext | null = null;
let mediaStream: MediaStream | null = null;
let sourceNode: MediaStreamAudioSourceNode | null = null;
let audioWorkletNode: AudioWorkletNode | null = null;
let ws: WebSocket | null = null;
let audioChunks: Float32Array[] = [];

const audioProcessorCode = `
  class AudioProcessor extends AudioWorkletProcessor {
    constructor() {
      super();
      this.buffer = [];
    }

    process(inputs, outputs, parameters) {
      const input = inputs[0];
      if (input.length > 0) {
        this.port.postMessage(input[0]);
      }
      return true;
    }
  }

  registerProcessor('audio-processor', AudioProcessor);
`;

const cleanupResources = () => {
  recording.value = false;
  loading.value = false;

  if (mediaStream) {
    mediaStream.getTracks().forEach(t => {
      t.stop();
      mediaStream = null;
    });
  }

  if (sourceNode) {
    sourceNode.disconnect();
    sourceNode = null;
  }

  if (audioWorkletNode) {
    audioWorkletNode.disconnect();
    audioWorkletNode.port.close();
    audioWorkletNode = null;
  }

  if (audioContext && audioContext.state !== "closed") {
    audioContext.close();
    audioContext = null;
  }

  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.close();
    ws = null;
  }
};

const startRecording = async () => {
  if (!navigator.mediaDevices?.getUserMedia) {
    ElMessage.error(
      "浏览器不支持麦克风访问，请使用 Chrome/Edge/Firefox 最新版本"
    );
    return;
  }

  result.value = "";
  audioChunks = [];
  status.value = "获取麦克风...";
  loading.value = true;

  try {
    mediaStream = await navigator.mediaDevices.getUserMedia({ audio: true });
  } catch (err) {
    console.error("获取麦克风失败:", err);
    ElMessage.error("获取麦克风失败，请允许浏览器访问麦克风");
    status.value = "获取麦克风失败";
    loading.value = false;
    return;
  }

  loading.value = false;
  ws = new WebSocket(WS_URL);
  ws.binaryType = "arraybuffer";

  try {
    const blob = new Blob([audioProcessorCode], {
      type: "application/javascript"
    });
    const moduleUrl = URL.createObjectURL(blob);
    audioContext = new AudioContext({ sampleRate: TARGET_SAMPLE_RATE });
    await audioContext.audioWorklet.addModule(moduleUrl);
    URL.revokeObjectURL(moduleUrl);
  } catch (err) {
    console.error("AudioWorklet 初始化失败:", err);
    ElMessage.error("音频处理初始化失败");
    cleanupResources();
    return;
  }

  ws.onmessage = e => {
    try {
      const data = JSON.parse(e.data);
      if (data.text?.trim()) {
        result.value = data.text;
        status.value = "识别完成";
      }
    } catch (err) {
      console.error("解析返回数据失败:", err);
    }
  };

  ws.onerror = err => {
    console.error("WebSocket 错误:", err);
    ElMessage.error("WebSocket 连接错误");
  };

  ws.onclose = () => {
    if (recording.value) {
      ElMessage.warning("WebSocket 连接已关闭");
    }
  };

  ws.onopen = () => {
    status.value = "录音中...";
    recording.value = true;

    sourceNode = audioContext!.createMediaStreamSource(mediaStream);
    audioWorkletNode = new AudioWorkletNode(audioContext!, "audio-processor");

    audioWorkletNode.port.onmessage = e => {
      audioChunks.push(new Float32Array(e.data));
    };

    sourceNode.connect(audioWorkletNode);
    audioWorkletNode.connect(audioContext!.destination);
  };
};

const stopRecording = () => {
  if (!recording.value) return;

  recording.value = false;
  status.value = "发送音频中...";

  const totalLength = audioChunks.reduce((s, a) => s + a.length, 0);
  const audioData = new Float32Array(totalLength);
  let offset = 0;

  for (const chunk of audioChunks) {
    audioData.set(chunk, offset);
    offset += chunk.length;
  }

  if (ws?.readyState === WebSocket.OPEN) {
    ws.send(audioData.buffer);
    setTimeout(() => ws?.send("EOF"), 50);
  }

  setTimeout(() => {
    if (status.value === "发送音频中...") {
      status.value = "识别完成（未检测到语音）";
    }
  }, 3000);
};

const handleClose = () => {
  cleanupResources();
  status.value = "未开始";
  result.value = "";
  dialogVisible.value = false;
};

onUnmounted(() => {
  cleanupResources();
});
</script>

<style scoped lang="scss">
.content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.buttons {
  display: flex;
  gap: 12px;
}

.status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--el-text-color-regular);
}
</style>
