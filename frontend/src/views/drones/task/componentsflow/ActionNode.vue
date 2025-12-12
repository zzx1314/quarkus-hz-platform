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
import BankCardLine from "~icons/ri/bank-card-line";
import More from "~icons/ep/more-filled";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
const { updateNodeData } = useVueFlow();

const actionForm = ref({
  nodeType: "action",
  action: {
    type: "",
    timeoutSec: null,
    params: {
      timeSec: null,
      targetAlt: null,
      targetWpStr: null,
      targetWp: null,
      num: null,
      intervalSec: null,
      type: "",
      reason: "",
      event: {
        onComplete: `complete_${Math.random().toString(36).slice(2, 10)}`,
        onFail: `fail_${Math.random().toString(36).slice(2, 10)}`
      }
    }
  },
  xpoint: null,
  ypoint: null,
  zpoint: null
});

const rules = reactive<FormRules>({
  "action.type": [
    { required: true, message: "动作类型必填", trigger: "change" }
  ],
  "action.timeoutSec": [
    { required: true, message: "超时时间必填", trigger: "blur" }
  ],
  "action.params.event.onComplete": [
    { required: true, message: "成功事件必填", trigger: "blur" }
  ],
  "action.params.event.onFail": [
    { required: true, message: "失败事件必填", trigger: "blur" }
  ],

  // TAKEOFF 时必须输入高度
  "action.params.targetAlt": [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "TAKEOFF" &&
          (value === null || value === "")
        ) {
          callback(new Error("起飞高度必填"));
        } else callback();
      },
      trigger: "change"
    }
  ],

  // GOTO 时必填（非自定义点）
  "action.params.targetWpStr": [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "GOTO" &&
          !props.isShowCustomPoint &&
          !value
        ) {
          callback(new Error("请选择航点"));
        } else callback();
      },
      trigger: "change"
    }
  ],

  // GOTO + 自定义点 → xpoint, ypoint, zpoint 必填
  xpoint: [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "GOTO" &&
          props.isShowCustomPoint &&
          (value === null || value === "")
        ) {
          callback(new Error("X 坐标必填"));
        } else callback();
      }
    }
  ],
  ypoint: [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "GOTO" &&
          props.isShowCustomPoint &&
          (value === null || value === "")
        ) {
          callback(new Error("Y 坐标必填"));
        } else callback();
      }
    }
  ],
  zpoint: [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "GOTO" &&
          props.isShowCustomPoint &&
          (value === null || value === "")
        ) {
          callback(new Error("Z 坐标必填"));
        } else callback();
      }
    }
  ],

  // PHOTO 校验数量
  "action.params.num": [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "PHOTO" &&
          (value === null || value === "")
        ) {
          callback(new Error("拍照数量必填"));
        } else callback();
      },
      trigger: "change"
    }
  ],

  // PHOTO 校验间隔
  "action.params.intervalSec": [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "PHOTO" &&
          (value === null || value === "")
        ) {
          callback(new Error("拍照间隔必填"));
        } else callback();
      },
      trigger: "change"
    }
  ],

  // HOVER 校验悬停时间
  "action.params.timeSec": [
    {
      validator: (_, value, callback) => {
        if (
          actionForm.value.action.type === "HOVER" &&
          (value === null || value === "")
        ) {
          callback(new Error("悬停时间必填"));
        } else callback();
      },
      trigger: "change"
    }
  ]
});

const isActive = ref(false);

// const isShowCustomPoint = ref(false);

const pathStringFromTask = ref([]);

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
  pathInfo: {
    type: Array,
    default: () => []
  },
  status: {
    type: String,
    default: "default"
  },
  isShowCustomPoint: {
    type: Boolean,
    default: false
  },
  getNodeDataById: Function,
  getNodeInfoById: Function,
  upstreamId: {
    type: String,
    default: ""
  }
});

const isSuccess = props.status === "success";
const isError = props.status === "error";
console.log("upstreamId", props.upstreamId);

// 回显信息
const originalData = computed(() => props.getNodeDataById?.(props.id));
console.log("originalData", originalData);
if (originalData.value) {
  actionForm.value = originalData.value;
}

const originalRoute = props.pathInfo;
console.log("originalRoute", originalRoute);
if (originalRoute) {
  pathStringFromTask.value = originalRoute;
}

const toggleActive = () => {
  emit("select", props.id);
};

const emit = defineEmits(["validate-fail", "select", "show-error-info"]);

const validate = async () => {
  return await submitForm(actionFormRef.value);
};

defineExpose({
  validate
});

const handleChange = value => {
  // Handle service type changes
  if (value === "SERVICE_START_YOLO" || value === "SERVICE_STOP_YOLO") {
    actionForm.value.action.params.type = "YOLO";
  } else if (
    value === "SERVICE_START_DEEPSORT" ||
    value === "SERVICE_STOP_DEEPSORT"
  ) {
    actionForm.value.action.params.type = "DEEPSORT";
  } else if (value === "SERVICE_START_RTSP" || value === "SERVICE_STOP_RTSP") {
    actionForm.value.action.params.type = "RTSP";
  }

  updateNodeData(props.id, actionForm.value);
};

const handleChangeGoto = value => {
  console.log(value);
  actionForm.value.action.params.targetWp = {
    wpId: value,
    alt: 0.5
  };
  console.log("action:" + JSON.stringify(actionForm.value));
  updateNodeData(props.id, actionForm.value);
};

