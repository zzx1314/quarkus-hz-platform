<script setup lang="ts">
import { ElMessageBox } from "element-plus";
import { ref, onMounted, nextTick } from "vue";
import ListCard from "./components/ListCard.vue";
import ListDialogForm from "./components/ListDialogForm.vue";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import AddFill from "~icons/ri/add-circle-line";
import { useAiApplication } from "./hook";
import { useDetail } from "./hooks/applicationChatHook";
import { message } from "@/utils/message";
const {
  openDia,
  handleDelete,
  cancel,
  handleSubmit,
  handleEnable,
  onPageSizeChange,
  onCurrentChange,
  getCardListData,
  dataLoading,
  applicationList,
  pagination,
  title,
  dialogFormVisible,
  addForm
} = useAiApplication();

const { toDetail } = useDetail();

defineOptions({
  name: "CardList"
});

onMounted(() => {
  console.log("onMounted");
  getCardListData();
});

const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
      `;

const offsetHeight = 240;

const searchValue = ref("");

const handleDeleteItem = application => {
  ElMessageBox.confirm(
    application
      ? `确认删除后${application.name}的所有应用信息将被清空, 且无法恢复`
      : "",
    "提示",
    {
      type: "warning"
    }
  )
    .then(() => {
      handleDelete(application);
    })
    .catch(() => {});
};
const handleManageProduct = application => {
  console.log(application);
  title.value = "修改应用";
  dialogFormVisible.value = true;
  nextTick(() => {
    addForm.value = {
      ...application,
      status: application?.isSetup ? "1" : "0"
    };
  });
};
const handleClickItem = application => {
  console.log("application", application);
  if (application.isSetup) {
    toDetail({
      chatModeType: "app",
      applicationType: application.type,
      applicationId: application.id,
      applicationName: application.name
    });
  } else {
    message("请先启用应用", { type: "warning" });
  }
};
const handleEnableItem = application => {
  const message = application.isSetup
    ? `是否确认停止${application.name}`
    : `建议启动前${application.name}已进行过命中测试`;
  ElMessageBox.confirm(message, "提示", {
    type: "warning"
  })
    .then(() => {
      handleEnable(application);
    })
    .catch(() => {});
};
const handleHitTest = application => {
  console.log("application", application);
  toDetail({
    applicationId: application.id,
    chatModeType: "test",
    applicationType: "复杂应用",
    applicationName: "命中测试-" + application.name
  });
};
</script>

<template>
  <div>
    <div class="w-full flex justify-between mb-4">
      <el-button :icon="useRenderIcon(AddFill)" @click="openDia('新建应用')">
        新建应用
      </el-button>
      <el-input
        v-model="searchValue"
        style="width: 300px"
        placeholder="请输入应用名称"
        clearable
      >
        <template #suffix>
          <el-icon class="el-input__icon">
            <IconifyIconOffline
              v-show="searchValue.length === 0"
              icon="ri/search-line"
            />
          </el-icon>
        </template>
      </el-input>
    </div>
    <div
      v-loading="dataLoading"
      :element-loading-svg="svg"
      element-loading-svg-view-box="-10, -10, 50, 50"
    >
      <el-empty
        v-show="
          applicationList
            .slice(
              pagination.pageSize * (pagination.current - 1),
              pagination.pageSize * pagination.current
            )
            .filter(v =>
              v.name.toLowerCase().includes(searchValue.toLowerCase())
            ).length === 0
        "
        :description="`${searchValue} 应用不存在`"
      />
      <template v-if="pagination.total > 0">
        <div :style="{ height: `calc(100vh - ${offsetHeight}px)` }">
          <el-row :gutter="16">
            <el-col
              v-for="(application, index) in applicationList
                .slice(
                  pagination.pageSize * (pagination.current - 1),
                  pagination.pageSize * pagination.current
                )
                .filter(v =>
                  v.name.toLowerCase().includes(searchValue.toLowerCase())
                )"
              :key="index"
              :xs="24"
              :sm="12"
              :md="8"
              :lg="6"
              :xl="4"
            >
              <ListCard
                :application="application"
                @click-item="handleClickItem"
                @delete-item="handleDeleteItem"
                @manage-application="handleManageProduct"
                @enable-item="handleEnableItem"
                @hit-test="handleHitTest"
              />
            </el-col>
          </el-row>
        </div>
        <el-pagination
          v-model:currentPage="pagination.current"
          class="float-right"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[12, 24, 36]"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="onPageSizeChange"
          @current-change="onCurrentChange"
        />
      </template>
    </div>
    <ListDialogForm
      v-model:visible="dialogFormVisible"
      :data="addForm"
      :title="title"
      @cancelForm="cancel"
      @submitForm="handleSubmit"
    />
  </div>
</template>
