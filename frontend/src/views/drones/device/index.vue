<script setup lang="ts">
import { ref, computed } from "vue";
import type { FormInstance } from "element-plus";
import { PureTableBar } from "@/components/RePureTableBar";
import PureTable from "@pureadmin/table";
import { PlusDialogForm, PlusSearch } from "plus-pro-components";
import { useDronesDevice } from "./hook";
import { useCollectorBusDevForm } from "./form";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import VoiceWsDialog from "./components/VoiceWsDialog.vue";

import Map from "~icons/ri/road-map-line";
import Delete from "~icons/ep/delete";
import AddFill from "~icons/ri/add-circle-line";
import More from "~icons/ep/more-filled";
import UserVoiceLine from "~icons/ri/user-voice-line";

defineOptions({
  name: "DronesDevice"
});

const addFormRef = ref<FormInstance>();
const show = ref(false);
const mapIcon = computed(() => useRenderIcon(Map));
const deleteIcon = computed(() => useRenderIcon(Delete));
const moreIcon = computed(() => useRenderIcon(More));
const voiceIcon = computed(() => useRenderIcon(UserVoiceLine));

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
  handleCommand,
  cancel
} = useDronesDevice();
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
    <PureTableBar title="设备列表" :columns="columns" @refresh="onSearch">
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
              :icon="mapIcon"
              @click="handleCommand(row)"
            >
              地图探索
            </el-button>
            <el-popconfirm title="是否确认删除?" @confirm="handleDelete(row)">
              <template #reference>
                <el-button
                  class="reset-margin"
                  link
                  type="primary"
                  :size="size"
                  :icon="deleteIcon"
                >
                  删除
                </el-button>
              </template>
            </el-popconfirm>
            <el-dropdown trigger="click" :hide-on-click="false">
              <el-button
                class="ml-2 mt-0.5"
                link
                type="primary"
                :size="size"
                :icon="moreIcon"
              />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-button
                      link
                      type="primary"
                      :size="size"
                      :icon="voiceIcon"
                      @click="show = true"
                    >
                      语音交互
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </pure-table>
      </template>
    </PureTableBar>

    <VoiceWsDialog v-model="show" />

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
