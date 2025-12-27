<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, provide } from "vue";
import { VueFlow, useVueFlow, MarkerType } from "@vue-flow/core";
import DropzoneBackground from "./componentsflow/DropzoneBackground.vue";
import Sidebar from "./componentsflow/Sidebar.vue";
import useDragAndDrop from "./componentsflow/useDnD.js";
import StartNode from "@/views/ai/application/componentsflow/StartNode.vue";
import EndNode from "@/views/ai/application/componentsflow/EndNode.vue";
import ModelNode from "@/views/ai/application/componentsflow/ModelNode.vue";
import MCPNode from "@/views/ai/application/componentsflow/MCPNode.vue";
import KnowledgeNode from "@/views/ai/application/componentsflow/KnowledgeNode.vue";
import { useProcess } from "@/views/ai/application/hooks/processHook.js";
import { useRenderIcon } from "@/components/ReIcon/src/hooks.js";
import Back from "~icons/ep/back";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags.js";
import { router } from "@/store/utils.js";
import { message } from "@/utils/message.js";
import Position from "~icons/ep/position";
import { useAlignmentGuidelines } from "./componentsflow/useAlignmentGuidelines";
import { aiApplicationSave, aiApplicationUpdate } from "@/api/aiApplication.js";
import { SUCCESS } from "@/api/base.js";
import { aiProcessGetById } from "@/api/aiProcess.js";
import SystemNode from "@/views/ai/application/componentsflow/SystemNode.vue";
const { guideline } = useAlignmentGuidelines(5); // 容差为5px

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
const isShowSave = ref("true");

// 注册区
const nodeRefs = ref({});
provide("registerNodeRef", (id, ref) => {
  nodeRefs.value[id] = ref;
});
provide("unregisterNodeRef", id => {
  delete nodeRefs.value[id];
});

onConnect(params => {
  addEdges([
    {
      ...params,
      style: {
        stroke: "#67C23A",
        strokeWidth: 1.5
      },
      animated: true,
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
  if (getParameter.appId) {
    aiProcessGetById(getParameter.appId).then(res => {
      if (res.code === SUCCESS) {
        console.log("res.data", res.data);
        const nodeData = res.data.nodes;
        const edgeData = res.data.edges;
        nodes.value = JSON.parse(nodeData);
        edges.value = JSON.parse(edgeData);
        rawNodes.value = JSON.parse(JSON.stringify(nodes.value));
      }
    });
  }
}

function getNodeDataById(id) {
  if (rawNodes.value) {
    return rawNodes.value.find(n => n.id === id)?.data;
  }
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
    id: getParameter.appId,
    name: getParameter.name,
    description: getParameter.description,
    roleIdList: getParameter.roleIdList.split(","),
    type: "复杂应用",
    nodes: JSON.stringify(getNodes.value),
    edges: JSON.stringify(getEdges.value)
  };
  if (getNodes.value.length === 0 && getEdges.value.length === 0) {
    message("请先拖坠流程节点到画布", { type: "error" });
  } else {
    isSubmit.value = !isSubmit.value;
    if (isPassed.value) {
      console.log("通过了校验，params", params);
      if (getParameter.appId) {
        // 修改
        aiApplicationUpdate(params).then(res => {
          if (res.code === SUCCESS) {
            message("修改成功", { type: "success" });
          }
        });
      } else {
        aiApplicationSave(params).then(res => {
          if (res.code === SUCCESS) {
            message("保存成功", { type: "success" });
            // todo: 跳转到应用页面
          }
        });
      }
    }
  }
}

onMounted(() => {
  nextTick(() => {
    window.addEventListener("keydown", handleKeydown);
    getFlowData();
    if (getParameter.isShowSave) {
      isShowSave.value = getParameter.isShowSave;
    }
  });
});

onBeforeUnmount(() => {
  window.removeEventListener("keydown", handleKeydown);
});

const back = () => {
  console.log("back");
  useMultiTagsStoreHook().handleTags("splice", "/ai/application/flowApp");
  router.push({ path: "/ai/application/index" });
};

const handleSelect = id => {
  selectedNodeId.value = id;
};

const handlePaneClick = () => {
  selectedNodeId.value = "";
};
</script>

<template>
  <div class="dnd-flow" @drop="onDrop">
    <div class="menuClass">
      <el-button type="primary" link :icon="useRenderIcon(Back)" @click="back()"
        >返回</el-button
      >
      <el-button
        v-if="isShowSave === 'true'"
        :icon="useRenderIcon(Position)"
        type="primary"
        size="small"
        @click="saveActivity"
        >保存</el-button
      >
    </div>
    <Sidebar />
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
      <template #node-model="props">
        <ModelNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :get-node-data-by-id="getNodeDataById"
          @select="handleSelect"
        />
      </template>
      <template #node-mcp="props">
        <MCPNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :get-node-data-by-id="getNodeDataById"
          @select="handleSelect"
        />
      </template>
      <template #node-knowledge="props">
        <KnowledgeNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :get-node-data-by-id="getNodeDataById"
          @select="handleSelect"
        />
      </template>
      <template #node-system="props">
        <SystemNode
          :id="props.id"
          :is-submit="isSubmit"
          :data="props.data"
          :selected="selectedNodeId === props.id"
          :get-node-data-by-id="getNodeDataById"
          @select="handleSelect"
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
