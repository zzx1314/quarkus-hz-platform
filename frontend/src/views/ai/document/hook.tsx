import { computed, nextTick, reactive, ref } from "vue";
import type { PaginationProps } from "@pureadmin/table";
import {
  ElLoading,
  ElMessageBox,
  type FormRules,
  type UploadUserFile
} from "element-plus";
import {
  aiDocumentPage,
  aiDocumentDelete,
  aiDocumentUploadDoc
} from "@/api/aiDocument";
import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import { useDocument } from "@/views/ai/knowledge/hook/docHook";
import { useRouter } from "vue-router";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { usePublicHooks } from "@/views/system/hooks";

export function useAiDocument() {
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
  const { initToDetail, getParameter } = useDocument();
  const router = useRouter();
  const knowledgeName = getParameter.name;
  const knowledgeId = Number(getParameter.id);
  const switchLoadMap = ref({});
  const { switchStyle } = usePublicHooks();
  const fileList = ref<UploadUserFile[]>([]);
  initToDetail();

  const pagination = reactive<PaginationProps>({
    total: 0,
    pageSize: 10,
    currentPage: 1,
    background: true
  });
  const addForm = ref({
    id: null,
    strategy: "",
    knowledgeBaseId: null,
    flag: "",
    length: 500
  });
  const rules = reactive<FormRules>({
    strategy: [{ required: true, message: "分段策略必填", trigger: "change" }],
    length: [{ required: true, message: "分段长度必填", trigger: "blur" }],
    hitHandle: [{ required: true, message: "命中处理必填", trigger: "change" }],
    flag: [{ required: true, message: "分段标识必填", trigger: "change" }],
    acquaintanceLevel: [
      { required: true, message: "相似度策略必填", trigger: "change" }
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
      label: "文件名称",
      slot: "content",
      width: 250
    },
    {
      label: "字符数",
      prop: "characterNumber",
      width: 100
    },
    {
      label: "分段数",
      prop: "sectionNumber",
      width: 100
    },
    {
      label: "文件状态",
      prop: "status",
      cellRenderer: ({ row, props }) => (
        <el-tag
          size={props.size}
          type={row.status === "成功" ? "success" : "warning"}
        >
          {row.status}
        </el-tag>
      ),
      width: 100
    },
    {
      label: "启用状态",
      prop: "enableStatus",
      width: 100,
      cellRenderer: scope => (
        <el-switch
          size={scope.props.size === "small" ? "small" : "default"}
          loading={switchLoadMap.value[scope.index]?.loading}
          v-model={scope.row.enableStatus}
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
      label: "分段策略",
      prop: "splitterStrategy",
      cellRenderer: ({ row, props }) => (
        <el-tag size={props.size} type={"success"}>
          {row.splitterStrategy === "regexSplitter"
            ? "智能分段"
            : row.splitterStrategy === "headingSplitter"
              ? "标题分段"
              : "全文分段"}
        </el-tag>
      ),
      width: 100
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

  // *********方法定义**********
  function handleUpdate(row, formEl) {
    console.log(row);
    const data = JSON.stringify(row);
    const dataJson = JSON.parse(data);
    addForm.value.id = dataJson.id;
    addForm.value.flag = dataJson.splitterFlag;
    addForm.value.strategy = dataJson.splitterStrategy;
    addForm.value.length = Number(dataJson.splitterLength);
    fileList.value.push({
      name: row.docName,
      status: "success"
    });
    openDia("修改配置", formEl);
  }

  // 删除
  function handleDelete(row) {
    console.log(row);
    aiDocumentDelete(row.id).then(res => {
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

  // 查询
  async function onSearch() {
    loading.value = true;
    console.log("查询信息");
    const page = {
      knowledgeId: getParameter.id,
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
    const { data } = await aiDocumentPage(query);
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
      strategy: "",
      knowledgeBaseId: null,
      flag: "",
      length: 500
    };
    queryForm.value.name = "";
    queryForm.value.beginTime = "";
    queryForm.value.endTime = "";
    dialogFormVisible.value = false;
    fileList.value = [];
    onSearch();
  }

  // 打开弹框
  function openDia(param, formEl) {
    dialogFormVisible.value = true;
    title.value = param;
    resetForm(formEl);
  }

  const back = () => {
    console.log("back");
    useMultiTagsStoreHook().handleTags("splice", "/ai/document/index");
    router.push({ path: "/ai/knowledge/index" });
  };

  function onChange({ row, index }) {
    ElMessageBox.confirm(
      `确定要<strong>${
        row.enableStatus
      }</strong><strong style='color:var(--el-color-primary)'>${
        row.docName
      }</strong>文档吗?`,
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
        setTimeout(() => {
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
        }, 300);
      })
      .catch(() => {
        row.enableStatus = row.enableStatus === "启用" ? "停用" : "启用";
      });
  }

  // 上传文件
  const onUpload = async option => {
    const loading = ElLoading.service({
      lock: true,
      text: "上传中",
      background: "rgba(0, 0, 0, 0.7)"
    });
    const formData = new FormData();

    const aiDoc = {
      id: addForm.value.id,
      strategy: addForm.value.strategy,
      knowledgeBaseId: getParameter.id,
      flag: addForm.value.flag,
      length: addForm.value.length
    };
    formData.append("file", option.file);
    formData.append("strategy", JSON.stringify(aiDoc));
    await aiDocumentUploadDoc(formData).then(res => {
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
    knowledgeName,
    knowledgeId,
    fileList,
    onUpload,
    onSearch,
    resetForm,
    handleUpdate,
    handleDelete,
    handleSizeChange,
    handleCurrentChange,
    handleSelectionChange,
    handleSubmitError,
    cancel,
    restartForm,
    openDia,
    back
  };
}
