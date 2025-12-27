<script setup lang="ts">
import { computed, nextTick, ref, watch } from "vue";
import { useAiApplication } from "../hook";
import { type PlusColumn, PlusDialogForm } from "plus-pro-components";
import { FormInstance } from "element-plus";
import { aiKnowledgeBaseAllSelectOption } from "@/api/aiKnowledgeBase";
import { getRoleSelectList } from "@/api/system";
import { useProcess } from "@/views/ai/application/hooks/processHook";

const addFormRef = ref<FormInstance>();

const { dialogFormVisible, rules } = useAiApplication();
const { toProcessDetail } = useProcess();

const columnsForm: PlusColumn[] = [
  {
    label: "应用名称",
    prop: "name",
    valueType: "copy"
  },
  {
    label: "应用类型",
    prop: "type",
    valueType: "select",
    options: [
      {
        label: "简单应用",
        value: "简单应用"
      },
      {
        label: "复杂应用",
        value: "复杂应用"
      }
    ],
    formItemProps: {
      labelWidth: "100px",
      style: {
        width: "100%"
      }
    }
  },
  {
    label: "关联知识库",
    prop: "knowledgeIdList",
    valueType: "select",
    options: async () => {
      const { data } = await aiKnowledgeBaseAllSelectOption();
      return data;
    },
    hideInForm: computed(() => formData.value.type === "复杂应用"),
    fieldProps: {
      multiple: true
    },
    formItemProps: {
      labelWidth: "100px",
      style: {
        width: "100%"
      }
    }
  },
  {
    label: "应用权限",
    prop: "roleIdList",
    valueType: "select",
    options: async () => {
      const { data } = await getRoleSelectList();
      return data;
    },
    fieldProps: {
      multiple: true
    },
    formItemProps: {
      labelWidth: "100px",
      style: {
        width: "100%"
      }
    }
  },
  {
    label: "提示词",
    prop: "prompt",
    valueType: "copy",
    hideInForm: computed(() => formData.value.type === "复杂应用")
  },
  {
    label: "历史记录条数",
    prop: "mixHistory",
    valueType: "input-number",
    fieldProps: { max: 1000, min: 100 },
    formItemProps: {
      labelWidth: "110px"
    },
    hideInForm: computed(() => formData.value.type === "复杂应用")
  },
  {
    label: "描述",
    prop: "description",
    valueType: "textarea",
    fieldProps: {
      maxlength: 10,
      showWordLimit: true,
      autosize: { minRows: 2, maxRows: 4 }
    }
  }
];

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  data: {
    type: Object,
    default: () => {
      return {};
    }
  },
  title: {
    type: String,
    default: "添加应用"
  }
});

const formData = ref(props.data);

const isShowNext = ref(false);

const emit = defineEmits(["update:visible", "cancelForm", "submitForm"]);
const cancel = () => {
  emit("cancelForm");
};
const submit = () => {
  emit("submitForm");
};

const openProcess = () => {
  console.log("formData--value", formData.value);
  if (formData.value.id && formData.value.type === "复杂应用") {
    toProcessDetail({
      appId: formData.value.id,
      name: formData.value.name,
      roleIdList: formData.value.roleIdList,
      description: formData.value.description
    });
  } else {
    toProcessDetail({
      name: formData.value.name,
      roleIdList: formData.value.roleIdList,
      description: formData.value.description
    });
  }
};

const resetForm = formEl => {
  if (!formEl) return;
  nextTick(() => {
    formEl.value.formInstance.clearValidate();
    console.log("resetForm");
  });
};
watch(
  () => dialogFormVisible.value,
  val => {
    emit("update:visible", val);
  }
);

watch(
  () => props.visible,
  val => {
    dialogFormVisible.value = val;
    resetForm(addFormRef);
  }
);

watch(
  () => props.data,
  val => {
    formData.value = val;
  }
);

watch(
  () => formData.value.type,
  val => {
    if (val === "复杂应用") {
      isShowNext.value = true;
    } else {
      isShowNext.value = false;
    }
  }
);
</script>

<template>
  <PlusDialogForm
    ref="addFormRef"
    v-model:visible="dialogFormVisible"
    v-model="formData"
    :dialog="{ title: title }"
    :form="{
      columns: columnsForm,
      rules,
      labelWidth: '100px'
    }"
    @cancel="cancel"
    @confirm="submit"
  >
    <template #dialog-footer="{ handleConfirm, handleCancel }">
      <div style="margin: 0 auto">
        <el-button type="primary" v-if="isShowNext" @click="openProcess"
          >下一步</el-button
        >
        <el-button type="primary" v-if="!isShowNext" @click="handleConfirm"
          >确定</el-button
        >
        <el-button type="warning" @click="handleCancel">取消</el-button>
      </div>
    </template>
  </PlusDialogForm>
</template>
