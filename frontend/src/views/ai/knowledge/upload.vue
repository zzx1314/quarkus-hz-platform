<script setup lang="ts">
import { nextTick, reactive, ref } from "vue";
import {
  ElLoading,
  UploadFiles,
  UploadInstance,
  UploadUserFile
} from "element-plus";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import {
  aiDocumentPreviewFile,
  aiDocumentStoreParagraph,
  aiDocumentUploadFile
} from "@/api/aiDocument";
import UploadIcon from "@/assets/svg/upload-icon.svg?component";
import { useDetail } from "@/views/ai/knowledge/hook/uploadHook";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import Back from "~icons/ep/back";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { useRouter } from "vue-router";
import Delete from "~icons/ep/delete";
import EditPen from "~icons/ep/edit-pen";

defineOptions({
  name: "AiDocumentUpload"
});

const fileList = ref<UploadUserFile[]>();
const uploadRef = ref<UploadInstance>(null);
const router = useRouter();
const isShowParagraph = ref(false);
const offsetHeight = 410;
const activeName = ref(0);
const data = ref([]);
const dialogVisible = ref(false);
const editingContent = ref("");
const currentParagraph = ref(null);
const showCharacterSplitter = ref(false);
const showFlagSplitter = ref(false);

const formParagraph = ref({
  splitterStrategy: "智能分段",
  splitterFlag: "换行",
  splitterLength: 500
});

const { initToDetail, getParameter } = useDetail();
initToDetail();

const onUpload = async option => {
  const loading = ElLoading.service({
    lock: true,
    text: "上传中",
    background: "rgba(0, 0, 0, 0.7)"
  });
  const formData = new FormData();
  formData.append("file", option.file);
  await aiDocumentUploadFile(formData).then(res => {
    if (res.code == SUCCESS) {
      console.log(res.data);
      data.value.push(res.data);
      message("上传成功！", { type: "success" });
      const file = fileList.value.find(item => item.name === option.file.name);
      file.status = "success";
      const allSuccess = fileList.value.every(
        file => file.status === "success"
      );
      if (allSuccess) {
        isShowParagraph.value = true;
      }
    } else {
      message("上传失败", { type: "error" });
    }
  });
  loading.close();
};

const submitUpload = () => {
  if (!fileList.value || fileList.value.length === 0) {
    message("请先上传文档！", { type: "error" });
    return;
  }
  const allSuccess = fileList.value.every(file => file.status === "success");
  if (allSuccess) {
    isShowParagraph.value = true;
  } else {
    uploadRef.value.submit();
  }
};

const onExceed = () => {
  message("最多能上传50个！", { type: "error" });
};

const fileHandleChange = (file: any, fileList: UploadFiles) => {
  const isLimit = file?.size / 1024 / 1024 < 100;
  if (!isLimit) {
    message("文件大小超过 100MB", { type: "error" });
    fileList.splice(-1, 1); //移除当前超出大小的文件
    return false;
  }
};

const handlePreview = (bool: boolean) => {
  let inputDom: any = null;
  nextTick(() => {
    if (document.querySelector(".el-upload__input") != null) {
      inputDom = document.querySelector(".el-upload__input");
      inputDom.webkitdirectory = bool;
    }
  });
};

const back = () => {
  console.log("back");
  useMultiTagsStoreHook().handleTags("splice", "/ai/knowledge/upload");
  router.push({ path: "/ai/knowledge/index" });
};

const backToUpload = () => {
  isShowParagraph.value = false;
};

const editParagraph = paragraph => {
  console.log("editParagraph");
  editingContent.value = paragraph.content;
  currentParagraph.value = paragraph;
  dialogVisible.value = true;
};

const saveEdit = () => {
  if (currentParagraph.value && editingContent.value.trim()) {
    // 找到当前段落所在的文档 index
    const docIndex = data.value.findIndex(doc =>
      doc.content.includes(currentParagraph.value)
    );
    const paraIndex = data.value[docIndex].content.findIndex(
      p => p === currentParagraph.value
    );
    // 替换整个段落对象（保证响应性）
    data.value[docIndex].content.splice(paraIndex, 1, {
      ...currentParagraph.value,
      content: editingContent.value
    });
    message("段落内容已更新", { type: "success" });
  }
  dialogVisible.value = false;
};

