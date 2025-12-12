<script setup lang="ts">
import { ref } from "vue";
import {
  FormInstance,
  genFileId,
  UploadFile,
  UploadFiles,
  UploadInstance,
  UploadProps,
  UploadRawFile
} from "element-plus";
import { useAiMcp } from "./hook";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import EditPen from "~icons/ep/edit-pen";
import Delete from "~icons/ep/delete";
import Search from "~icons/ep/search";
import { PureTableBar } from "@/components/RePureTableBar";
import { useCollectorBusDevForm } from "./form";
import PureTable from "@pureadmin/table";
import { PlusSearch } from "plus-pro-components";
import AddFill from "~icons/ri/add-circle-line";
import { message } from "@/utils/message";
import { aiMcpUpdate } from "@/api/aiMcp";
import { SUCCESS } from "@/api/base";

defineOptions({
  name: "AiMcp"
});

const addFormRef = ref<FormInstance>();
const { columnsQueryForm } = useCollectorBusDevForm();
const uploadRef = ref<UploadInstance>(null);
const uploadFileTemp = ref<UploadFile>(null);

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

// mcp添加
const submitUpload = () => {
  if (uploadFileTemp.value !== null) {
    uploadRef.value.submit();
  } else {
    message("请先上传文件", { type: "error" });
  }
};

// mcp修改
const submitUploadUpdate = () => {
  if (uploadFileTemp.value !== null) {
    uploadRef.value.submit();
  } else {
    aiMcpUpdate(addForm.value).then(res => {
      if (res.code === SUCCESS) {
        message("修改成功！", { type: "success" });
        cancel();
      } else {
        message(res.msg, { type: "error" });
      }
    });
  }
};

const handleExceed: UploadProps["onExceed"] = files => {
  uploadRef.value!.clearFiles();
  const file = files[0] as UploadRawFile;
  file.uid = genFileId();
  uploadRef.value!.handleStart(file);
};

const beforeUpload = (uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  console.log("上传文件前。。", uploadFile.name);
  if (uploadFile.name.includes(".jar")) {
    uploadFileTemp.value = uploadFile;
  } else {
    uploadRef.value!.clearFiles();
    message("请选择正确的安装包格式：.jar", {
      type: "error"
    });
  }
};

function onDialogOpened(formEl: FormInstance | undefined) {
  if (!formEl) return;
  formEl.clearValidate();
}

const {
  queryForm,
  dataList,
  dataListTools,
  loading,
  dialogFormVisible,
  dialogTableVisible,
  title,
  pagination,
  paginationTools,
  addForm,
  rules,
  columns,
  columnsTool,
  fileList,
  openToolPage,
  onSearch,
  onSearchTools,
  handleUpdate,
  handleDelete,
  handleSizeChange,
  handleSizeChangeTools,
  handleCurrentChange,
  handleCurrentChangeTools,
  handleSelectionChange,
  handleSelectionChangeTools,
  onUpload,
  cancel,
  openDia
} = useAiMcp();
</script>
<template>
  <div class="main">
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
    <PureTableBar title="MCP列表" :columns="columns" @refresh="onSearch">
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
            <el-link type="primary" @click="openToolPage(row)">{{
              row.toolNum
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
            <el-popconfirm title="是否确认删除?" @confirm="handleDelete(row)">
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
    <el-dialog
      v-model="dialogFormVisible"
      :title="title"
      width="800px"
      @close="cancel"
    >
      <el-form
        ref="addFormRef"
        :model="addForm"
        :inline="true"
        :rules="rules"
        label-width="150px"
      >
        <el-form-item label="MCP名称" prop="name" style="width: 100%">
          <el-input v-model="addForm.name" placeholder="请输入MCP名称" />
        </el-form-item>
        <el-form-item label="MCP描述" prop="name" style="width: 100%">
          <el-input v-model="addForm.description" placeholder="请输入MCP描述" />
        </el-form-item>
        <el-form-item label="MCP类型" prop="commandType" style="width: 100%">
          <el-select v-model="addForm.commandType" placeholder="请选择MCP类型">
            <el-option label="java" value="java" />
            <el-option label="python" value="python" />
            <el-option label="nodeJs" value="nodeJs" />
          </el-select>
        </el-form-item>
        <el-form-item label="上传文件" prop="file">
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
        <span class="dialog-footer">
          <el-button @click="cancel()">取消</el-button>
          <el-button type="primary" @click="submitForm(addFormRef)"
            >确认</el-button
          >
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="dialogTableVisible"
      title="MCP工具列表"
      width="80%"
      @close="cancel"
      @opened="onDialogOpened(addFormRef)"
    >
      <PureTableBar
        title="MCP工具"
        :columns="columnsTool"
        @refresh="onSearchTools"
      >
        <template v-slot="{ size, checkList, dynamicColumns }">
          <pure-table
            border
            align-whole="center"
            showOverflowTooltip
            table-layout="auto"
            :loading="loading"
            :size="size"
            :data="dataListTools"
            :columns="dynamicColumns"
            :checkList="checkList"
            :pagination="paginationTools"
            :paginationSmall="size === 'small'"
            :header-cell-style="{
              background: 'var(--el-table-row-hover-bg-color)',
              color: 'var(--el-text-color-primary)'
            }"
            @selection-change="handleSelectionChangeTools"
            @page-size-change="handleSizeChangeTools"
            @page-current-change="handleCurrentChangeTools"
          />
        </template>
      </PureTableBar>
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
</style>
