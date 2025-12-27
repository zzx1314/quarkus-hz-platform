<script setup lang="ts">
import { Handle, Position, useVueFlow } from "@vue-flow/core";
import {
  computed,
  inject,
  nextTick,
  onBeforeUnmount,
  onMounted,
  reactive,
  ref,
  watch
} from "vue";
import { SUCCESS } from "@/api/base";
import { aiMcpSelect } from "@/api/aiMcp";
import type { FormInstance, FormRules } from "element-plus";
const { updateNodeData } = useVueFlow();
import VaadinTools from "~icons/vaadin/tools";
import {
  aiMcpToolsAllSelectOption,
  aiMcpToolsFindById
} from "@/api/aiMcpTools";
const mcpForm = ref({
  mcpId: null,
  toolId: null,
  arguments: null
});

const mcpListOptions = ref([]);
const mcpToolListOptions = ref([]);
const mcpFormRef = ref<FormInstance>();
const schema = ref([]);
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
  getNodeDataById: Function
});

const originalData = computed(() => props.getNodeDataById?.(props.id));
console.log("originalData", originalData);
if (originalData.value) {
  mcpForm.value = originalData.value;
  if (mcpForm.value.mcpId) {
    aiMcpToolsAllSelectOption(mcpForm.value.mcpId).then(res => {
      if (res.code === SUCCESS) {
        mcpToolListOptions.value = res.data;
      }
    });
  }
  if (mcpForm.value.toolId) {
    getToolParameters(mcpForm.value.toolId);
  }
}

const toggleActive = () => {
  emit("select", props.id);
};

const validate = async () => {
  return await submitForm(mcpFormRef.value);
};

defineExpose({
  validate
});

const emit = defineEmits(["validate-fail", "select"]);

const rules = reactive<FormRules>({
  mcpId: [{ required: true, message: "mcp必填", trigger: "change" }],
  toolId: [{ required: true, message: "mcp工具必填", trigger: "change" }]
});

const handleChange = value => {
  console.log("选项切换", value);
  updateNodeData(props.id, mcpForm.value);
  aiMcpToolsAllSelectOption(value).then(res => {
    if (res.code === SUCCESS) {
      mcpToolListOptions.value = res.data;
    }
  });
};

const handleChangeTool = value => {
  console.log("选项切换", value);
  updateNodeData(props.id, mcpForm.value);
  getToolParameters(value);
};

function getToolParameters(value) {
  validateOnRuleChange.value = false;
  aiMcpToolsFindById(value).then(res => {
    if (res.code === SUCCESS) {
      schema.value = res.data.parametersArray;
      schema.value.forEach(field => {
        mcpForm[field.property] = field.type === "boolean" ? false : "";
        if (field.required) {
          rules[field.property] = [
            {
              required: true,
              message: `请输入${field.property}`,
              trigger: "blur"
            }
          ];
        }
      });
      nextTick(() => {
        mcpFormRef.value?.clearValidate();
        // 延迟恢复校验开启
        setTimeout(() => {
          validateOnRuleChange.value = true;
        }, 100);
      });
    }
  });
}

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
  aiMcpSelect().then(res => {
    if (res.code === SUCCESS) {
      mcpListOptions.value = res.data;
    }
  });
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
            <vaadin-tools style="font-size: 25px; color: #009966" />
            MCP配置
          </span>
        </div>
      </template>
      <el-form
        ref="mcpFormRef"
        :model="mcpForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
        :validate-on-rule-change="validateOnRuleChange"
      >
        <el-form-item label="MCP名称" prop="mcpId">
          <el-select
            v-model="mcpForm.mcpId"
            placeholder="请选择MCP名称"
            @change="handleChange"
          >
            <el-option
              v-for="item in mcpListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工具名称" prop="toolId">
          <el-select
            v-model="mcpForm.toolId"
            placeholder="请选择工具名称"
            style="width: 200px"
            @change="handleChangeTool"
          >
            <el-option
              v-for="item in mcpToolListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-for="field in schema"
          :key="field.property"
          :label="field.property"
          :prop="field.property"
        >
          <!-- 输入框类型选择 -->
          <el-input
            v-if="field.type === 'string'"
            v-model="mcpForm[field.property]"
            @blur="updateNodeData(props.id, mcpForm)"
          />
          <el-input
            v-else-if="field.type === 'number'"
            v-model.number="mcpForm[field.property]"
            type="number"
            @blur="updateNodeData(props.id, mcpForm)"
          />
          <el-switch
            v-else-if="field.type === 'boolean'"
            v-model="mcpForm[field.property]"
            @change="updateNodeData(props.id, mcpForm)"
          />
          <!-- 可扩展更多类型 -->
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
