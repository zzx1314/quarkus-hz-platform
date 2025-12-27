<script setup lang="ts">
import { onMounted, ref } from "vue";
import {
  FormInstance,
  genFileId,
  UploadFile,
  UploadFiles,
  UploadInstance,
  UploadProps,
  UploadRawFile
} from "element-plus";
import { useAiDocument } from "./hook";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import EditPen from "~icons/ep/edit-pen";
import Delete from "~icons/ep/delete";
import { PureTableBar } from "@/components/RePureTableBar";
import { useDocForm } from "./form";
import PureTable from "@pureadmin/table";
import { PlusSearch, FieldValues, PlusForm } from "plus-pro-components";
import AddFill from "~icons/ri/add-circle-line";
import Back from "~icons/ep/back";
import IconDocument from "~icons/ep/document";
import EmojioneMonotoneDirectHit from "~icons/emojione-monotone/direct-hit";
import IconSetting from "~icons/ep/setting";
import Chat from "@/views/ai/chat/index.vue";
import { useParagraph } from "./paragraphHook";
import {
  aiKnowledgeBaseDetail,
  aiKnowledgeBaseUpdate
} from "@/api/aiKnowledgeBase";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import Search from "~icons/ep/search";
import { aiDocumentUpdate } from "@/api/aiDocument";

defineOptions({
  name: "AiDocument"
});

const addFormRef = ref<FormInstance>();
const offsetHeight = 172;
const { columnsQueryForm, columnsKbForm } = useDocForm();
const treeRef = ref();
const selectLabel = ref("");
const { toParagraphDetail } = useParagraph();
const uploadRef = ref<UploadInstance>(null);
const uploadFileTemp = ref<UploadFile>(null);
const showCharacterSplitter = ref(false);
const showFlagSplitter = ref(false);

const state = ref<FieldValues>({
  hitHandle: "大模型优化",
  acquaintanceLevel: 80
});

const defaultProps = {
  children: "children",
  label: "label"
};

interface Tree {
  label: string;
  children?: Tree[];
}

const handleNodeClick = (data: Tree) => {
  console.log(data.label);
  selectLabel.value = data.label;
  if (data.label === "文档") {
    onSearch();
  } else if (data.label === "命中测试") {
    localStorage.removeItem("index");
    localStorage.removeItem("list");
  } else if (data.label === "设置") {
    aiKnowledgeBaseDetail(knowledgeId).then(res => {
      if (res.code === SUCCESS) {
        state.value = {
          acquaintanceLevel: res.data.acquaintanceLevel,
          hitHandle: res.data.hitHandle
        };
      }
    });
  }
};

// 提交知识库配置
const handleSubmitKb = (values: FieldValues) => {
  console.log(values, "Submit");
  aiKnowledgeBaseUpdate({
    acquaintanceLevel: values.acquaintanceLevel,
    hitHandle: values.hitHandle,
    id: knowledgeId
  }).then(res => {
    if (res.code === SUCCESS) {
      message("提交成功", { type: "success" });
    }
  });
};

const openParagraphPage = row => {
  console.log("openParagraphPage");
  toParagraphDetail({ docId: row.id, name: row.docName });
};

const handleExceed: UploadProps["onExceed"] = files => {
  uploadRef.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  file.uid = genFileId();
  uploadRef.value!.handleStart(file);
};

const beforeUpload = (uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  console.log("上传文件前的钩子", uploadFile.name);
  uploadFileTemp.value = uploadFile;
};

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log(addForm.value);
      if (addForm.value.id) {
        // 修改
        submitUploadUpdate();
      } else {
        // 新增
        submitUpload();
      }
    } else {
      console.log("error submit!", fields);
    }
  });
};

// 文档上传
const submitUpload = () => {
  if (uploadFileTemp.value !== null) {
    uploadRef.value.submit();
  } else {
    message("请先上传文件", { type: "error" });
  }
};

// 文档修改
const submitUploadUpdate = () => {
  if (uploadFileTemp.value !== null) {
    uploadRef.value.submit();
  } else {
    aiDocumentUpdate(addForm.value).then(res => {
      if (res.code === SUCCESS) {
        message("修改成功！", { type: "success" });
        cancel();
      } else {
        message(res.msg, { type: "error" });
      }
    });
  }
};

const data: Tree[] = [
  {
    label: "文档"
  },
  {
    label: "命中测试"
  },
  {
    label: "设置"
  }
];

