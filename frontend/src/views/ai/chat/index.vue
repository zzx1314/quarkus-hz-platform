<template>
  <div class="inner-html-container">
    <div class="page">
      <div class="tips">
        <div v-if="chatType === '知识库'" class="title">
          {{ knowledgeName }}
        </div>
        <div v-else class="title">
          {{ applicationName }}
        </div>
      </div>
      <div class="grid-space-between" :class="!isMobile ? 'grid-box' : ''">
        <div v-if="!isMobile" class="left-container">
          <el-button
            class="add-btn"
            :icon="useRenderIcon(IconPlus)"
            @click="handleAddSession"
            >新建对话</el-button
          >
          <div class="session-area">
            <div
              v-for="(item, index) in sessionList"
              :key="index"
              class="session-item"
              :class="activeIndex === index ? 'session-item-active' : ''"
              @click="handleChangeSessionIndex(index)"
            >
              <span
                v-if="editIndex !== index"
                :class="activeIndex === index ? 'active-node' : 'normal-node'"
                >{{ item.title }}</span
              >
              <el-input
                v-else
                :ref="`renameRef_${index}`"
                v-model="item.title"
                autofocus
                size="small"
                style="width: 120px"
                @blur="editIndex = -1"
                @change="editIndex = -1"
              />
              <div class="icon-box">
                <el-icon
                  class="icon"
                  color="#fff"
                  @click.stop="handleClearSession(index)"
                >
                  <icon-brush />
                </el-icon>
                <el-icon
                  class="icon"
                  color="#fff"
                  @click.stop="handleFocusInput(index)"
                >
                  <icon-edit-pen />
                </el-icon>
                <el-icon
                  class="icon"
                  color="#fff"
                  @click.stop="handleDeleteSession(index)"
                >
                  <icon-delete />
                </el-icon>
              </div>
            </div>
          </div>
        </div>
        <div class="container">
          <div class="message-area">
            <MessageComp
              ref="messageRef"
              :message="queryInfos.messages"
              :loading="loading"
            />
          </div>
          <div class="input-parent">
            <div class="upload-container">
              <el-upload
                ref="upload"
                v-model:file-list="fileList"
                :http-request="onUpload"
                class="upload-demo"
                :limit="1"
                :on-exceed="handleExceed"
                :auto-upload="false"
              >
                <template #trigger>
                  <icon-park-folder-upload />
                </template>
                <!-- 自定义文件列表项 -->
                <template #file="{ file }">
                  <div class="custom-file-item">
                    <!-- 自定义缩略图位置 -->
                    <div class="flex items-center gap-x-1">
                      <div class="text-lg">
                        <iconamoon-file-document />
                      </div>
                      {{ file.name }}
                    </div>
                    <el-icon class="delete-icon" @click="handleRemove(file)">
                      <icon-delete />
                    </el-icon>
                  </div>
                </template>
              </el-upload>
            </div>
            <div class="input-area" :class="isMobile ? 'left-space' : ''">
              <el-input
                id="keyInput"
                v-model="queryKeys"
                type="textarea"
                placeholder="请输入内容，按 Enter 发送，Shift+Enter 换行"
                :autosize="{ minRows: 1, maxRows: 3 }"
                show-word-limit
                @keydown.enter="
                  (e: KeyboardEvent) => {
                    if (e.isComposing || loading) return;
                    if (e.shiftKey) return;
                    e.preventDefault();
                    queryKeys = queryKeys.replace(/[\r\n]+$/, '');
                    fetchStreamWithFetch();
                  }
                "
              />
              <el-button
                style="height: 40px"
                type="primary"
                :disabled="!queryKeys"
                :loading="loading"
                @click="fetchStreamWithFetch()"
              >
                <el-icon>
                  <Promotion />
                </el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, nextTick } from "vue";
import MessageComp from "./components/messageComp.vue";
import Promotion from "~icons/ep/promotion";
import IconDelete from "~icons/ep/delete";
import IconEditPen from "~icons/ep/editPen";
import IconBrush from "~icons/ep/brush";
import IconPlus from "~icons/ep/plus";
import IconParkFolderUpload from "~icons/streamline/upload-file";
import {
  ElMessage,
  ElMessageBox,
  genFileId,
  UploadUserFile
} from "element-plus";
import { useRenderIcon } from "@/components/ReIcon/src/hooks.js";
import { getToken } from "@/utils/auth";
import IconamoonFileDocument from "~icons/iconamoon/file-document";
import type { UploadInstance, UploadProps, UploadRawFile } from "element-plus";

