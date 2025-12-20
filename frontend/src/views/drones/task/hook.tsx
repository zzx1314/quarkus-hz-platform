import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import type { FormRules } from "element-plus";
import { ElMessageBox } from "element-plus";
import {
  dronesTaskSave,
  dronesTaskPage,
  dronesTaskUpdate,
  dronesTaskDelete,
  dronesTaskStart
} from "@/api/dronesTask";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import type { FieldValues } from "plus-pro-components";
import { useProcess } from "./flowHook";
import { uuid } from "@pureadmin/utils";

export function useDronesTask() {
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
  const dialogResultVisible = ref(false);
  const title = ref("");
  const { toDetail } = useProcess();

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null
  });
  const rules = reactive<FormRules>({
    taskName: [{ required: true, message: "任务名称必填", trigger: "blur" }],
    taskDescription: [
      { required: true, message: "任务描述必填", trigger: "blur" }
    ]
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
      label: "任务名称",
      prop: "taskName",
      width: 150
    },
    {
      label: "任务描述",
      prop: "taskDescription",
      minWidth: 180
    },
    {
      label: "任务状态",
      prop: "taskStatus",
      cellRenderer: ({ row, props }) => (
        <el-tag
          size={props.size}
          type={
            row.taskStatus === "成功"
              ? "success"
              : row.taskStatus === "失败"
                ? "error"
                : "warning"
          }
        >
          {row.taskStatus}
        </el-tag>
      ),
      width: 100
    },
    {
      label: "任务结果",
      prop: "taskResult",
      width: 180
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
    dronesTaskDelete(row.id).then(res => {
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
  const handleSubmit = (values: FieldValues) => {
    console.log(values, "Submit");
    if (addForm.value.id) {
      // 修改
      console.log("修改");
      dronesTaskUpdate(addForm.value).then(res => {
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
      dronesTaskSave(addForm.value).then(res => {
        if (res.code === SUCCESS) {
          message("保存成功！", { type: "success" });
          cancel();
        } else {
          message(res.msg, { type: "error" });
        }
      });
    }
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
    const { data } = await dronesTaskPage(query);
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
      id: null
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

  // 流程设计
  function flowRoute(row) {
    console.log("flowRoute", row);
    const param = {
      taskId: row.id,
      taskType: "taskDesign",
      name: row.taskName,
      workflowId: row.workflowUuid ? row.workflowUuid : uuid(5)
    };
    toDetail(param);
  }
  // 流程执行状态
  function flowRouteStatus(row) {
    console.log("flowRouteStatus", row);
    const param = {
      taskId: row.id,
      taskType: "taskStatus",
      name: row.taskName,
      workflowId: row.workflowUuid ? row.workflowUuid : uuid(5)
    };
    toDetail(param);
  }
  // 启动任务
  function startTask(row) {
    console.log("startTask", row);
    ElMessageBox.confirm(
      `确定要<strong>启动</strong><strong style='color:var(--el-color-primary)'>${
        row.taskName
      }</strong>任务吗?`,
      "系统提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
        draggable: true
      }
    )
      .then(() => {
        dronesTaskStart(row.id).then(res => {
          if (res.code === SUCCESS) {
            message("任务启动成功", {
              type: "success"
            });
          }
        });
      })
      .catch(() => {
        message("已取消", {
          type: "info"
        });
      });
  }
  // 数据看板
  function datadashBoard(row) {
    console.log("datadashBoard", row);
    // 跳转数据看板页面
    /* router.push({
      name: "datadashboardIndex",
      params: {
        taskId: row.id
      }
    }); */
    window.open(`/#/datadashboard?type=datadash&taskId=` + row.id, "_blank");
  }

  onMounted(() => {
    onSearch();
  });

  return {
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
    openDia,
    flowRoute,
    flowRouteStatus,
    startTask,
    datadashBoard
  };
}
