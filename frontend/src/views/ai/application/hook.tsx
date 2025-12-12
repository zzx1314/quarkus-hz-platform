import { nextTick, reactive, ref } from "vue";
import type { FormRules } from "element-plus";
import {
  aiApplicationSave,
  aiApplicationPage,
  aiApplicationUpdate,
  aiApplicationDelete,
  aiApplicationEnable
} from "@/api/aiApplication";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import type { FieldValues } from "plus-pro-components";

export function useAiApplication() {
  // *********变量定义**********
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
  const applicationList = ref([]);
  const dataLoading = ref(true);

  const pagination = ref({ current: 1, pageSize: 12, total: 0 });
  const addForm = ref({
    id: null,
    name: "",
    description: "",
    type: "",
    model: "",
    aiRole: "",
    prompt: "",
    mixHistory: 10
  });
  const rules = reactive<FormRules>({
    name: [{ required: true, message: "应用名称必填", trigger: "blur" }],
    type: [{ required: true, message: "应用类型必填", trigger: "change" }],
    knowledgeIdList: [
      { required: true, message: "关联知识库必填", trigger: "change" }
    ],
    roleIdList: [{ required: true, message: "应用权限必填", trigger: "change" }]
  });
  // *********方法定义**********
  function handleUpdate(row) {
    console.log(row);
    const data = JSON.stringify(row);
    addForm.value = JSON.parse(data);
    openDia("修改");
  }
  // 删除
  function handleDelete(row) {
    console.log(row);
    aiApplicationDelete(row.id).then(res => {
      if (res.code === SUCCESS) {
        message("删除成功！", { type: "success" });
        getCardListData();
      } else {
        message(res.msg, { type: "error" });
      }
    });
  }
  // 启动
  function handleEnable(row) {
    aiApplicationEnable(row).then(res => {
      if (res.code === SUCCESS) {
        const status = !row.isSetup ? "启用" : "停用";
        message(status + "成功！", { type: "success" });
        getCardListData();
      } else {
        message(res.msg, { type: "error" });
      }
    });
  }

  const onPageSizeChange = (size: number) => {
    pagination.value.pageSize = size;
    pagination.value.current = 1;
  };
  const onCurrentChange = (current: number) => {
    pagination.value.current = current;
  };

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
      aiApplicationUpdate(addForm.value).then(res => {
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
      aiApplicationSave(addForm.value).then(res => {
        if (res.code === SUCCESS) {
          message("保存成功！", { type: "success" });
          cancel();
        } else {
          message(res.msg, { type: "error" });
        }
      });
    }
  };

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
      name: "",
      description: "",
      type: "",
      model: "",
      aiRole: "",
      prompt: "",
      mixHistory: 10
    };
    queryForm.value.name = "";
    queryForm.value.beginTime = "";
    queryForm.value.endTime = "";
    dialogFormVisible.value = false;
    getCardListData();
  }
  // 打开弹框
  function openDia(param) {
    dialogFormVisible.value = true;
    title.value = param;
  }

  const getCardListData = async () => {
    console.log("getCardListData");
    try {
      const { data } = await aiApplicationPage();
      applicationList.value = data.records;
      pagination.value = {
        ...pagination.value,
        total: data.records.length
      };
    } catch (e) {
      console.log(e);
    } finally {
      setTimeout(() => {
        dataLoading.value = false;
      }, 500);
    }
  };

  return {
    queryForm,
    dataList,
    loading,
    dialogFormVisible,
    title,
    pagination,
    addForm,
    rules,
    moreCondition,
    applicationList,
    dataLoading,
    getCardListData,
    resetForm,
    handleUpdate,
    handleDelete,
    onPageSizeChange,
    onCurrentChange,
    handleSelectionChange,
    handleSubmit,
    handleSubmitError,
    handleEnable,
    cancel,
    restartForm,
    openDia
  };
}