const upload = ref<UploadInstance>();
const fileList = ref<UploadUserFile[]>();

const handleExceed: UploadProps["onExceed"] = files => {
  upload.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  file.uid = genFileId();
  upload.value!.handleStart(file);
};

const props = defineProps({
  chatType: {
    type: String,
    default: "知识库"
  },
  knowledgeId: {
    type: Number,
    default: null
  },
  knowledgeName: {
    type: String,
    default: ""
  },
  applicationId: {
    type: Number,
    default: null
  },
  applicationName: {
    type: String,
    default: "AI"
  },
  applicationType: {
    type: String,
    default: ""
  },
  chatClear: {
    type: String,
    default: ""
  }
});

// 响应式数据
const isMobile = ref(false);
const sessionList = ref([]);
const activeIndex = ref(-1);
const editIndex = ref(-1);
const queryKeys = ref("");
const loading = ref(false);
const messageRef = ref(null);
const STORAGE_KEYS = {
  sessionList: "list",
  activeIndex: "index"
};

const queryInfos = ref({
  messages: [],
  model: "deepseek-chat"
});

// 监听数据变化
watch(
  sessionList,
  val => {
    const list = val.map((o, i) => ({
      ...o,
      messages: i === activeIndex.value ? queryInfos.value.messages : o.messages
    }));
    localStorage.setItem(STORAGE_KEYS.sessionList, JSON.stringify(list));
  },
  { deep: true }
);

watch(
  activeIndex,
  val => {
    localStorage.setItem(STORAGE_KEYS.activeIndex, JSON.stringify(val));
  },
  { deep: true }
);

function setMessage() {
  const param = {
    title: "对话1",
    crtTime: new Date(),
    messages: []
  };
  if ("复杂应用" === props.applicationType) {
    param.messages.push({
      role: "assistant",
      content:
        "您好，欢迎进入**" +
        props.applicationName +
        "**，此应用为复杂应用，您可以输入启动，程序会自动执行配置的节点。"
    });
  } else {
    param.messages.push({
      role: "assistant",
      content: "您好，欢迎进入**" + props.applicationName + "**."
    });
  }
  sessionList.value.push(param);
  activeIndex.value = sessionList.value.length - 1;
  queryInfos.value.messages = param.messages;
}

const initSessionList = () => {
  if (!localStorage.getItem(STORAGE_KEYS.sessionList)) {
    setMessage();
  } else {
    sessionList.value = JSON.parse(
      localStorage.getItem(STORAGE_KEYS.sessionList) || "[]"
    );
    activeIndex.value = JSON.parse(
      localStorage.getItem(STORAGE_KEYS.activeIndex) || "-1"
    );
    queryInfos.value.messages = sessionList.value[activeIndex.value].messages;
  }
};

const handleAddSession = () => {
  if (loading.value) {
    ElMessage({ type: "warning", message: "请当前问题查询完成后重试！" });
    return;
  }
  sessionList.value.push({
    title: `对话${sessionList.value.length + 1}`,
    crtTime: new Date(),
    messages: []
  });
  queryInfos.value.messages = [];
  activeIndex.value = sessionList.value.length - 1;
};

const handleDeleteSession = (index = 0) => {
  ElMessageBox.confirm("确认删除当前对话？", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      sessionList.value.splice(index, 1);
      if (index === activeIndex.value) {
        activeIndex.value = sessionList.value[index] ? index : --index;
      } else if (index < activeIndex.value) {
        activeIndex.value = --activeIndex.value;
      }
      queryInfos.value.messages =
        activeIndex.value > -1
          ? sessionList.value[activeIndex.value].messages
          : [];
      handleChangeSessionIndex(activeIndex.value);
    })
    .catch(() => {});
};

const handleClearSession = index => {
  sessionList.value[index].messages = [];
  queryInfos.value.messages = sessionList.value[index].messages;
  activeIndex.value = index;
};

const handleFocusInput = index => {
  editIndex.value = index;
};

const handleChangeSessionIndex = async index => {
  if (loading.value) {
    ElMessage({ type: "warning", message: "请当前问题查询完成后重试！" });
    return;
  }
  activeIndex.value = index;
  queryInfos.value.messages =
    sessionList.value[activeIndex.value]?.messages || [];
  await nextTick();
  messageRef.value.scrollBottom();
};