const deleteParagraph = paragraph => {
  // 找到该段落所在的文档索引
  const docIndex = data.value.findIndex(doc => doc.content.includes(paragraph));
  if (docIndex === -1) return;

  // 找到该段落在文档中的位置
  const paraIndex = data.value[docIndex].content.indexOf(paragraph);
  if (paraIndex === -1) return;

  // 使用 splice 删除，保证响应式更新
  data.value[docIndex].content.splice(paraIndex, 1);

  message("段落已删除", { type: "success" });
};

const previewParagraph = () => {
  const param = {
    fileName: data.value[activeName.value].name,
    strategy: formParagraph.value.splitterStrategy,
    flag: formParagraph.value.splitterFlag,
    length: formParagraph.value.splitterLength,
    path: data.value[activeName.value].tempFilePath
  };
  console.log(param);
  aiDocumentPreviewFile(param).then(res => {
    if (res.code === SUCCESS) {
      console.log(res.data);
      // 更新data文档的数据
      data.value[activeName.value] = res.data;
      console.log(data.value);
      message("请求成功！", { type: "success" });
    } else {
      message("请求失败", { type: "error" });
    }
  });
};

const submitParagraph = () => {
  console.log("submitParagraph", getParameter);
  const params = data.value.map(doc => ({
    knowledgeBaseId: Number(getParameter.id),
    strategy: formParagraph.value.splitterStrategy,
    flag: formParagraph.value.splitterFlag,
    length: formParagraph.value.splitterLength,
    fileName: doc.name,
    paragraphs: doc.content,
    tempFilePath: doc.tempFilePath
  }));
  console.log(params);
  aiDocumentStoreParagraph(params).then(res => {
    if (res.code === SUCCESS) {
      message("存储成功！", { type: "success" });
      useMultiTagsStoreHook().handleTags("splice", "/ai/knowledge/upload");
      router.push({ path: "/ai/document/index", query: getParameter });
    } else {
      message("存储失败", { type: "error" });
    }
  });
};

function strategyChange(value) {
  console.log(value);
  if (value === "flagSplitter") {
    showFlagSplitter.value = true;
    showCharacterSplitter.value = false;
  } else if (value === "characterSplitter") {
    showCharacterSplitter.value = true;
    showFlagSplitter.value = false;
  } else {
    showFlagSplitter.value = false;
    showCharacterSplitter.value = false;
  }
}
</script>

