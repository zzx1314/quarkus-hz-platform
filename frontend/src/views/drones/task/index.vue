<script setup lang="ts">
import { ref } from "vue";
import { FormInstance } from "element-plus";
import { useDronesTask } from "./hook";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import EditPen from "~icons/ep/edit-pen";
import Delete from "~icons/ep/delete";
import { PureTableBar } from "@/components/RePureTableBar";
import { useCollectorBusDevForm } from "./form";
import PureTable from "@pureadmin/table";
import { PlusDialogForm, PlusSearch } from "plus-pro-components";
import AddFill from "~icons/ri/add-circle-line";
import More from "~icons/ep/more-filled";
import DropperLine from "~icons/ri/dropper-line";
import DropperStatus from "~icons/ri/text-wrap";
import RiBarChartBoxLine from "~icons/ri/bar-chart-box-line";
import RiRestartFill from "~icons/ri/restart-fill";

defineOptions({
  name: "DronesTask"
});

const addFormRef = ref<FormInstance>();
const { columnsForm, columnsQueryForm } = useCollectorBusDevForm();

const {
  queryForm,
  dataList,
  loading,
  dialogFormVisible,
  dialogResultVisible,
  title,
  pagination,
  addForm,
  rules,
  columns,
  buttonClass,
  onSearch,
  handleUpdate,
  handleDelete,
  handleSizeChange,
  handleCurrentChange,
  handleSelectionChange,
  handleSubmitError,
  handleSubmit,
  cancel,
  openDia,
  flowRoute,
  flowRouteStatus,
  startTask,
  datadashBoard
} = useDronesTask();
</script>
<template>
  <div class="main">
    <el-card class="my-card">
      <PlusSearch
        v-model="queryForm"
        :columns="columnsQueryForm"
        :show-number="3"
        label-width="80"
        label-position="right"
        @search="onSearch"
        @reset="cancel"
      />
    </el-card>
    <PureTableBar title="任务列表" :columns="columns" @refresh="onSearch">
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
            <el-dropdown class="ml-2" trigger="click" :hide-on-click="false">
              <el-button
                class="ml-4 mt-0.5"
                link
                type="primary"
                :size="size"
                :icon="useRenderIcon(More)"
              />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-button
                      :class="buttonClass"
                      link
                      type="primary"
                      :size="size"
                      :icon="useRenderIcon(DropperLine)"
                      @click="flowRoute(row)"
                    >
                      流程设计
                    </el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button
                      :class="['status-btn', buttonClass]"
                      link
                      type="primary"
                      :size="size"
                      :icon="useRenderIcon(RiRestartFill)"
                      @click="startTask(row)"
                    >
                      启动流程
                    </el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button
                      :class="[buttonClass]"
                      link
                      type="primary"
                      :size="size"
                      :icon="useRenderIcon(DropperStatus)"
                      @click="flowRouteStatus(row)"
                    >
                      执行状态
                    </el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button
                      :class="buttonClass"
                      link
                      type="primary"
                      :size="size"
                      :icon="useRenderIcon(RiBarChartBoxLine)"
                      @click="datadashBoard(row)"
                    >
                      数据看板
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </pure-table>
      </template>
    </PureTableBar>

    <PlusDialogForm
      ref="addFormRef"
      v-model:visible="dialogFormVisible"
      v-model="addForm"
      :dialog="{ title: title }"
      :form="{
        columns: columnsForm,
        rules,
        labelWidth: '100px'
      }"
      @cancel="cancel"
      @confirm-error="handleSubmitError"
      @confirm="handleSubmit"
    />

    <el-dialog v-model="dialogResultVisible">
      <div>
        <video
          controls
          autoplay
          :src="'http://192.168.41.227:8080/api/dronesMedia/stream/video.mov'"
          width="640"
          height="360"
        />
      </div>
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

.status-btn :deep(svg) {
  color: #67c23a;
}
</style>