async function fetchStreamWithFetch() {
  if (fileList.value && fileList.value.length > 0) {
    // 处理文件上传
    console.log("文件上传", fileList.value);
    upload.value.submit();
    return;
  }
  if (!sessionList.value.length) {
    await handleAddSession();
  }
  queryInfos.value.messages.push({
    role: "user",
    content: queryKeys.value,
    name: "小智"
  });
  messageRef.value.scrollBottom();
  let question = queryKeys.value;
  queryKeys.value = null;

  loading.value = true;
  queryInfos.value.messages.push({ role: "assistant", content: "" });
  const token = getToken().accessToken;
  let response = null;
  question = question.replace(/\n+$/, "");
  if (props.chatType === "知识库") {
    response = await fetch(
      "/api/upms/aiDocument/hitTest?message=" +
        question +
        "&knowledgeBaseId=" +
        props.knowledgeId,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );
  } else {
    response = await fetch(
      "/api/upms/aiApplication/chat?message=" +
        question +
        "&appId=" +
        props.applicationId,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );
  }

  if (response.ok && response.body) {
    const reader = response.body.getReader();
    const decoder = new TextDecoder();

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      const chunk = decoder.decode(value, { stream: true });
      console.log("chunk----", chunk);
      queryInfos.value.messages[queryInfos.value.messages.length - 1].content +=
        chunk;
    }
    messageRef.value.scrollBottom();
    if (
      sessionList.value[activeIndex.value] &&
      sessionList.value[activeIndex.value].messages
    ) {
      sessionList.value[activeIndex.value].messages = queryInfos.value.messages;
    }
    loading.value = false;
  } else {
    console.error("Stream failed");
  }
}

async function fetchStreamWithFile(param: any) {
  console.log("文件上传", param.get("file"));
  if (!sessionList.value.length) {
    await handleAddSession();
  }
  queryInfos.value.messages.push({
    role: "user",
    fileName: param.get("file").name,
    content: queryKeys.value,
    messageType: "file",
    name: "小智"
  });
  messageRef.value.scrollBottom();
  let question = queryKeys.value;
  question = question.replace(/\n+$/, "");
  param.append("message", question);
  param.append("appId", props.applicationId);
  queryKeys.value = null;

  loading.value = true;
  queryInfos.value.messages.push({ role: "assistant", content: "" });
  const token = getToken().accessToken;
  let response = null;
  response = await fetch("/api/upms/aiApplication/chatFile", {
    method: "POST",
    body: param,
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
  // 进行解码
  if (response.ok && response.body) {
    upload.value!.clearFiles();
    const reader = response.body.getReader();
    const decoder = new TextDecoder();

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      const chunk = decoder.decode(value, { stream: true });
      console.log("chunk----", chunk);
      queryInfos.value.messages[queryInfos.value.messages.length - 1].content +=
        chunk;
    }
    messageRef.value.scrollBottom();
    if (
      sessionList.value[activeIndex.value] &&
      sessionList.value[activeIndex.value].messages
    ) {
      sessionList.value[activeIndex.value].messages = queryInfos.value.messages;
    }
    loading.value = false;
  } else {
    console.error("Stream failed");
  }
}

const onUpload = async option => {
  const formData = new FormData();
  formData.append("file", option.file);
  fetchStreamWithFile(formData);
};

const handleRemove = file => {
  upload.value!.handleRemove(file);
};

// 生命周期钩子
onMounted(async () => {
  await nextTick(() => {
    initSessionList();
    messageRef.value.scrollBottom();
  });
});
</script>

<style scoped lang="scss">
@use "./styles/common.scss" as *;

.input-parent {
  border: 1px solid #ddd;
  border-radius: 12px; /* 圆角弧度 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* 柔和灰色阴影 */
  padding: 10px;
  background-color: #fff;
  font-family: sans-serif;
  flex-direction: column;
  align-items: flex-end;
}

.upload-container {
  position: relative;
  margin-top: 1px; // 调整顶部间距
}

.upload-demo {
  font-size: 25px;
  margin-left: 10px;

  :deep(.el-upload-list) {
    position: absolute;
    top: -10px;
    left: 40px;
    z-index: 10;
  }

  .custom-file-item {
    display: flex;
    align-items: center;
    padding: 5px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    margin-bottom: 5px;

    .custom-thumbnail {
      width: 30px;
      height: 30px;
      object-fit: cover;
      margin-right: 8px;
      border-radius: 4px;
    }

    .file-name {
      flex: 1;
      font-size: 12px;
      color: #666;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .delete-icon {
      cursor: pointer;
      color: #f56565;
      font-size: 14px;

      &:hover {
        color: #ff0000;
      }
    }
  }
}
</style>