<template>
  <div class="main">
    <div class="mb-2">
      <el-button type="primary" link :icon="useRenderIcon(Back)" @click="back()"
        >返回</el-button
      >
    </div>
    <el-card v-if="!isShowParagraph">
      <h4 class="title-decoration-1">文档上传</h4>
      <el-upload
        ref="uploadRef"
        v-model:file-list="fileList"
        :http-request="onUpload"
        :webkitdirectory="false"
        class="upload-demo"
        drag
        multiple
        action="#"
        :auto-upload="false"
        :show-file-list="true"
        accept=".txt, .md, .log, .docx, .pdf, .html,.zip,.xlsx,.xls,.csv"
        :limit="50"
        :on-exceed="onExceed"
        :on-change="fileHandleChange"
        @click.prevent="handlePreview(false)"
      >
        <div class="flex justify-center align--center mb-4">
          <upload-icon />
        </div>
        <div class="el-upload__text">
          <p>
            拖拽文件至此上传或
            <em class="hover" @click.prevent="handlePreview(false)">
              选择文件
            </em>
            <em class="hover ml-1" @click.prevent="handlePreview(true)">
              选择文件夹
            </em>
          </p>
          <div class="upload__decoration">
            <p>支持格式TXT、Markdown、PDF、DOCX、HTML、XLS、XLSX、CSV、ZIP</p>
          </div>
        </div>
      </el-upload>
      <div class="flex justify-end mt-4">
        <el-button type="primary" @click="submitUpload">下一步</el-button>
      </div>
    </el-card>
    <el-card v-if="isShowParagraph" shadow="never">
      <el-row :gutter="10">
        <el-col :span="10">
          <h4 class="title-decoration-1 mb-8">设置段落</h4>
          <el-form
            :model="formParagraph"
            label-width="auto"
            style="max-width: 600px"
          >
            <el-form-item label="分段策略">
              <el-select
                v-model="formParagraph.splitterStrategy"
                @change="strategyChange"
                placeholder="请选择分段策略"
              >
                <el-option label="长度分段" value="characterSplitter" />
                <el-option label="标识分段" value="flagSplitter" />
                <el-option label="智能分段" value="regexSplitter" />
                <el-option label="标题分段" value="headingSplitter" />
                <el-option label="全文分段" value="allWorkSplitter" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="showFlagSplitter" label="分段标识">
              <el-select
                v-model="formParagraph.splitterFlag"
                placeholder="请选择分段标识"
              >
                <el-option
                  v-for="(item, index) in ['换行', '分号', '句号', '冒号']"
                  :key="index"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item v-if="showCharacterSplitter" label="分段长度">
              <el-input-number
                v-model="formParagraph.splitterLength"
                :min="500"
                :max="1000"
              />
            </el-form-item>
            <el-form-item>
              <div class="flex justify-end w-full mr-1">
                <el-button type="primary" @click="previewParagraph"
                  >预览</el-button
                >
              </div>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="14">
          <h4 class="title-decoration-1 mb-8">分段预览</h4>
          <el-tabs v-model="activeName" class="paragraph-tabs">
            <template v-for="(item, index) in data" :key="index">
              <el-tab-pane :label="item.name" :name="index">
                <template #label>
                  <div class="flex-center">
                    <span>{{ item?.name }}</span>
                  </div>
                </template>
                <div class="mb-1">
                  <el-text type="info" size="small"
                    >{{ item.content.length }}个段落</el-text
                  >
                </div>
                <div v-if="activeName === index" class="paragraph-list">
                  <el-scrollbar :height="`calc(100vh - ${offsetHeight}px)`">
                    <el-card
                      v-for="(paragraph, idx) in item.content"
                      :key="idx"
                      shadow="never"
                      class="card-never mb-1"
                    >
                      <div class="flex items-center justify-between">
                        <span class="font-bold text-base"
                          >{{ paragraph.title }}...</span
                        >
                        <div class="flex space-x-2">
                          <el-button
                            link
                            :icon="useRenderIcon(EditPen)"
                            @click="editParagraph(paragraph)"
                          />
                          <el-button
                            link
                            :icon="useRenderIcon(Delete)"
                            @click="deleteParagraph(paragraph)"
                          />
                        </div>
                      </div>
                      <div class="mt-1 text-sm">{{ paragraph.content }}</div>
                      <div class="mt-1">
                        <el-text type="info" size="small"
                          >{{ paragraph.length }}个字符</el-text
                        >
                      </div>
                    </el-card>
                  </el-scrollbar>
                </div>
              </el-tab-pane>
            </template>
          </el-tabs>
        </el-col>
      </el-row>
      <el-row class="flex justify-end mt-4">
        <el-button type="primary" @click="backToUpload()">上一步</el-button>
        <el-button type="primary" @click="submitParagraph">导入</el-button>
      </el-row>
    </el-card>

    <el-dialog v-model="dialogVisible" title="编辑段落">
      <el-input v-model="editingContent" type="textarea" :rows="4" />
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.title-decoration-1 {
  position: relative;
  padding-left: 12px;
  margin-bottom: 20px;
  &:before {
    position: absolute;
    left: 2px;
    top: 50%;
    transform: translate(-50%, -50%);
    width: 2px;
    height: 80%;
    content: "";
    background: var(--el-color-primary);
  }
}
.upload__decoration {
  font-size: 12px;
  line-height: 20px;
  color: var(--el-text-color-secondary);
}
.el-upload__text {
  .hover:hover {
    color: var(--el-color-primary-light-5);
  }
}
.card-never {
  background: #f5f6f7;
  border: none;
}
.paragraph-tabs {
  :deep(.el-tabs__item) {
    background: rgba(31, 35, 41, 0.1);
    margin: 1px;
    border-radius: 4px;
    padding: 5px 10px 5px 8px !important;
    height: auto;
    &:nth-child(2) {
      margin-left: 0;
    }
    &:last-child {
      margin-right: 0;
    }
    &.is-active {
      border: 1px solid var(--el-color-primary);
      background: var(--el-color-primary-light-9);
      color: var(--el-text-color-primary);
    }
  }
  :deep(.el-tabs__nav-wrap::after) {
    display: none;
  }
  :deep(.el-tabs__active-bar) {
    display: none;
  }
}
</style>
