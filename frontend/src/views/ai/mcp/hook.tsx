import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import {
  ElLoading,
  type UploadUserFile,
  type FormRules,
  ElMessageBox
} from "element-plus";
import {
  aiMcpPage,
  aiMcpDelete,
  aiMcpUploadFile,
  aiMcpUpdate,
  aiMcpSave,
  aiMcpEnable
} from "@/api/aiMcp";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import type { FieldValues } from "plus-pro-components";
import { usePublicHooks } from "@/views/system/hooks";
import { aiMcpToolsPage } from "@/api/aiMcpTools";

export function useAiMcp() {
  // *********变量定义**********
  const queryForm = ref({
    name: "",
    beginTime: "",
    endTime: ""
  });
  const moreCondition = ref(false);
  const dataList = ref([]);
  const dataListTools = ref([]);
  const loading = ref(true);
  const dialogFormVisible = ref(false);
  const dialogTableVisible = ref(false);
  const title = ref("");
  const fileList = ref<UploadUserFile[]>([]);
  const switchLoadMap = ref({});
  const { switchStyle } = usePublicHooks();
  const currentRow = ref(null);

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });

  const paginationTools = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    name: "",
    description: "",
    commandType: "",
    command: ""
  });
  const rules = reactive<FormRules>({
    name: [{ required: true, message: "名称必填", trigger: "blur" }],
    commandType: [{ required: true, message: "命令类型", trigger: "blur" }]
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
      label: "名称",
      prop: "name",
      width: 200
    },
    {
      label: "类型",
      prop: "commandType",
      width: 100
    },
    {
      label: "文件名称",
      prop: "originFileName",
      width: 250
    },
    {
      label: "工具数量",
      slot: "content",
      width: 100
    },
    {
      label: "描述",
      prop: "description",
      width: 180
    },
    {
      label: "启动",
      prop: "enable",
      width: 150,
      cellRenderer: scope => (
        <el-switch
          size={scope.props.size === "small" ? "small" : "default"}
          loading={switchLoadMap.value[scope.index]?.loading}
          v-model={scope.row.enable}
          active-value={"启用"}
          inactive-value={"停用"}
          active-text="已启用"
          inactive-text="已停用"
          inline-prompt
          style={switchStyle.value}
          onChange={() => onChange(scope as any)}
        />
      )
    },
    {
      label: "启动命令",
      prop: "command",
      width: 250
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

  const columnsTool: TableColumnList = [
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
      label: "名称",
      prop: "name",
      width: 150
    },
    {
      label: "描述",
      prop: "description",
      minWidth: 100
    },
    {
      label: "参数",
      prop: "parameters",
      minWidth: 150
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 180
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

  // *********方法定义**********
  function handleUpdate(row, formEl) {
    console.log(row);
    const data = JSON.stringify(row);
    addForm.value = JSON.parse(data);
    fileList.value.push({
      name: row.originFileName,
      status: "success"
    });
    openDia("修改配置", formEl);
  }

  // 删除
  function handleDelete(row) {
    console.log(row);
    aiMcpDelete(row.id).then(res => {
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
  function handleSizeChangeTools(val: number) {
    paginationTools.pageSize = val;
    onSearchTools(currentRow.value);
  }
  function handleCurrentChange(val: number) {
    pagination.currentPage = val;
    onSearch();
  }
  function handleCurrentChangeTools(val: number) {
    paginationTools.currentPage = val;
    onSearchTools(currentRow.value);
  }
  function handleSelectionChange(val) {
    console.log("handleSelectionChange", val);
  }
  function handleSelectionChangeTools(val) {
    console.log("handleSelectionChangeTools", val);
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
      aiMcpUpdate(addForm.value).then(res => {
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
      aiMcpSave(addForm.value).then(res => {
        if (res.code === SUCCESS) {
          message("保存成功！", { type: "success" });
          cancel();
        } else {
          message(res.msg, { type: "error" });
        }
      });
    }
  };

  const onUpload = async option => {
    const loading = ElLoading.service({
      lock: true,
      text: "上传中",
      background: "rgba(0, 0, 0, 0.7)"
    });
    const formData = new FormData();

    const aiMcp = {
      id: addForm.value.id,
      name: addForm.value.name,
      commandType: addForm.value.commandType,
      command: addForm.value.command,
      description: addForm.value.description
    };
    formData.append("file", option.file);
    formData.append("aiMcp", JSON.stringify(aiMcp));
    await aiMcpUploadFile(formData).then(res => {
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
  function onChange({ row, index }) {
    ElMessageBox.confirm(
      `确定要<strong>${
        row.enable
      }</strong><strong style='color:var(--el-color-primary)'>${
        row.name
      }</strong>MCP吗?`,
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
        const param = {
          id: row.id,
          enable: row.enable
        };
        aiMcpEnable(param).then(res => {
          if (res.code === SUCCESS) {
            switchLoadMap.value[index] = Object.assign(
              {},
              switchLoadMap.value[index],
              {
                loading: false
              }
            );
            message("已成功修改状态", {
              type: "success"
            });
          }
        });
      })
      .catch(() => {
        row.enable = row.enable === "启用" ? "停用" : "启用";
      });
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
    const { data } = await aiMcpPage(query);
    dataList.value = data.records;
    pagination.total = data.total;
    setTimeout(() => {
      loading.value = false;
    }, 500);
  }

  // 查询
  async function onSearchTools(row) {
    loading.value = true;
    console.log("查询信息");
    const page = {
      size: paginationTools.pageSize,
      current: paginationTools.currentPage
    };
    const query = {
      ...page,
      ...queryForm.value,
      mcpId: row.id
    };
    if (query.endTime) {
      query.endTime = query.endTime + " 23:59:59";
    }
    const { data } = await aiMcpToolsPage(query);
    dataListTools.value = data.records;
    paginationTools.total = data.total;
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
      name: "",
      description: "",
      commandType: "",
      command: ""
    };
    queryForm.value.name = "";
    queryForm.value.beginTime = "";
    queryForm.value.endTime = "";
    dialogFormVisible.value = false;
    dialogTableVisible.value = false;
    currentRow.value = null;
    fileList.value = [];
    onSearch();
  }

  // 打开弹框
  function openDia(param, formEl) {
    dialogFormVisible.value = true;
    title.value = param;
    resetForm(formEl);
  }

  function openToolPage(row) {
    console.log("打开工具页面", row);
    dialogTableVisible.value = true;
    currentRow.value = row;
    onSearchTools(row);
  }

  onMounted(() => {
    onSearch();
  });

  return {
    queryForm,
    dataList,
    dataListTools,
    loading,
    dialogFormVisible,
    dialogTableVisible,
    title,
    pagination,
    paginationTools,
    addForm,
    rules,
    columns,
    columnsTool,
    buttonClass,
    moreCondition,
    fileList,
    openToolPage,
    onSearch,
    onSearchTools,
    resetForm,
    handleUpdate,
    handleDelete,
    handleSizeChange,
    handleSizeChangeTools,
    handleCurrentChange,
    handleCurrentChangeTools,
    handleSelectionChange,
    handleSelectionChangeTools,
    handleSubmit,
    onUpload,
    handleSubmitError,
    cancel,
    restartForm,
    openDia
  };
}
