<script setup lang="ts">
import { Handle, Position, useVueFlow } from "@vue-flow/core";
import {
  computed,
  inject,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  watch
} from "vue";
import type { FormInstance, FormRules } from "element-plus";
const { updateNodeData } = useVueFlow();
import SettingsIcon from "~icons/ri/settings-3-line";
const errorActionForm = ref({
  nodeType: "action",
  action: {
    type: "",
    params: {
      reason: "",
      event: {
        onComplete: ""
      }
    }
  }
});

const errorActionFormRef = ref<FormInstance>();
const validateOnRuleChange = ref(true);
const isActive = ref(false);

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  data: {
    type: Object,
    required: true
  },
  isSubmit: {
    type: Boolean,
    default: false
  },
  selected: {
    type: Boolean,
    default: false
  },
  status: {
    type: String,
    default: "default"
  },
  getNodeDataById: Function
});

const isSuccess = props.status === "success";
const isError = props.status === "error";

const originalData = computed(() => props.getNodeDataById?.(props.id));
console.log("originalData--error", originalData);
if (originalData.value) {
  errorActionForm.value = {
    nodeType: originalData.value.nodeType || "action",
    action: {
      type: originalData.value.action?.type || "",
      params: {
        reason: originalData.value.action?.params?.reason || "",
        event: {
          onComplete: originalData.value.action?.params?.event?.onComplete || ""
        }
      }
    }
  };
}

const toggleActive = () => {
  emit("select", props.id);
};

const validate = async () => {
  return await submitForm(errorActionFormRef.value);
};

defineExpose({
  validate
});

const emit = defineEmits(["validate-fail", "select"]);

const rules = reactive<FormRules>({
  configId: [{ required: true, message: "配置必填", trigger: "change" }]
});

const handleChange = value => {
  console.log("选项切换", value);
  updateNodeData(props.id, errorActionForm.value);
};

const submitForm = async (
  formEl: FormInstance | undefined
): Promise<boolean> => {
  if (!formEl) return false;
  return await new Promise(resolve => {
    formEl.validate((valid, fields) => {
      if (valid) {
        console.log("submit success!");
        resolve(true);
      } else {
        console.log("submit fail!", fields);
        emit("validate-fail", fields);
        resolve(false);
      }
    });
  });
};

// 注册到父组件
const registerNodeRef = inject("registerNodeRef", null);
const unregisterNodeRef = inject("unregisterNodeRef", null);

watch(
  () => props.selected,
  newVal => {
    isActive.value = newVal;
  }
);

onMounted(() => {
  registerNodeRef?.(props.id, { validate });
});

onBeforeUnmount(() => {
  unregisterNodeRef?.(props.id);
});
</script>

<template>
  <div @click="toggleActive">
    <el-card
      style="max-width: 480px"
      shadow="hover"
      :class="{
        'active-border': isActive,
        'is-success': isSuccess,
        'is-error': isError
      }"
    >
      <template #header>
        <div class="card-header">
          <span class="label-container">
            <SettingsIcon style="font-size: 25px; color: #009966" />
            错误处理
          </span>
        </div>
      </template>
      <el-form
        ref="errorActionFormRef"
        :model="errorActionForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
        :validate-on-rule-change="validateOnRuleChange"
      >
        <el-form-item label="错误类型" prop="type">
          <el-input
            v-model="errorActionForm.action.type"
            placeholder="请输入错误类型"
            @blur="handleChange"
          />
        </el-form-item>
        <el-form-item label="错误原因" prop="type">
          <el-input
            v-model="errorActionForm.action.params.reason"
            placeholder="请输入错误原因"
            @blur="handleChange"
          />
        </el-form-item>
        <el-form-item label="成功事件" prop="onComplete">
          <el-input
            v-model="errorActionForm.action.params.event.onComplete"
            placeholder="请输入成功事件"
            @blur="handleChange"
          />
        </el-form-item>
      </el-form>
    </el-card>
    <Handle
      id="right-output"
      type="source"
      :position="Position.Right"
      style="height: 10px; width: 3px; background-color: #409eff"
    />
    <Handle
      id="left-output"
      type="source"
      :position="Position.Left"
      style="height: 10px; width: 3px; background-color: #409eff"
    />
    <Handle
      id="bottom-output"
      type="source"
      :position="Position.Bottom"
      style="height: 3px; width: 10px; background-color: #409eff"
    />
    <Handle
      id="top-output"
      type="source"
      :position="Position.Top"
      style="height: 3px; width: 10px; background-color: #409eff"
    />
  </div>
</template>

<style scoped lang="scss">
:deep(.el-card__header) {
  height: 40px;
  padding-top: 10px;
}
:deep(.el-form-item) {
  margin-bottom: 9px;
}

.label-container {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
:deep(.el-card) {
  border: 2px solid transparent !important;
  border-radius: 6px;
  transition: border-color 0.2s ease;
}
:deep(.el-card.is-success) {
  background-color: #67c23a !important;
}
:deep(.el-card.is-error) {
  background-color: #f56c6c !important;
}
:deep(.el-card.active-border) {
  border-color: #409eff !important;
}
</style>
