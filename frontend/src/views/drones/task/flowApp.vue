<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, provide } from "vue";
import { VueFlow, useVueFlow, MarkerType } from "@vue-flow/core";
import DropzoneBackground from "./componentsflow/DropzoneBackground.vue";
import Sidebar from "./componentsflow/Sidebar.vue";
import useDragAndDrop from "./componentsflow/useDnD.js";
import StartNode from "@/views/drones/task/componentsflow/StartNode.vue";
import EndNode from "@/views/drones/task/componentsflow/EndNode.vue";
import ActionNode from "@/views/drones/task/componentsflow/ActionNode.vue";
import ErrorNode from "@/views/drones/task/componentsflow/ErrorNode.vue";
import { useProcess } from "@/views/drones/task/flowHook.tsx";
import TaskNode from "@/views/drones/task/componentsflow/TaskNode.vue";
import { useRenderIcon } from "@/components/ReIcon/src/hooks.js";
import Back from "~icons/ep/back";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags.js";
import { router } from "@/store/utils.js";
import { message } from "@/utils/message.js";
import Position from "~icons/ep/position";
import { useAlignmentGuidelines } from "./componentsflow/useAlignmentGuidelines";
import {
  dronesTaskSaveFlow,
  dronesTaskGetFlow,
  droneGetTaskStatus
} from "@/api/dronesTask";
import { SUCCESS } from "@/api/base.js";

import { reactive, watch } from "vue";
import "vue-json-pretty/lib/styles.css";
import VueJsonPretty from "vue-json-pretty";
import Search from "~icons/ep/search";
import WebSocketClient from "@/components/ReWebSocket";

import {
  droneGetCommandJsonString,
  dronesTaskUpdateFlow
} from "@/api/dronesTask";

const { guideline } = useAlignmentGuidelines(5);

const {
  onConnect,
  addEdges,
  removeNodes,
  getSelectedNodes,
  removeEdges,
  getSelectedEdges,
  getEdges,
  getNodes
} = useVueFlow();

const { onDragOver, onDrop, onDragLeave, isDragOver } = useDragAndDrop();

const { initToDetail, getParameter } = useProcess();
initToDetail();

console.log("getParameter", getParameter);

const nodes = ref([]);
const edges = ref([]);
const isSubmit = ref(false);
const selectedNodeId = ref("");
const isPassed = ref(true);
const isShowSave = getParameter.taskType === "taskDesign";
const isSuccessNodeId = ref([]);
const isErrorNodeId = ref([]);
const errorInfoMap = ref({});

const dialogFormVisible = ref(false);
const dialogFormVisibleString = ref(false);
const dialogFormVisibleTreacking = ref(false);
const isShowCustomPoint = ref(false);
const needWebSocket = ref(false);

// 注册区
const nodeRefs = ref({});
provide("registerNodeRef", (id, ref) => {
  nodeRefs.value[id] = ref;
});
provide("unregisterNodeRef", id => {
  delete nodeRefs.value[id];
});

onConnect(params => {
  const sourceNode = getNodeInfoById(params.source);
  const targetNode = getNodeInfoById(params.target);
  const sourceType = sourceNode?.nodeType;
  const targetType = targetNode?.nodeType;

  // 如果任意一端是 action 节点，则使用虚线
  const isActionEdge = sourceType === "action" || targetType === "action";
  let edgeStyle;

  if (isActionEdge) {
    // 虚线（任务 -> 动作、动作 -> 动作、start/end -> action）
    edgeStyle = {
      stroke: "#409EFF",
      strokeWidth: 2,
      strokeDasharray: "5,5"
    };
  } else {
    // 实线（start, end, task 之间全部实线）
    edgeStyle = {
      stroke: "#409EFF",
      strokeWidth: 2
    };
  }
  addEdges([
    {
      ...params,
      style: edgeStyle,
      animated: false,
      markerEnd: {
        type: MarkerType.ArrowClosed,
        color: "#ff00a6"
      }
    }
  ]);
});

function handleKeydown(event) {
  if (event.key === "Delete") {
    const selectedNodes = getSelectedNodes;
    const selectedEdges = getSelectedEdges;
    console.log("selectedNodes", selectedNodes.value);
    if (selectedNodes.value.length) removeNodes(selectedNodes.value);
    if (selectedEdges.value.length) removeEdges(selectedEdges.value);
  }
}

