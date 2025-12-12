import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import { ElMessageBox, type FormInstance, type FormRules } from "element-plus";
import {
  dronesRouteLibrarySave,
  dronesRouteLibraryPage,
  dronesRouteLibraryUpdate,
  dronesRouteLibraryDelete,
  dronesRouteLibraryStartOrStopRoute,
  getRouteItemByDto
} from "@/api/dronesRouteLibrary";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import { usePlanning } from "./planning/planningHook";

export function useDronesRouteLibrary() {
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
  const { toDetail } = usePlanning();
  const switchLoadMap = ref({});

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    routeName: "",
    routeType: "",
    routeDescription: "",
    routeItems: []
  });
  const rules = reactive<FormRules>({
    routeName: [{ required: true, message: "路线名称必填", trigger: "blur" }],
    routeType: [{ required: true, message: "路线类型必填", trigger: "change" }]
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
      label: "路线名称",
      prop: "routeName",
      width: 100
    },
    {
      label: "路线类型",
      prop: "routeType",
      width: 100,
      cellRenderer: ({ row, props }) => (
        <el-tag
          size={props.size}
          type={row.routeType === "在线地图" ? "success" : "warning"}
        >
          {row.routeType}
        </el-tag>
      )
    },
    {
      label: "路线状态",
      prop: "isEnable",
      width: 100,
      cellRenderer: scope => (
        <el-switch
          size={scope.props.size === "small" ? "small" : "default"}
          loading={switchLoadMap.value[scope.index]?.loading}
          v-model={scope.row.routeStatus}
          active-value={"启用"}
          inactive-value={"停用"}
          active-text="启用"
          inactive-text="停用"
          inline-prompt
          onChange={() => onChange(scope as any)}
        />
      )
    },
    {
      label: "路线描述",
      prop: "routeDescription",
      minWidth: 100
    },
    {
      label: "起点坐标",
      prop: "startCoordinates",
      width: 200
    },
    {
      label: "终点坐标",
      prop: "endCoordinates",
      width: 200
    },
    {
      label: "航点信息",
      prop: "routeData",
      width: 250
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 170
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
  function onChange({ row, index }) {
    ElMessageBox.confirm(
      `确认要<strong>${
        row.routeStatus
      }</strong><strong style='color:var(--el-color-primary)'>${
        row.routeName
      }</strong>航线吗?`,
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
        switchLoadMap.value[index] = Object.assign(
          {},
          switchLoadMap.value[index],
          {
            loading: true
          }
        );
        const updateParam = {
          id: row.id,
          status: row.routeStatus === "启用" ? "start" : "stop"
        };
        dronesRouteLibraryStartOrStopRoute(updateParam).then(res => {
          switchLoadMap.value[index] = Object.assign(
            {},
            switchLoadMap.value[index],
            {
              loading: false
            }
          );
          if (res.code === SUCCESS) {
            message("已成功修改航线状态", {
              type: "success"
            });
          } else {
            message(res.msg, {
              type: "error"
            });
            row.routeStatus === "启用"
              ? (row.routeStatus = "停用")
              : (row.routeStatus = "启用");
          }
        });
      })
      .catch(() => {
        row.routeStatus === "启用"
          ? (row.routeStatus = "停用")
          : (row.routeStatus = "启用");
      });
  }
  function handleUpdate(row, formEl) {
    console.log(row);
    if (row.routeStatus === "启用") {
      message("请先停用该航线！", {
        type: "warning"
      });
      return;
    }
    const data = JSON.stringify(row);
    addForm.value = JSON.parse(data);
    // 需要补充航点参数
    const param = {
      routeLibraryId: row.id
    };
    getRouteItemByDto(param).then(res => {
      if (res.code === SUCCESS && res.data) {
        addForm.value.routeItems = res.data;
      }
    });
    openDia("修改", formEl);
  }
  // 删除
  function handleDelete(row) {
    console.log(row);
    if (row.routeStatus === "启用") {
      message("请先停用该航线！", {
        type: "warning"
      });
      return;
    }
    dronesRouteLibraryDelete(row.id).then(res => {
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
        if (addForm.value.id) {
          // 修改
          console.log("修改");
          dronesRouteLibraryUpdate(addForm.value).then(res => {
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
          dronesRouteLibrarySave(addForm.value).then(res => {
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
    const { data } = await dronesRouteLibraryPage(query);
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
      routeName: "",
      routeType: "",
      routeDescription: "",
      routeItems: []
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

  function designRoute(row) {
    console.log("设计航线", row);
    const param = {
      id: row.id,
      name: row.routeName,
      type: row.routeType,
      routeData: row.routeData,
      modelId: row.modelId
    };
    toDetail(param);
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
    openDia,
    designRoute
  };
}
