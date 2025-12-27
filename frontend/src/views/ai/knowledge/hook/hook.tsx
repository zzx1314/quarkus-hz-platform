import { computed, nextTick, onMounted, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import type { FormRules } from "element-plus";
import {
  aiKnowledgeBaseSave,
  aiKnowledgeBasePage,
  aiKnowledgeBaseUpdate,
  aiKnowledgeBaseDelete
} from "@/api/aiKnowledgeBase";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import type { FieldValues } from "plus-pro-components";
import { useDetail } from "@/views/ai/knowledge/hook/uploadHook";

export function useAiKnowledgeBase() {
  // *********变量定义**********
  const queryForm = ref({
    knowledgeBaseName: "",
    beginTime: "",
    endTime: ""
  });
  const moreCondition = ref(false);
  const dataList = ref([]);
  const loading = ref(true);
  const dialogFormVisible = ref(false);
  const title = ref("");
  const { toDetail } = useDetail();

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    knowledgeBaseName: "",
    knowledgeBaseType: "",
    knowledgeBaseDesc: ""
  });
  const rules = reactive<FormRules>({
    knowledgeBaseName: [
      { required: true, message: "名称必填", trigger: "blur" }
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
      label: "知识库名称",
      slot: "content",
      width: 150
    },
    {
      label: "知识库类型",
      prop: "knowledgeBaseType",
      width: 120
    },
    {
      label: "知识库描述",
      prop: "knowledgeBaseDesc",
      width: 250
    },
    {
      label: "文档数量",
      prop: "documentCount",
      width: 100
    },
    {
      label: "关联应用数",
      prop: "appCount",
      width: 100
    },
    {
      label: "创建者",
      prop: "createUser",
      width: 100
    },
    {
      label: "创建时间",
      prop: "createTime",
      minWidth: 180
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

  // *********方法定义**********
  // 修改
  function handleUpdate(row, formEl) {
    console.log(row);
    const data = JSON.stringify(row);
    addForm.value = JSON.parse(data);
    openDia("修改配置", formEl);
  }
  // 删除
  function handleDelete(row) {
    console.log(row);
    aiKnowledgeBaseDelete(row.id).then(res => {
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
      aiKnowledgeBaseUpdate(addForm.value).then(res => {
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
      aiKnowledgeBaseSave(addForm.value).then(res => {
        if (res.code === SUCCESS) {
          message("保存成功！", { type: "success" });
          cancel();
        } else {
          message(res.msg, { type: "error" });
        }
      });
    }
  };
  // 处理上传
  function handlerUpload(row) {
    console.log(row);
    toDetail({ id: row.id, name: row.knowledgeBaseName });
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
    const { data } = await aiKnowledgeBasePage(query);
    dataList.value = data.records;
    pagination.total = data.total;
    setTimeout(() => {
      loading.value = false;
    }, 500);
  }
  // 重置表单
  const resetForm = formEl => {
    if (!formEl) return;
    nextTick(() => {
      formEl.formInstance.clearValidate();
      console.log("resetForm");
    });
  };
  // 重置表单
  const restartForm = formEl => {
    if (!formEl) return;
    formEl.resetFields();
    cancel();
  };
  // 取消
  function cancel() {
    addForm.value = {
      id: null,
      knowledgeBaseName: "",
      knowledgeBaseType: "",
      knowledgeBaseDesc: ""
    };
    queryForm.value.knowledgeBaseName = "";
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
  // 初始化
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
    handlerUpload,
    handleSubmitError,
    cancel,
    restartForm,
    openDia
  };
}