const rawNodes = ref("");

function getFlowData() {
  if (getParameter.taskId) {
    dronesTaskGetFlow(getParameter.taskId).then(res => {
      if (res.code === SUCCESS && res.data) {
        console.log("res.data", res.data);
        const nodeData = res.data.nodes;
        const edgeData = res.data.edges;
        nodes.value = JSON.parse(nodeData);
        edges.value = JSON.parse(edgeData);
        rawNodes.value = JSON.parse(JSON.stringify(nodes.value));
        // 判断是否有目标跟踪节点
        console.log("nodesData", rawNodes.value);
        needWebSocket.value = rawNodes.value.some(
          node =>
            node.type === "action" &&
            node.data?.action?.type === "SERVICE_START_DEEPSORT"
        );
        startWebSocket();
        nextTick(() => {
          pathInfo.value = nodeRoute();
          console.log("初始化 pathInfo:", pathInfo.value);
        });
      }
    });
  }
}

function getNodeDataById(id) {
  if (rawNodes.value) {
    return rawNodes.value.find(n => n.id === id)?.data;
  }
}

function getNodeInfoById(id) {
  const node = getNodes.value.find(n => n.id === id);
  return node?.data;
}

const nodeRoute = () => {
  for (const node of getNodes.value) {
    if (node.type === "task") {
      const taskData = node.data;
      if (taskData.routePointListOptions) {
        return taskData.routePointListOptions;
      }
    }
  }
  return [];
};

const pathInfo = ref(nodeRoute());

function updateRoute(newRoutePointListOptions) {
  pathInfo.value = newRoutePointListOptions;
  console.log("保存路径:", pathInfo.value);
}

// 保存流程
async function saveActivity() {
  isPassed.value = true;

  for (const id in nodeRefs.value) {
    const node = nodeRefs.value[id];
    if (node?.validate && !(await node.validate())) {
      isPassed.value = false;
    }
  }

  if (!isPassed.value) {
    message("请完善节点配置后再保存", { type: "error" });
    return;
  }
  const params = {
    taskId: getParameter.taskId,
    nodes: JSON.stringify(getNodes.value),
    edges: JSON.stringify(getEdges.value),
    uuid: getParameter.workflowId
  };
  if (getNodes.value.length === 0 && getEdges.value.length === 0) {
    message("请先拖坠流程节点到画布", { type: "error" });
  } else {
    isSubmit.value = !isSubmit.value;
    if (isPassed.value) {
      dronesTaskSaveFlow(params).then(res => {
        if (res.code === SUCCESS) {
          message("保存成功", { type: "success" });
        }
      });
    }
  }
}

function getTaskStatus() {
  droneGetTaskStatus(getParameter.taskId).then(res => {
    if (res.code === SUCCESS) {
      isSuccessNodeId.value = res.data.successIds;
      isErrorNodeId.value = res.data.failIds;
      errorInfoMap.value = res.data.errorInfoMap;
    }
  });
}

onMounted(() => {
  nextTick(() => {
    window.addEventListener("keydown", handleKeydown);
    getFlowData();
    getTaskStatus();
    if (getParameter.isShowSave) {
      isShowSave.value = getParameter.isShowSave;
    }
  });
});

onBeforeUnmount(() => {
  window.removeEventListener("keydown", handleKeydown);

  if (webSocketImag.value) {
    webSocketImag.value.disconnect?.();
    webSocketImag.value = null;
  }
});

const back = () => {
  console.log("back");
  useMultiTagsStoreHook().handleTags("splice", "/drones/task/flowApp");
  router.push({ path: "/drones/task/index" });
};

const handleSelect = id => {
  selectedNodeId.value = id;
};

const handlePaneClick = () => {
  selectedNodeId.value = "";
};

const handleShowCustomPoint = param => {
  isShowCustomPoint.value = param;
};

const nodeStatus = id => {
  return isSuccessNodeId.value?.includes(id)
    ? "success"
    : isErrorNodeId.value?.includes(id)
      ? "error"
      : "default";
};

