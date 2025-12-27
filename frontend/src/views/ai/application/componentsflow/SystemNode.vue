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
import CodiconRobot from "~icons/codicon/robot";
import type { FormInstance, FormRules } from "element-plus";
const { updateNodeData } = useVueFlow();
const systemForm = ref({
  toolName: null
});

const rules = reactive<FormRules>({
  toolName: [{ required: true, message: "工具必须选择", trigger: "change" }]
});

const isActive = ref(false);
const systemFormRef = ref<FormInstance>();

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
  getNodeDataById: Function
});

const originalData = computed(() => props.getNodeDataById?.(props.id));
console.log("originalData", originalData);
if (originalData.value) {
  systemForm.value = originalData.value;
}

const toggleActive = () => {
  emit("select", props.id);
};

const emit = defineEmits(["validate-fail", "select"]);

const validate = async () => {
  return await submitForm(systemFormRef.value);
};

defineExpose({
  validate
});

const handleChange = value => {
  console.log(value);
  updateNodeData(props.id, systemForm.value);
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
      :class="{ 'active-border': isActive }"
    >
      <template #header>
        <div class="card-header">
          <span class="label-container">
            <codicon-robot style="font-size: 25px; color: #4caf50" />
            系统配置
          </span>
        </div>
      </template>
      <el-form
        ref="systemFormRef"
        :model="systemForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
      >
        <el-form-item label="系统工具" prop="toolName">
          <el-select
            v-model="systemForm.toolName"
            placeholder="请选择系统工具"
            @change="handleChange"
            style="width: 200px"
          >
            <el-option label="文档解析" value="文档解析" />
          </el-select>
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
:deep(.el-card.active-border) {
  border-color: #409eff !important;
}
</style>
