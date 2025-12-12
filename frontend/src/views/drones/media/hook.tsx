import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import { ElLoading, type FormRules, type UploadUserFile } from "element-plus";
import {
  dronesMediaPage,
  dronesMediaDelete,
  dronesMediaUploadFile,
  dronesMediaDownloadByPath
} from "@/api/dronesMedia";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";

export function useDronesMedia() {
  // ----变量定义-----
  const queryForm = ref({
    name: "",
    beginTime: "",
    endTime: ""
  });
  const moreCondition = ref(false);
  const dataList = ref([]);
  const loading = ref(true);
  const dialogFormVisible = ref(false);
  const title = ref("");
  const fileList = ref<UploadUserFile[]>([]);

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    remarks: ""
  });
  const rules = reactive<FormRules>({
    remarks: [{ required: true, message: "文件描述必填", trigger: "blur" }]
  });
  const columns: TableColumnList = [
    {
      type: "selection",
      width: 55,
      align: "left",
      fixed: "left",
      label: "勾选列"
    },
    {
      label: "序号",
      type: "index",
      fixed: "left",
      width: 70
    },
    {
      label: "文件名称",
      prop: "mediaName",
      width: 100
    },
    {
      label: "文件类型",
      prop: "mediaType",
      width: 100
    },
    {
      label: "文件大小（字节）",
      prop: "mediaSize",
      width: 180
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 170
    },
    {
      label: "文件描述",
      prop: "remarks",
      minWidth: 170
    },
    {
      label: "操作",
      fixed: "right",
      width: 250,
      slot: "operation"
    }
  ];
  const buttonClass = computed(() => {
    return [
      "!h-[20px]",
      "reset-margin",
      "!text-gray-500",
      "dark:!text-white",
      "dark:hover:!text-primary"
    ];
  });

  // -----方法定义---
  function handleUpdate(row, formEl) {
    console.log(row);
    const data = JSON.stringify(row);
    addForm.value = JSON.parse(data);
    openDia("修改配置", formEl);
  }
  // 删除
  function handleDelete(row) {
    console.log(row);
    dronesMediaDelete(row.id).then(res => {
      if (res.code === SUCCESS) {
        message("删除成功！", { type: "success" });
        onSearch();
      } else {
        message(res.msg, { type: "error" });
      }
    });
  }

  function handleSizeChange(val: number) {
    pagination.pageSize = val;
    onSearch();
  }

  function handleCurrentChange(val: number) {
    pagination.currentPage = val;
    onSearch();
  }

  function handleSelectionChange(val) {
    console.log("handleSelectionChange", val);
  }

  function handleDown(row) {
    const param = {
      filePath: row.mediaPath,
      fileName: row.mediaName
    };
    dronesMediaDownloadByPath(param);
  }
  const onUpload = async option => {
    const loading = ElLoading.service({
      lock: true,
      text: "上传中",
      background: "rgba(0, 0, 0, 0.7)"
    });
    const formData = new FormData();

    const modelFile = {
      id: addForm.value.id,
      mediaType: option.file.name.split(".").pop(),
      remarks: addForm.value.remarks
    };
    formData.append("file", option.file);
    formData.append("mediaInfo", JSON.stringify(modelFile));
    await dronesMediaUploadFile(formData).then(res => {
      if (res.code == SUCCESS) {
        console.log(res.data);
        message("上传成功！", { type: "success" });
        cancel();
      } else {
        message("上传失败", { type: "error" });
      }
    });
    loading.close();
  };

  // 查询
  async function onSearch() {
    loading.value = true;
    console.log("查询信息");
    const page = {
      size: pagination.pageSize,
      current: pagination.currentPage
    };
    const query = {
      ...page,
      ...queryForm.value
    };
    if (query.endTime) {
      query.endTime = query.endTime + " 23:59:59";
    }
    const { data } = await dronesMediaPage(query);
    dataList.value = data.records;
    pagination.total = data.total;
    setTimeout(() => {
      loading.value = false;
    }, 500);
  }

  const resetForm = formEl => {
    if (!formEl) return;
    nextTick(() => {
      formEl.formInstance.clearValidate();
      console.log("resetForm");
    });
  };

  const restartForm = formEl => {
    if (!formEl) return;
    formEl.resetFields();
    cancel();
  };
  // 取消
  function cancel() {
    addForm.value = {
      id: null,
      remarks: ""
    };
    queryForm.value.name = "";
    queryForm.value.beginTime = "";
    queryForm.value.endTime = "";
    dialogFormVisible.value = false;
    onSearch();
  }
  // 打开弹框
  function openDia(param, formEl) {
    dialogFormVisible.value = true;
    title.value = param;
    resetForm(formEl);
  }

  onMounted(() => {
    onSearch();
  });

  return {
    queryForm,
    dataList,
    loading,
    dialogFormVisible,
    title,
    pagination,
    addForm,
    rules,
    columns,
    fileList,
    buttonClass,
    moreCondition,
    onSearch,
    resetForm,
    handleUpdate,
    handleDelete,
    handleSizeChange,
    handleCurrentChange,
    handleSelectionChange,
    handleDown,
    cancel,
    restartForm,
    openDia,
    onUpload
  };
}
