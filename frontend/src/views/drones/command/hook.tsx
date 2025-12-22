import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import type { FormRules } from "element-plus";
import {
  dronesCommandSave,
  dronesCommandPage,
  dronesCommandUpdate,
  dronesCommandDelete
} from "@/api/dronesCommand";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import type { FieldValues } from "plus-pro-components";

export function useDronesCommand() {
  /**
   * ==============================
   * State - 状态定义
   * ==============================
   */

  // 查询条件
  const queryForm = ref({
    name: "",
    beginTime: "",
    endTime: ""
  });
  const moreCondition = ref(false);

  // 表格数据
  const dataList = ref([]);
  const loading = ref(true);

  // 弹窗 & 表单
  const dialogFormVisible = ref(false);
  const title = ref("");
  const addForm = ref({
    id: null
  });

  // 分页
  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });

  /**
   * ==============================
   * Static Config - 静态配置
   * ==============================
   */

  // 表单校验规则
  const rules = reactive<FormRules>({
    name: [{ required: true, message: "名称必填", trigger: "blur" }]
  });

  // 表格列
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
      label: "指令名称",
      prop: "commandName",
      width: 100
    },
    {
      label: "指令参数",
      prop: "commandParams",
      width: 100
    },
    {
      label: "目标设备ID",
      prop: "deviceId",
      width: 100
    },
    {
      label: "指令状态",
      prop: "status",
      width: 100
    },
    {
      label: "返回值",
      prop: "returnValue",
      width: 100
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 100
    },
    {
      label: "操作",
      fixed: "right",
      width: 180,
      slot: "operation"
    }
  ];

  /**
   * ==============================
   * Computed - 计算属性
   * ==============================
   */

  const buttonClass = computed(() => {
    return [
      "!h-[20px]",
      "reset-margin",
      "!text-gray-500",
      "dark:!text-white",
      "dark:hover:!text-primary"
    ];
  });

  /**
   * ==============================
   * Actions - 行为方法
   * ==============================
   */

  // ===== 表格行为 =====

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

  // ===== 表单行为 =====

  function handleUpdate(row, formEl) {
    console.log(row);
    const data = JSON.stringify(row);
    addForm.value = JSON.parse(data);
    openDia("修改配置", formEl);
  }

  function handleDelete(row) {
    console.log(row);
    dronesCommandDelete(row.id).then(res => {
      if (res.code === SUCCESS) {
        message("删除成功！", { type: "success" });
        onSearch();
      } else {
        message(res.msg, { type: "error" });
      }
    });
  }

  const handleSubmit = (values: FieldValues) => {
    console.log(values, "Submit");
    if (addForm.value.id) {
      console.log("修改");
      dronesCommandUpdate(addForm.value).then(res => {
        if (res.code === SUCCESS) {
          message("修改成功！", { type: "success" });
          cancel();
        } else {
          message("修改失败！", { type: "error" });
        }
      });
    } else {
      console.log("新增");
      dronesCommandSave(addForm.value).then(res => {
        if (res.code === SUCCESS) {
          message("保存成功！", { type: "success" });
          cancel();
        } else {
          message(res.msg, { type: "error" });
        }
      });
    }
  };

  const handleSubmitError = (err: any) => {
    console.log(err, "err");
  };

  // ===== 查询 =====

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
    const { data } = await dronesCommandPage(query);
    dataList.value = data.records;
    pagination.total = data.total;
    setTimeout(() => {
      loading.value = false;
    }, 500);
  }

  // ===== 弹窗 & 表单工具 =====

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

  function openDia(param, formEl) {
    dialogFormVisible.value = true;
    title.value = param;
    resetForm(formEl);
  }

  /**
   * ==============================
   * Lifecycle - 生命周期
   * ==============================
   */

  onMounted(() => {
    onSearch();
  });

  /**
   * ==============================
   * Expose - 对外暴露
   * ==============================
   */

  return {
    // state
    queryForm,
    dataList,
    loading,
    pagination,
    addForm,
    dialogFormVisible,
    title,
    moreCondition,

    // config
    rules,
    columns,
    buttonClass,

    // actions
    onSearch,
    handleUpdate,
    handleDelete,
    handleSizeChange,
    handleCurrentChange,
    handleSelectionChange,
    handleSubmit,
    handleSubmitError,
    resetForm,
    restartForm,
    cancel,
    openDia
  };
}