const cancel = () => {
  dialogFormVisible.value = false;
  dialogFormVisibleString.value = false;
  state.data = {};
};

const errorInfoMsg = ref("");
const showErrorInfo = actionId => {
  console.log("showErrorInfo", actionId);
  if (errorInfoMap.value && errorInfoMap.value[actionId]) {
    errorInfoMsg.value = errorInfoMap.value[actionId];
  } else {
    errorInfoMsg.value = "未找到对应错误信息";
  }
  dialogFormVisible.value = true;
};

const showJsonString = () => {
  console.log("showJsonString");
  droneGetCommandJsonString(getParameter.taskId).then(res => {
    if (res.code === SUCCESS) {
      try {
        const jsonData = JSON.parse(res.data);
        state.data = jsonData;
        state.val = JSON.stringify(jsonData);
        dialogFormVisibleString.value = true;
      } catch (error) {
        console.error("Failed to parse JSON:", error);
      }
    } else {
      message("获取指令集JSON字符串失败", { type: "error" });
    }
  });
};

const updateCommandJsonString = () => {
  // 此处可添加逻辑以处理更新后的 JSON 数据
  console.log("Updated Command JSON Data:", state.val);
  const param = {
    taskId: getParameter.taskId,
    commandJsonString: state.val
  };
  dronesTaskUpdateFlow(param).then(res => {
    if (res.code === SUCCESS) {
      message("指令集JSON字符串更新成功", { type: "success" });
    } else {
      message("指令集JSON字符串更新失败", { type: "error" });
    }
  });
};

const videoImg = ref(null);
const webSocketImag = ref(null);
let lastObjectUrl = null;

function startWebSocket() {
  if (!needWebSocket.value) {
    return;
  }
  if (webSocketImag.value) return;
  webSocketImag.value = new WebSocketClient();

  webSocketImag.value.connect({
    path: "/api/ws/track",
    onConnect: () => {
      console.log("连接成功");
    },
    onError: function (error) {
      console.log("连接失败", error);
    },
    onClose: function () {
      console.log("连接关闭");
    },
    onData: function (data) {
      // 数据处理
      const blob = new Blob([data], { type: "image/jpeg" });
      const url = URL.createObjectURL(blob);

      if (videoImg.value) {
        videoImg.value.onload = () => {
          if (lastObjectUrl) {
            URL.revokeObjectURL(lastObjectUrl);
          }
          lastObjectUrl = url;
        };
        videoImg.value.src = url;
      }
    }
  });
}

const defaultData = {};

const state = reactive({
  val: JSON.stringify(defaultData),
  data: defaultData,
  showLine: true,
  showLineNumber: true,
  showDoubleQuotes: true,
  showLength: true,
  editable: true,
  showIcon: true,
  editableTrigger: "click",
  deep: 3
});

watch(
  () => state.data,
  (newVal, oldVal) => {
    try {
      if (oldVal && Object.keys(oldVal).length === 0) return;
      if (newVal && Object.keys(newVal).length === 0) return;
      state.val = JSON.stringify(newVal);
      updateCommandJsonString();
    } catch (err) {
      // console.log('JSON ERROR');
    }
  }
);
</script>

