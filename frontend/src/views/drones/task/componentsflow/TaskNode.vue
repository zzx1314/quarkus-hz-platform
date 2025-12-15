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
import SendPlane2Line from "~icons/ri/send-plane-2-line";
import type { FormInstance, FormRules } from "element-plus";
import { dronesDeviceGetSelectOptions } from "@/api/dronesDevice";
import { SUCCESS } from "@/api/base";
import {
  dronesRouteLibraryGetSelectOption,
  dronesRouteLibraryGetSelectOptionPoint
} from "@/api/dronesRouteLibrary";
const { updateNodeData } = useVueFlow();

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

const rules = reactive<FormRules>({
  deviceId: [{ required: true, message: "设备必须选择", trigger: "change" }],
  routeId: [
    {
      required: true,
      message: "必须选择航线，或选择自定义航线",
      trigger: "change"
    }
  ],
  pathString: [
    { required: true, message: "航线段落必须选择", trigger: "change" }
  ],
  "taskInfo.event": [
    { required: true, message: "任务事件不能为空", trigger: "blur" }
  ]
});

const isSuccess = props.status === "success";
const isError = props.status === "error";
const isActive = ref(false);
const taskFormRef = ref<FormInstance>();
const deviceListOptions = ref([]);
const routeListOptions = ref([]);
const routePointListOptions = ref([]);

const taskForm = ref({
  taskId: props.data.taskId,
  routeId: null,
  deviceId: null,
  nodeType: "task",
  taskInfo: {
    fromWp: {
      wpId: null
    },
    toWp: {
      wpId: null
    },
    event: `init_${Math.random().toString(36).slice(2, 10)}`
  },
  pathString: [],
  routePointListOptions: []
});

const isCustomeRoute = ref(false);

const emit = defineEmits([
  "validate-fail",
  "select",
  "update-route",
  "showCustomPoint"
]);

const originalData = computed(() => props.getNodeDataById?.(props.id));
console.log("originalData", originalData);
if (originalData.value) {
  taskForm.value = originalData.value;
  // 回显下拉框
  if (taskForm.value.routeId && taskForm.value.routeId != 0) {
    dronesRouteLibraryGetSelectOptionPoint(taskForm.value.routeId).then(res => {
      routePointListOptions.value.push(...res.data);
      console.log("routePointListOptions", routePointListOptions.value);
    });
    isCustomeRoute.value = false;
    emit("showCustomPoint", false);
  } else {
    isCustomeRoute.value = true;
    emit("showCustomPoint", true);
  }
}

const toggleActive = () => {
  emit("select", props.id);
};

const validate = async () => {
  return await submitForm(taskFormRef.value);
};

defineExpose({
  validate
});

const handleChangeBase = () => {
  updateNodeData(props.id, { ...taskForm.value });
};

const handleChange = value => {
  console.log(value);
  const firstPoint = taskForm.value.pathString[0];
  taskForm.value.taskInfo.fromWp = {
    wpId: firstPoint
  };
  const endPoint =
    taskForm.value.pathString[taskForm.value.pathString.length - 1];
  taskForm.value.taskInfo.toWp = {
    wpId: endPoint
  };
  taskForm.value.routePointListOptions = routePointListOptions.value;
  console.log("taskForm.value", taskForm.value.routePointListOptions);
  // 更新动作节点的下拉数据
  emit("update-route", routePointListOptions.value);
  updateNodeData(props.id, { ...taskForm.value });
};

const handleChangeRoute = value => {
  console.log("选项切换", value);
  if (value && value !== 0) {
    dronesRouteLibraryGetSelectOptionPoint(value).then(res => {
      if (res.code === SUCCESS) {
        console.log("res.data", res.data);
        routePointListOptions.value.push(...res.data);
        taskForm.value.routePointListOptions = res.data;
        console.log(
          "routePointListOptions.value",
          taskForm.value.routePointListOptions
        );
        updateNodeData(props.id, taskForm.value);
      }
    });
    isCustomeRoute.value = false;
    emit("showCustomPoint", false);
  } else {
    // 选择自定义航线
    isCustomeRoute.value = true;
    emit("showCustomPoint", true);
    updateNodeData(props.id, taskForm.value);
  }
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
  // 获取设备列表
  dronesDeviceGetSelectOptions().then(res => {
    if (res.code === SUCCESS) {
      deviceListOptions.value = res.data;
    }
  });
  // 获取航线列表
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
      style="max-width: 1000px"
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
            <SendPlane2Line style="font-size: 25px; color: #330033" />
            任务节点
          </span>
        </div>
      </template>
      <el-form
        ref="taskFormRef"
        :model="taskForm"
        label-width="auto"
        style="max-width: 650px"
        :rules="rules"
      >
        <el-form-item label="无人机设备" prop="deviceId">
          <el-select
            v-model="taskForm.deviceId"
            clearable
            placeholder="请选择无人机设备"
            style="width: 200px"
            @change="handleChangeBase"
          >
            <el-option
              v-for="item in deviceListOptions"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="全局航线" prop="routeId">
          <el-select
            v-model="taskForm.routeId"
            clearable
            placeholder="请选择无人机航线"
            style="width: 200px"
            @change="handleChangeRoute"
          >
            <el-option label="自定义航线" :value="0" />
            <el-option
              v-for="item in routeListOptions"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!isCustomeRoute" label="航线段落" prop="pathString">
          <el-select
            v-model="taskForm.pathString"
            clearable
            multiple
            placeholder="请选择航线节点"
            style="width: 200px"
            @change="handleChange"
          >
            <el-option
              v-for="item in routePointListOptions"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="任务事件" prop="taskInfo.event">
          <el-input
            v-model="taskForm.taskInfo.event"
            style="width: 200px"
            placeholder="请输入任务事件"
          />
        </el-form-item> -->
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
:deep(.el-card.active-border) {
  border-color: #409eff !important;
}
:deep(.el-card.is-success) {
  background-color: #67c23a !important;
}
:deep(.el-card.is-error) {
  background-color: #f56c6c !important;
}
.card {
  border: 1px solid #e5e7eb; /* 边框颜色和粗细 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.12); /* x y 模糊 颜色 */
  padding: 10px;
  background: #fff;
  border-radius: 6px; /* 可选：圆角 */
  margin-bottom: 10px;
  width: 1000px;
}

.title-underline {
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #e5e7eb;
  padding-bottom: 6px;
  margin-bottom: 10px;
}

.action-container {
  display: flex;
  gap: 16px; /* 卡片间距 */
  flex-wrap: wrap;
}
.card-full {
  flex-basis: 100%;
}
</style>