const handleChangePoint = value => {
  console.log(value);
  actionForm.value.action.params.targetWp = {
    wpId: 0,
    lat: actionForm.value.xpoint ? Number(actionForm.value.xpoint) : 0,
    lon: actionForm.value.ypoint ? Number(actionForm.value.ypoint) : 0,
    alt: actionForm.value.zpoint ? Number(actionForm.value.zpoint) : 0.5
  };
  console.log("action:" + JSON.stringify(actionForm.value));
  updateNodeData(props.id, actionForm.value);
};

const showErrorInfo = () => {
  emit("show-error-info");
};

const actionListOptions = ref([
  {
    label: "起飞动作",
    value: "TAKEOFF"
  },
  {
    label: "悬停动作",
    value: "HOVER"
  },
  {
    label: "航点动作",
    value: "GOTO"
  },
  {
    label: "拍照动作",
    value: "PHOTO"
  },
  {
    label: "启动YOLO",
    value: "SERVICE_START_YOLO"
  },
  {
    label: "启动DEEPSORT",
    value: "SERVICE_START_DEEPSORT"
  },
  {
    label: "启动RTSP",
    value: "SERVICE_START_RTSP"
  },
  {
    label: "停止YOLO",
    value: "SERVICE_STOP_YOLO"
  },
  {
    label: "停止DEEPSORT",
    value: "SERVICE_STOP_DEEPSORT"
  },
  {
    label: "停止RTSP",
    value: "SERVICE_STOP_RTSP"
  },
  {
    label: "降落动作",
    value: "LAND"
  }
]);

const actionFormRef = ref<FormInstance>();

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
  },
  { immediate: true }
);

watch(
  () => props.pathInfo,
  newVal => {
    console.log("pathInfo 有变化:", newVal);
    pathStringFromTask.value = newVal;
  },
  { deep: true, immediate: true }
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
            <div class="left">
              <bank-card-line style="font-size: 25px; color: #0000cc" />
              动作信息
            </div>
            <el-button
              link
              type="primary"
              :icon="useRenderIcon(More)"
              @click="showErrorInfo"
            />
          </span>
        </div>
      </template>
      <el-form
        ref="actionFormRef"
        :model="actionForm"
        label-width="auto"
        style="max-width: 600px"
        :rules="rules"
      >
        <el-form-item label="动作类型" prop="action.type">
          <el-select
            v-model="actionForm.action.type"
            placeholder="请选择动作类型"
            style="width: 200px"
            @change="handleChange"
          >
            <el-option
              v-for="item in actionListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="超时时间" prop="action.timeoutSec">
          <el-input-number
            v-model="actionForm.action.timeoutSec"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChange"
          />
        </el-form-item>
        <!-- TAKEOFF -->
        <el-form-item
          v-if="actionForm.action && actionForm.action.type === 'TAKEOFF'"
          label="起飞高度"
          prop="action.params.targetAlt"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.action.params.targetAlt"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChange"
          />
        </el-form-item>
        <!-- GOTO -->
        <el-form-item
          v-if="
            actionForm.action &&
            actionForm.action.type === 'GOTO' &&
            !props.isShowCustomPoint
          "
          label="航点动作"
          :required="true"
          prop="action.params.targetWpStr"
        >
          <el-select
            v-model="actionForm.action.params.targetWpStr"
            @change="handleChangeGoto"
          >
            <el-option
              v-for="(item, index) in pathStringFromTask"
              :key="index"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="
            actionForm.action &&
            actionForm.action.type === 'GOTO' &&
            props.isShowCustomPoint
          "
          prop="xpoint"
          label="目标点X坐标"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.xpoint"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChangePoint"
          />
        </el-form-item>
        <el-form-item
          v-if="
            actionForm.action &&
            actionForm.action.type === 'GOTO' &&
            props.isShowCustomPoint
          "
          prop="ypoint"
          label="目标点Y坐标"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.ypoint"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChangePoint"
          />
        </el-form-item>
        <el-form-item
          v-if="
            actionForm.action &&
            actionForm.action.type === 'GOTO' &&
            props.isShowCustomPoint
          "
          prop="zpoint"
          label="目标点Z坐标"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.zpoint"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChangePoint"
          />
        </el-form-item>
        <!-- PHOTO -->
        <el-form-item
          v-if="actionForm.action && actionForm.action.type === 'PHOTO'"
          label="拍照数量"
          prop="action.params.num"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.action.params.num"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChange"
          />
        </el-form-item>
        <el-form-item
          v-if="actionForm.action && actionForm.action.type === 'PHOTO'"
          label="拍照间隔"
          prop="action.params.intervalSec"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.action.params.intervalSec"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChange"
          />
        </el-form-item>
        <!-- HOVER -->
        <el-form-item
          v-if="actionForm.action && actionForm.action.type === 'HOVER'"
          label="悬停时间"
          prop="action.params.timeSec"
          :required="true"
        >
          <el-input-number
            v-model="actionForm.action.params.timeSec"
            style="width: 200px"
            :min="0"
            :max="999999"
            @change="handleChange"
          />
        </el-form-item>
        <!-- Events -->
        <!-- <el-form-item label="成功事件" prop="action.params.event.onComplete">
          <el-input
            v-model="actionForm.action.params.event.onComplete"
            style="width: 200px"
            @change="handleChange"
          />
        </el-form-item>
        <el-form-item label="失败事件" prop="action.params.event.onFail">
          <el-input
            v-model="actionForm.action.params.event.onFail"
            style="width: 200px"
            @change="handleChange"
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
.left {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
:deep(.el-card__header) {
  height: 40px;
  padding-top: 10px;
}
.label-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
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
:deep(.el-form-item) {
  margin-bottom: 9px;
}
</style>
