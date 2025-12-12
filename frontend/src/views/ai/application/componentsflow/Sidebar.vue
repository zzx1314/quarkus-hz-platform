<script setup>
import useDragAndDrop from "./useDnD.js";
import CarbonFlowModelerReference from "~icons/carbon/flow-modeler-reference";
import VaadinTools from "~icons/vaadin/tools";
import CarbonIbmWatsonKnowledgeStudio from "~icons/carbon/ibm-watson-knowledge-studio";
import CodiconRobot from "~icons/codicon/robot";

const { onDragStart } = useDragAndDrop();
</script>

<template>
  <aside>
    <div class="nodes">
      <div class="node-item">
        <div
          class="circle-node-start"
          :draggable="true"
          @dragstart="onDragStart($event, 'start')"
        />
        开始节点
      </div>

      <div class="node-item">
        <div
          class="circle-node-end"
          :draggable="true"
          @dragstart="onDragStart($event, 'end')"
        />
        结束节点
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'system')">
          <codicon-robot style="font-size: 3em; color: #4caf50" />
        </div>
        <span class="node-text">系统节点</span>
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'model')">
          <carbon-flow-modeler-reference
            style="font-size: 3em; color: #0000cc"
          />
        </div>
        模型节点
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'mcp')">
          <vaadin-tools style="font-size: 3em; color: #009966" />
        </div>
        MCP节点
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'knowledge')">
          <carbon-ibm-watson-knowledge-studio
            style="font-size: 3em; color: #330033"
          />
        </div>
        <span class="node-text">知识库节点</span>
      </div>
    </div>
  </aside>
</template>

<style scoped>
.nodes {
  display: flex;
  flex-direction: column;
  align-items: center; /* 上下元素整体左右居中 */
}
.node-item {
  width: 120px;
  display: flex;
  align-items: center; /* 垂直居中 */
  gap: 8px; /* 图标和文字之间的小间距（可选） */
  margin-bottom: 10px;
  color: black;
}

.circle-node-start {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: transparent;
  border: 2px solid #5370ff;
}

.circle-node-end {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: transparent;
  border: 2px solid #9f0429;
}
.node-text {
  width: 60px; /* 固定宽度，适配最长文字 */
  text-align: center; /* 文字居中 */
}
</style>
