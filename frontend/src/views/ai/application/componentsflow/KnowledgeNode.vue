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
import { SUCCESS } from "@/api/base";
import { aiKnowledgeBaseAllSelectOption } from "@/api/aiKnowledgeBase";
import CarbonIbmWatsonKnowledgeStudio from "~icons/carbon/ibm-watson-knowledge-studio";
import type { FormInstance, FormRules } from "element-plus";
import { aiDocumentGetByKbId } from "@/api/aiDocument";
const { updateNodeData } = useVueFlow();
const knowledgeForm = ref({
  knowledgeId: null,
  docId: null,
  maxResult: null,
  minScore: null
});

const rules = reactive<FormRules>({
  knowledgeId: [{ required: true, message: "知识库必填", trigger: "change" }]
});

const kbListOptions = ref([]);
const docListOptions = ref([]);
const isActive = ref(false);
const kbFormRef = ref<FormInstance>();

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
  knowledgeForm.value = originalData.value;
  if (knowledgeForm.value && knowledgeForm.value.knowledgeId) {
    aiDocumentGetByKbId(knowledgeForm.value.knowledgeId).then(res => {
      if (res.code === SUCCESS) {
        docListOptions.value = res.data;
      }
    });
  }
}

const toggleActive = () => {
  emit("select", props.id);
};

const emit = defineEmits(["validate-fail", "select"]);

const validate = async () => {
  return await submitForm(kbFormRef.value);
};

defineExpose({
  validate
});

const handleChange = value => {
  console.log(value);
  updateNodeData(props.id, knowledgeForm.value);
  aiDocumentGetByKbId(knowledgeForm.value.knowledgeId).then(res => {
    if (res.code === SUCCESS) {
      docListOptions.value = res.data;
    }
  });
};

const handleChangeDoc = value => {
  console.log("选项切换", value);
  updateNodeData(props.id, knowledgeForm.value);
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
  aiKnowledgeBaseAllSelectOption().then(res => {
    if (res.code === SUCCESS) {
      kbListOptions.value = res.data;
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
            <carbon-ibm-watson-knowledge-studio
              style="font-size: 25px; color: #330033"
            />
            知识库配置
          </span>
        </div>
      </template>
      <el-form
        ref="kbFormRef"
        :model="knowledgeForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
      >
        <el-form-item label="知识库名称" prop="knowledgeId">
          <el-select
            v-model="knowledgeForm.knowledgeId"
            placeholder="请选择知识库名称"
            @change="handleChange"
          >
            <el-option
              v-for="item in kbListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文档名称" prop="docId">
          <el-select
            v-model="knowledgeForm.docId"
            placeholder="请选择关联的文档"
            @change="handleChangeDoc"
          >
            <el-option
              v-for="item in docListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="最大条数">
          <el-input
            v-model="knowledgeForm.maxResult"
            style="width: 200px"
            placeholder="请输入最大条数"
          />
        </el-form-item>
        <el-form-item label="最小得分">
          <el-input
            v-model="knowledgeForm.minScore"
            style="width: 200px"
            placeholder="请输入最小得分"
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