const iconMap = {
  文档: IconDocument,
  命中测试: EmojioneMonotoneDirectHit,
  设置: IconSetting
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

function onDialogOpened(formEl: FormInstance | undefined) {
  if (!formEl) return;
  formEl.clearValidate();
}

const {
  queryForm,
  dataList,
  loading,
  dialogFormVisible,
  title,
  pagination,
  addForm,
  rules,
  columns,
  knowledgeName,
  knowledgeId,
  fileList,
  onUpload,
  onSearch,
  handleUpdate,
  handleDelete,
  handleSizeChange,
  handleCurrentChange,
  handleSelectionChange,
  cancel,
  openDia,
  back
} = useAiDocument();

onMounted(() => {
  onSearch();
  if (treeRef.value && data.length > 0) {
    treeRef.value.setCurrentKey(data[0].label);
    selectLabel.value = data[0].label;
  }
});
</script>
<template>
  <div class="main">
    <el-row :gutter="6">
      <el-col :span="3">
        <el-card shadow="never">
          <div :style="{ height: `calc(100vh - ${offsetHeight}px)` }">
            <div class="mb-2">
              <el-button
                type="primary"
                link
                :icon="useRenderIcon(Back)"
                style="font-size: 17px"
                @click="back()"
                >{{ knowledgeName }}</el-button
              >
            </div>
            <el-tree
              ref="treeRef"
              node-key="label"
              highlight-current
              style="max-width: 600px"
              :data="data"
              :props="defaultProps"
              @node-click="handleNodeClick"
            >
              <template #default="{ data }">
                <span
                  :class="[
                    'pl-1',
                    'pr-1',
                    'rounded',
                    'flex',
                    'items-center',
                    'select-none'
                  ]"
                >
                  <component
                    :is="iconMap[data.label]"
                    v-if="iconMap[data.label]"
                  />
                  <div class="ml-1">
                    {{ data.label }}
                  </div>
                </span>
              </template>
            </el-tree>
          </div>
        </el-card>
      </el-col>
      <el-col :span="21">
        <div v-if="selectLabel === '文档'">
          <el-card class="my-card">
            <PlusSearch
              v-model="queryForm"
              :columns="columnsQueryForm"
              :show-number="2"
              label-width="80"
              label-position="right"
              @search="onSearch"
              @reset="cancel"
            />
          </el-card>
          <PureTableBar title="文档列表" :columns="columns" @refresh="onSearch">
            <template #buttons>
              <el-button
                type="primary"
                :icon="useRenderIcon(AddFill)"
                @click="openDia('上传', addFormRef)"
              >
                上传
              </el-button>
            </template>
            <template v-slot="{ size, checkList, dynamicColumns }">
              <pure-table
                border
                adaptive
                align-whole="center"
                showOverflowTooltip
                table-layout="auto"
                :loading="loading"
                :size="size"
                :data="dataList"
                :columns="dynamicColumns"
                :checkList="checkList"
                :pagination="pagination"
                :paginationSmall="size === 'small'"
                :header-cell-style="{
                  background: 'var(--el-table-row-hover-bg-color)',
                  color: 'var(--el-text-color-primary)'
                }"
                @selection-change="handleSelectionChange"
                @page-size-change="handleSizeChange"
                @page-current-change="handleCurrentChange"
              >
                <template #content="{ row }">
                  <el-link type="primary" @click="openParagraphPage(row)">{{
                    row.docName
                  }}</el-link>
                </template>
                <template #operation="{ row }">
                  <el-button
                    class="reset-margin"
                    link
                    type="primary"
                    :size="size"
                    :icon="useRenderIcon(EditPen)"
                    @click="handleUpdate(row, addFormRef)"
                  >
                    修改
                  </el-button>
                  <el-popconfirm
                    title="是否确认删除?"
                    @confirm="handleDelete(row)"
                  >
                    <template #reference>
                      <el-button
                        class="reset-margin"
                        link
                        type="primary"
                        :size="size"
                        :icon="useRenderIcon(Delete)"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                </template>
              </pure-table>
            </template>
          </PureTableBar>
        </div>
        <div v-if="selectLabel === '命中测试'">
          <chat
            chat-clear="test"
            :knowledge-id="Number(knowledgeId)"
            :knowledge-name="String(knowledgeName)"
          />
        </div>
        <div v-if="selectLabel === '设置'">
          <el-card shadow="never">
            <div :style="{ height: `calc(100vh - ${offsetHeight}px)` }">
              <div
                class="text-2xl mb-3"
                style="
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "
              >
                知识库设置
              </div>
              <PlusForm
                v-model="state"
                :columns="columnsKbForm"
                :rules="rules"
                @submit="handleSubmitKb"
              />
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      v-model="dialogFormVisible"
      :title="title"
      @close="cancel"
      @opened="onDialogOpened(addFormRef)"
    >
      <el-form
        ref="addFormRef"
        :model="addForm"
        label-width="auto"
        :rules="rules"
      >
        <el-form-item label="分段规则" prop="strategy">
          <el-select
            v-model="addForm.strategy"
            placeholder="选择规则"
            @change="strategyChange"
          >
            <el-option label="长度分段" value="characterSplitter" />
            <el-option label="标识分段" value="flagSplitter" />
            <el-option label="智能分段" value="regexSplitter" />
            <el-option label="标题分段" value="headingSplitter" />
            <el-option label="全文分段" value="allWorkSplitter" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="showFlagSplitter" label="分段标识" prop="flag">
          <el-select v-model="addForm.flag" placeholder="请选择分段标识">
            <el-option
              v-for="(item, index) in ['换行', '分号']"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="showCharacterSplitter"
          label="分段长度"
          prop="length"
        >
          <el-input-number v-model="addForm.length" :min="500" :max="1000" />
        </el-form-item>
        <el-form-item label="上传文档" prop="docFile">
          <el-upload
            ref="uploadRef"
            v-model:file-list="fileList"
            :http-request="onUpload"
            :auto-upload="false"
            :limit="1"
            :on-exceed="handleExceed"
            :on-change="beforeUpload"
            style="width: 190px"
          >
            <template #trigger>
              <el-button
                :icon="useRenderIcon(Search)"
                type="primary"
                style="margin-right: 10px"
                >选取文件</el-button
              >
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm(addFormRef)"
          >确认</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.my-card ::v-deep(.el-card__body) {
  padding: 10px 10px 0;
}
:deep(.el-dropdown-menu__item i) {
  margin: 0;
}

:deep(.el-link) {
  padding-left: 10px;
}

:deep(.el-tree .el-tree-node__content) {
  height: 35px;
  line-height: 35px;
  font-size: 14px;
}
</style>
