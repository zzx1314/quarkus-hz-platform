<script setup lang="ts">
import { ref } from "vue";
import { FormInstance } from "element-plus";
import { useDronesConfig } from "./hook";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import EditPen from "~icons/ep/edit-pen";
import Delete from "~icons/ep/delete";
import { PureTableBar } from "@/components/RePureTableBar";
import { useCollectorBusDevForm } from "./form";
import PureTable from "@pureadmin/table";
import { PlusDialogForm, PlusSearch } from "plus-pro-components";
import AddFill from "~icons/ri/add-circle-line";

defineOptions({
  name: "DronesConfig"
});

const addFormRef = ref<FormInstance>();
const { columnsForm, columnsQueryForm } = useCollectorBusDevForm();

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
  onSearch,
  handleUpdate,
  handleDelete,
  handleSizeChange,
  handleCurrentChange,
  handleSelectionChange,
  handleSubmitError,
  handleSubmit,
  cancel,
  openDia
} = useDronesConfig();
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
    <PureTableBar title="服务列表" :columns="columns" @refresh="onSearch">
      <template #buttons>
        <el-button
          type="primary"
          :icon="useRenderIcon(AddFill)"
          @click="openDia('新增', addFormRef)"
        >
          新增
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
      width="500px"
      @close="cancel"
    >
      <el-form
        ref="addFormRef"
        :model="addForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
      >
        <el-form-item label="服务类型" prop="type">
          <el-select v-model="addForm.type" placeholder="请选择服务类型">
            <el-option label="YOLO" value="YOLO" />
            <el-option label="DEEPSORT" value="DEEPSORT" />
            <el-option label="RTSP" value="RTSP" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="addForm.type === 'YOLO'" label="模型" prop="model">
          <el-input v-model="addForm.yolo.model" placeholder="请输入模型名称" />
        </el-form-item>
        <el-form-item v-if="addForm.type === 'YOLO'" label="输入" prop="input">
          <el-input v-model="addForm.yolo.input" placeholder="请输入" />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'YOLO'"
          label="YOLO开始"
          prop="start"
        >
          <el-input v-model="addForm.yolo.event.onStart" placeholder="请输入" />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'YOLO'"
          label="YOLO结束"
          prop="end"
        >
          <el-input v-model="addForm.yolo.event.onStop" placeholder="请输入" />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'YOLO'"
          label="YOLO检测完成"
          prop="onDetected"
        >
          <el-input
            v-model="addForm.yolo.event.onDetected"
            placeholder="请输入"
          />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'YOLO'"
          label="YOLO检测丢失"
          prop="onDetectFailed"
        >
          <el-input v-model="addForm.yolo.event.onLost" placeholder="请输入" />
        </el-form-item>

        <el-form-item
          v-if="addForm.type === 'DEEPSORT'"
          label="目标跟踪"
          prop="max_age"
        >
          <el-input v-model="addForm.deepsort.max_age" placeholder="请输入" />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'DEEPSORT'"
          label="目标跟踪开始"
          prop="start"
        >
          <el-input
            v-model="addForm.deepsort.event.onStart"
            placeholder="请输入"
          />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'DEEPSORT'"
          label="目标跟踪结束"
          prop="end"
        >
          <el-input
            v-model="addForm.deepsort.event.onStop"
            placeholder="请输入"
          />
        </el-form-item>

        <el-form-item
          v-if="addForm.type === 'DEEPSORT'"
          label="已跟踪"
          prop="onTracked"
        >
          <el-input
            v-model="addForm.deepsort.event.onTracked"
            placeholder="请输入"
          />
        </el-form-item>

        <el-form-item
          v-if="addForm.type === 'DEEPSORT'"
          label="未跟踪"
          prop="onLost"
        >
          <el-input
            v-model="addForm.deepsort.event.onLost"
            placeholder="请输入"
          />
        </el-form-item>

        <el-form-item
          v-if="addForm.type === 'RTSP'"
          label="RTSP_URL"
          prop="rtsp"
        >
          <el-input v-model="addForm.rtsp.url" placeholder="请输入" />
        </el-form-item>

        <el-form-item v-if="addForm.type === 'RTSP'" label="比特率" prop="rtsp">
          <el-input v-model="addForm.rtsp.bitrate" placeholder="请输入" />
        </el-form-item>

        <el-form-item
          v-if="addForm.type === 'RTSP'"
          label="RTSP开始"
          prop="start"
        >
          <el-input v-model="addForm.rtsp.event.onStart" placeholder="请输入" />
        </el-form-item>

        <el-form-item
          v-if="addForm.type === 'RTSP'"
          label="RTSP结束"
          prop="end"
        >
          <el-input v-model="addForm.rtsp.event.onStop" placeholder="请输入" />
        </el-form-item>
        <el-form-item
          v-if="addForm.type === 'RTSP'"
          label="RTSP检测完成"
          prop="onComplete"
        >
          <el-input
            v-model="addForm.rtsp.event.onComplete"
            placeholder="请输入"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="handleSubmit(addFormRef)">
          确定
        </el-button>
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
:deep(.el-form-item) {
  margin-bottom: 12px;
}
</style>
