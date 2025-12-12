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
import { aiModelAllSelectOption } from "@/api/aiModel";
import { SUCCESS } from "@/api/base";
import type { FormInstance, FormRules } from "element-plus";
import QuestionFill from "~icons/ep/question-filled";
import CarbonFlowModelerReference from "~icons/carbon/flow-modeler-reference";

const { updateNodeData } = useVueFlow();

interface ModelFormType {
  modelId: any;
  temperature: number;
  maxTokens: number;
  frequencyPenalty: number;
}

const modelForm = ref({
  modelId: null,
  temperature: 0.2,
  maxTokens: 1024,
  frequencyPenalty: 0.0,
  command: ""
});

const rules = reactive<FormRules>({
  modelId: [{ required: true, message: "模型必填", trigger: "change" }]
});
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
  modelForm.value = originalData.value;
}

const toggleActive = () => {
  emit("select", props.id);
};

const emit = defineEmits(["validate-fail", "select"]);

const validate = async () => {
  return await submitForm(modelFormRef.value);
};

defineExpose({
  validate
});

const handleChange = value => {
  console.log("选项切换", modelForm.value);
  updateNodeData(props.id, modelForm.value);
};

const modeListOptions = ref([]);

const modelFormRef = ref<FormInstance>();

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
  aiModelAllSelectOption().then(res => {
    if (res.code === SUCCESS) {
      modeListOptions.value = res.data;
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
            <carbon-flow-modeler-reference
              style="font-size: 25px; color: #0000cc"
            />
            模型配置
          </span>
        </div>
      </template>
      <el-form
        ref="modelFormRef"
        :model="modelForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
      >
        <el-form-item label="模型名称" prop="modelId">
          <el-select
            v-model="modelForm.modelId"
            placeholder="请选择模型名称"
            @change="handleChange"
          >
            <el-option
              v-for="item in modeListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="temperature">
          <template v-slot:label>
            <span class="label-container">
              温度
              <el-tooltip
                content="采样温度，介于 0 和 2 之间。较高的值（如 0.8）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定"
                placement="top"
              >
                <question-fill />
              </el-tooltip>
            </span>
          </template>
          <el-input-number
            v-model="modelForm.temperature"
            :precision="1"
            :step="0.1"
            :max="2"
            :min="0"
          />
        </el-form-item>
        <el-form-item prop="maxTokens">
          <template v-slot:label>
            <span class="label-container">
              最大Token数
              <el-tooltip
                content="聊天完成时可生成的最大令牌数"
                placement="top"
              >
                <question-fill />
              </el-tooltip>
            </span>
          </template>
          <el-input-number v-model="modelForm.maxTokens" :max="2048" :min="0" />
        </el-form-item>
        <el-form-item label="惩罚频率" prop="frequencyPenalty">
          <template v-slot:label>
            <span class="label-container">
              惩罚频率
              <el-tooltip placement="top">
                <template #content>
                  <div>
                    介于 -2.0 和 2.0 之间的数字。<br />
                    正值会根据新标记在文本中出现的频率对其进行惩罚，<br />
                    从而降低模型逐字重复同一行内容的可能性。<br />
                  </div>
                </template>
                <question-fill />
              </el-tooltip>
            </span>
          </template>
          <el-input-number
            v-model="modelForm.frequencyPenalty"
            :min="-2"
            :max="2"
            :step="0.1"
          />
        </el-form-item>
        <el-form-item label="指令信息" prop="command">
          <template v-slot:label>
            <span class="label-container">
              指令信息
              <el-tooltip placement="top">
                <template #content>
                  <div>给模型输入指令，例如对上一节点做数据处理</div>
                </template>
                <question-fill />
              </el-tooltip>
            </span>
          </template>
          <el-input
            @blur="handleChange"
            v-model="modelForm.command"
            type="textarea"
            placeholder="请输入指令"
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
