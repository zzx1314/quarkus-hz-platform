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
import { dronesRouteLibraryGetSelectOption } from "@/api/dronesRouteLibrary";
import LinksFill from "~icons/ri/links-fill";
import type { FormInstance, FormRules } from "element-plus";
const { updateNodeData } = useVueFlow();
const routeForm = ref({
  routeId: null,
  type: "航线"
});

const rules = reactive<FormRules>({
  routeId: [{ required: true, message: "航线必填", trigger: "change" }]
});

const routeListOptions = ref([]);
const isActive = ref(false);
const routeFormRef = ref<FormInstance>();

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

const toggleActive = () => {
  emit("select", props.id);
};

const emit = defineEmits(["validate-fail", "select"]);

const validate = async () => {
  return await submitForm(routeFormRef.value);
};

defineExpose({
  validate
});

const handleChange = value => {
  console.log(value);
  updateNodeData(props.id, routeForm.value);
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
  dronesRouteLibraryGetSelectOption().then(res => {
    if (res.code === SUCCESS) {
      routeListOptions.value = res.data;
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
            <links-fill style="font-size: 25px; color: #4caf50" />
            航线
          </span>
        </div>
      </template>
      <el-form
        ref="routeFormRef"
        :model="routeForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
      >
        <el-form-item label="航线名称" prop="routeId">
          <el-select
            v-model="routeForm.routeId"
            placeholder="请选择航线名称"
            style="width: 200px"
            @change="handleChange"
          >
            <el-option
              v-for="item in routeListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
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
