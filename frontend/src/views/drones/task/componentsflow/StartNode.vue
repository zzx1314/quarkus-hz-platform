<script setup lang="ts">
import { Handle } from "@vue-flow/core";
import { Position } from "@vue-flow/core";
</script>

<template>
  <div class="node-container">
    <div class="circle-node" />
    <Handle
      id="right-output"
      type="source"
      :position="Position.Right"
      style="height: 10px; width: 3px; background-color: #409eff"
    />
    <Handle
      id="bottom-output"
      type="source"
      :position="Position.Bottom"
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
  border: 2px solid #5370ff;
}
</style>