<template>
  <div class="dnd-flow" @drop="onDrop">
    <div class="menuClass">
      <el-button type="primary" link :icon="useRenderIcon(Back)" @click="back()"
        >返回</el-button
      >
      <el-button
        v-if="isShowSave"
        :icon="useRenderIcon(Position)"
        type="primary"
        size="small"
        @click="saveActivity"
        >保存</el-button
      >
      <el-button
        v-if="isShowSave"
        :icon="useRenderIcon(Search)"
        type="success"
        size="small"
        @click="showJsonString"
        >查看Json字符串</el-button
      >
    </div>
    <Sidebar v-if="isShowSave" />
    <VueFlow
      :nodes="nodes"
      :edges="edges"
      @dragover="onDragOver"
      @dragleave="onDragLeave"
      @pane-click="handlePaneClick"
    >
      <div
        v-if="guideline.x !== null"
        class="align-line vertical"
        :style="{ left: guideline.x + 'px' }"
      />
      <div
        v-if="guideline.y !== null"
        class="align-line horizontal"
        :style="{ top: guideline.y + 'px' }"
      />
      <template #node-start>
        <StartNode />
      </template>
      <template #node-end>
        <EndNode />
      </template>
      <template #node-action="props">
        <ActionNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :status="nodeStatus(props.id)"
          :get-node-data-by-id="getNodeDataById"
          :get-node-info-by-id="getNodeInfoById"
          :pathInfo="pathInfo"
          :isShowCustomPoint="isShowCustomPoint"
          @select="handleSelect"
          @show-error-info="showErrorInfo(props.id)"
        />
      </template>
      <template #node-error="props">
        <ErrorNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :status="nodeStatus(props.id)"
          :get-node-data-by-id="getNodeDataById"
          @select="handleSelect"
        />
      </template>
      <template #node-task="props">
        <TaskNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :status="nodeStatus(props.id)"
          :get-node-data-by-id="getNodeDataById"
          @select="handleSelect"
          @show-custom-point="handleShowCustomPoint"
          @update-route="updateRoute"
        />
      </template>
      <DropzoneBackground
        :style="{
          backgroundColor: isDragOver ? '#e7f3ff' : 'transparent',
          transition: 'background-color 0.2s ease'
        }"
      >
        <p v-if="isDragOver">推拽到画布</p>
      </DropzoneBackground>
    </VueFlow>

    <el-dialog
      v-model="dialogFormVisible"
      title="错误信息"
      width="500px"
      @close="cancel"
    >
      <div>
        {{ errorInfoMsg }}
      </div>
    </el-dialog>

    <el-dialog
      v-model="dialogFormVisibleTreacking"
      title="目标跟踪"
      width="500px"
      @close="cancel"
    >
      <div>
        <img ref="videoImg" width="500" height="300" />
      </div>
    </el-dialog>

    <el-dialog
      v-model="dialogFormVisibleString"
      title="下发命令字符串"
      width="500px"
      @close="cancel"
    >
      <vue-json-pretty
        v-model:data="state.data"
        :deep="state.deep"
        :show-double-quotes="state.showDoubleQuotes"
        :show-line="state.showLine"
        :show-length="state.showLength"
        :show-icon="state.showIcon"
        :show-line-number="state.showLineNumber"
        :editable="state.editable"
        :editable-trigger="state.editableTrigger"
      />
    </el-dialog>
  </div>
</template>

<style lang="scss">
@import "@vue-flow/core/dist/style.css";
@import "@vue-flow/core/dist/theme-default.css";
@import "@vue-flow/controls/dist/style.css";
@import "@vue-flow/minimap/dist/style.css";
@import "@vue-flow/node-resizer/dist/style.css";

.menuClass {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 10;
}
.vue-flow__minimap {
  transform: scale(75%);
  transform-origin: bottom right;
}

.dnd-flow {
  position: relative;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  flex-direction: column;
  display: flex;
  height: 100%;
}

.dnd-flow aside {
  color: #fff;
  font-weight: 700;
  padding: 15px 10px;
  margin-top: 44px;
  font-size: 12px;
  background: #10b981bf;
  -webkit-box-shadow: 0px 5px 10px 0px rgba(0, 0, 0, 0.3);
  box-shadow: 0 5px 10px #0000004d;
}

.dnd-flow aside .description {
  margin-bottom: 10px;
}

.dnd-flow .vue-flow-wrapper {
  flex-grow: 1;
  height: 100%;
}

@media screen and (min-width: 200px) {
  .dnd-flow {
    flex-direction: row;
  }
  .dnd-flow aside {
    min-width: 8%;
  }
}

@media screen and (max-width: 639px) {
  .dnd-flow aside .nodes {
    display: flex;
    flex-direction: row;
    gap: 5px;
  }
}

.dropzone-background {
  position: relative;
  height: 100%;
  width: 100%;
}

.dropzone-background .overlay {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  pointer-events: none;
}
.align-line {
  position: absolute;
  background-color: #ff4d4f;
  z-index: 1000;
  pointer-events: none;
}
.vertical {
  top: 0;
  bottom: 0;
  width: 1px;
}
.horizontal {
  left: 0;
  right: 0;
  height: 1px;
}
</style>
