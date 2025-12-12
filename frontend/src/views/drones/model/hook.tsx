import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import {
  ElLoading,
  type UploadFile,
  type FormRules,
  type UploadUserFile
} from "element-plus";
import {
  dronesModelPage,
  dronesModelDelete,
  dronesModelUploadFile
} from "@/api/dronesModel";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";

export function useDronesModel() {
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
  const uploadFileTemp = ref<UploadFile>(null);

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    modelType: "",
    modelName: "",
    remarks: ""
  });
  const rules = reactive<FormRules>({
    modelName: [{ required: true, message: "模型名称必填", trigger: "blur" }],
    modelType: [{ required: true, message: "模型类型必填", trigger: "blur" }],
    remarks: [{ required: true, message: "文件描述必填", trigger: "blur" }]
  });
  const columns: TableColumnList = [
    {
      type: "selection",
      width: 50,
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
      label: "模型名称",
      prop: "modelName",
      width: 100
    },
    {
      label: "模型类型",
      prop: "modelType",
      width: 100,
      cellRenderer: ({ row }) => (
        <el-tag type={row.modelType === "map" ? "success" : "danger"}>
          {row.modelType === "map"
            ? "地图"
            : row.modelType === "vision"
              ? "视觉"
              : "未知"}
        </el-tag>
      )
    },
    {
      label: "文件名称",
      prop: "fileName",
      width: 150
    },
    {
      label: "文件格式",
      prop: "fileFormat",
      width: 100
    },
    {
      label: "文件大小（字节）",
      prop: "fileSize",
      width: 150
    },
    {
      label: "描述信息",
      prop: "remarks",
      width: 150
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 180
    },
    {
      label: "操作",
      fixed: "right",
      minWidth: 180,
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
    dronesModelDelete(row.id).then(res => {
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
    const { data } = await dronesModelPage(query);
    dataList.value = data.records;
    pagination.total = data.total;
    setTimeout(() => {
      loading.value = false;
    }, 500);
  }

  const resetForm = formEl => {
    if (!formEl) return;
    nextTick(() => {
      formEl.clearValidate();
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
      modelType: "",
      modelName: "",
      remarks: ""
    };
    fileList.value = [];
    queryForm.value.name = "";
    queryForm.value.beginTime = "";
    queryForm.value.endTime = "";
    dialogFormVisible.value = false;
    uploadFileTemp.value = null;
    onSearch();
  }
  // 打开弹框
  function openDia(param, formEl) {
    dialogFormVisible.value = true;
    title.value = param;
    resetForm(formEl);
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
      modelType: addForm.value.modelType,
      modelName: addForm.value.modelName,
      fileName: option.file.name,
      fileFormat: option.file.name.split(".").pop(),
      fileSize: option.file.size,
      remarks: addForm.value.remarks
    };
    formData.append("file", option.file);
    formData.append("modelInfo", JSON.stringify(modelFile));
    await dronesModelUploadFile(formData).then(res => {
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
    fileList,
    columns,
    buttonClass,
    moreCondition,
    uploadFileTemp,
    onSearch,
    resetForm,
    handleUpdate,
    handleDelete,
    handleSizeChange,
    handleCurrentChange,
    handleSelectionChange,
    cancel,
    restartForm,
    openDia,
    onUpload
  };
}
