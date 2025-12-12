import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import type { FormRules } from "element-plus";
import {
  dronesServicesPage,
  dronesServicesSave,
  dronesServicesUpdate,
  dronesServicesDelete
} from "@/api/dronesService";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import type { FormInstance } from "element-plus";
export function useDronesConfig() {
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

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    type: "",
    params: "",
    yolo: {
      model: "",
      input: "",
      event: {
        onStart: "yolo_start",
        onStop: "yolo_stop",
        onDetected: "yolo_detected",
        onLost: "yolo_lost"
      }
    },
    deepsort: {
      max_age: 30,
      event: {
        onStart: "deepsort_start",
        onStop: "deepsort_stop",
        onTracked: "deepsort_tracked",
        onLost: "deepsort_lost"
      }
    },
    rtsp: {
      url: "",
      bitrate: "",
      event: {
        onStart: "rtsp_start",
        onStop: "rtsp_stop",
        onComplete: "rtsp_pushcomplete"
      }
    }
  });
  const rules = reactive<FormRules>({
    type: [{ required: true, message: "服务类型必填", trigger: "blur" }]
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
      label: "服务类型",
      prop: "type",
      width: 150
    },
    {
      label: "服务参数",
      prop: "params",
      width: 700
    },
    {
      label: "创建时间",
      prop: "createTime",
      minWidth: 100
    },
    {
      label: "操作",
      fixed: "right",
      width: 180,
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
    if (row.type === "YOLO") {
      addForm.value.yolo = JSON.parse(row.params);
    } else if (row.type === "DEEPSORT") {
      addForm.value.deepsort = JSON.parse(row.params);
    } else if (row.type === "RTSP") {
      addForm.value.rtsp = JSON.parse(row.params);
    }
    openDia("修改配置", formEl);
  }
  // 删除
  function handleDelete(row) {
    console.log(row);
    dronesServicesDelete(row.id).then(res => {
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

  const handleSubmitError = (err: any) => {
    console.log(err, "err");
  };

  // 保存
  const handleSubmit = async (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    await formEl.validate(valid => {
      if (valid) {
        if (addForm.value.type === "YOLO") {
          addForm.value.params = JSON.stringify(addForm.value.yolo);
        } else if (addForm.value.type === "DEEPSORT") {
          addForm.value.params = JSON.stringify(addForm.value.deepsort);
        } else if (addForm.value.type === "RTSP") {
          addForm.value.params = JSON.stringify(addForm.value.rtsp);
        }
        if (addForm.value.id) {
          // 修改
          console.log("修改");
          dronesServicesUpdate(addForm.value).then(res => {
            if (res.code === SUCCESS) {
              message("修改成功！", { type: "success" });
              cancel();
            } else {
              message("修改失败！", { type: "error" });
            }
          });
        } else {
          // 新增
          console.log("新增");
          dronesServicesSave(addForm.value).then(res => {
            if (res.code === SUCCESS) {
              message("保存成功！", { type: "success" });
              cancel();
            } else {
              message(res.msg, { type: "error" });
            }
          });
        }
      }
    });
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
    const { data } = await dronesServicesPage(query);
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
      type: "",
      params: "",
      yolo: {
        model: "",
        input: "",
        event: {
          onStart: "yolo_start",
          onStop: "yolo_stop",
          onDetected: "yolo_detected",
          onLost: "yolo_lost"
        }
      },
      deepsort: {
        max_age: 30,
        event: {
          onStart: "deepsort_start",
          onStop: "deepsort_stop",
          onTracked: "deepsort_tracked",
          onLost: "deepsort_lost"
        }
      },
      rtsp: {
        url: "",
        bitrate: "",
        event: {
          onStart: "rtsp_start",
          onStop: "rtsp_stop",
          onComplete: "rtsp_pushcomplete"
        }
      }
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
    buttonClass,
    moreCondition,
    onSearch,
    resetForm,
    handleUpdate,
    handleDelete,
    handleSizeChange,
    handleCurrentChange,
    handleSelectionChange,
    handleSubmit,
    handleSubmitError,
    cancel,
    restartForm,
    openDia
  };
}
