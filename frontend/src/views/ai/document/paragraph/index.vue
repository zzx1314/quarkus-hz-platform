<script setup lang="ts">
import { message } from "@/utils/message";
import { ElMessageBox } from "element-plus";
import { ref, onMounted, nextTick } from "vue";
import ListDialogForm from "./components/ListDialogForm.vue";
import { aiDocumentParagraphPage } from "@/api/aiDocument";
import { useParagraph } from "../paragraphHook";
import Back from "~icons/ep/back";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { useRouter } from "vue-router";
import { MdPreview } from "md-editor-v3";
import "md-editor-v3/lib/preview.css";

defineOptions({
  name: "CardList"
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

const INITIAL_DATA = {
  content: ""
};

const { initToDetail, getParameter } = useParagraph();
initToDetail();

const pagination = ref({ current: 1, pageSize: 12, total: 0 });
const paragraphList = ref([]);
const dataLoading = ref(true);
const router = useRouter();
const id = "preview-only";

const getCardListData = async () => {
  try {
    const page = {
      size: pagination.value.pageSize,
      current: pagination.value.current
    };
    const query = {
      ...page,
      docId: getParameter.docId
    };
    const { data } = await aiDocumentParagraphPage(query);
    paragraphList.value = data.records;
    console.log(paragraphList.value);
    pagination.value = {
      ...pagination.value,
      total: data.total
    };
  } catch (e) {
    console.log(e);
  } finally {
    setTimeout(() => {
      dataLoading.value = false;
    }, 500);
  }
};

onMounted(() => {
  getCardListData();
});

const formDialogVisible = ref(false);
const formData = ref({ ...INITIAL_DATA });
const searchValue = ref("");

const onPageSizeChange = (size: number) => {
  pagination.value.pageSize = size;
  pagination.value.current = 1;
  getCardListData();
};
const onCurrentChange = (current: number) => {
  pagination.value.current = current;
  getCardListData();
  console.log(pagination.value);
};
const handleDeleteItem = paragraph => {
  ElMessageBox.confirm(
    paragraph
      ? `确认删除后${paragraph.name}的所有段落信息将被清空, 且无法恢复`
      : "",
    "提示",
    {
      type: "warning",
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    }
  )
    .then(() => {
      message("删除成功", { type: "success" });
    })
    .catch(() => {});
};
const handleManageParagraph = paragraph => {
  formDialogVisible.value = true;
  nextTick(() => {
    formData.value = { ...paragraph, status: paragraph?.isSetup ? "1" : "0" };
  });
};
const back = () => {
  console.log("back");
  useMultiTagsStoreHook().handleTags("splice", "/ai/document/paragraph/index");
  router.push({ path: "/ai/knowledge/index" });
};
</script>

<template>
  <div>
    <div class="w-full flex justify-between mb-4">
      <div class="mb-2">
        <el-button
          type="primary"
          link
          :icon="useRenderIcon(Back)"
          @click="back()"
          >返回</el-button
        >
      </div>
      <el-input
        v-model="searchValue"
        style="width: 300px"
        placeholder="请输入段落内容"
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
        v-show="paragraphList.length === 0"
        :description="`${searchValue} 没有段落不存在`"
      />
      <template v-if="pagination.total > 0">
        <el-row :gutter="16">
          <el-col
            v-for="(paragraph, index) in paragraphList"
            :key="index"
            :span="24"
            class="paragraph-item"
          >
            <MdPreview :id="id" :modelValue="paragraph.content" />
          </el-col>
        </el-row>
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
    <ListDialogForm v-model:visible="formDialogVisible" :data="formData" />
  </div>
</template>

<style scoped>
.paragraph-item {
  border: 1px solid #ddd;
  padding: 8px;
  margin-bottom: 8px;
  border-radius: 4px;
}

::v-deep(.md-editor-preview) {
  font-size: 15px;
}
</style>
