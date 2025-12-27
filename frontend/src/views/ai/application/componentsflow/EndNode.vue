<script setup lang="ts">
import { Handle } from "@vue-flow/core";
import { Position } from "@vue-flow/core";
</script>

<template>
  <div class="node-container">
    <div class="circle-node" />
    <Handle
      id="left-output"
      type="target"
      :position="Position.Left"
      style="height: 10px; width: 3px; background-color: #409eff"
    />
    <Handle
      id="top-output"
      type="target"
      :position="Position.Top"
      style="height: 3px; width: 10px; background-color: #409eff"
    />
  </div>
</template>

<style scoped>
.circle-node {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: transparent;
  border: 2px solid #9f0429;
}
</style>
