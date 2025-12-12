<script setup>
import useDragAndDrop from "./useDnD.js";
import BankCardLine from "~icons/ri/bank-card-line";
import Settings3Line from "~icons/ri/settings-3-line";
import SendPlane2Line from "~icons/ri/send-plane-2-line";

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
        <p style="font-size: 14px">开始节点</p>
      </div>

      <div class="node-item">
        <div
          class="circle-node-end"
          :draggable="true"
          @dragstart="onDragStart($event, 'end')"
        />
        <p style="font-size: 14px">结束节点</p>
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'task')">
          <SendPlane2Line style="font-size: 3em; color: #330033" />
        </div>
        <span class="node-text">任务节点</span>
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'action')">
          <BankCardLine style="font-size: 3em; color: #0000cc" />
        </div>
        <p style="font-size: 14px">动作节点</p>
      </div>
      <div class="node-item">
        <div :draggable="true" @dragstart="onDragStart($event, 'error')">
          <Settings3Line style="font-size: 3em; color: #009966" />
        </div>
        <p style="font-size: 14px">错误处理</p>
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
  font-size: 14px;
  text-align: center; /* 文字居中 */
}
</style>
