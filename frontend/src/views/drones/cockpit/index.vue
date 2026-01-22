<script setup lang="ts">
defineOptions({
  name: "DronesCockpit"
});
</script>
<template>
  <div class="main">
    <div class="container">
      <img src="./test.png" alt="图片描述" />
    </div>
  </div>
</template>

<style scoped lang="scss">
:deep(.el-dropdown-menu__item i) {
  margin: 0;
}

.container {
  width: 1200px; /* 设置容器宽度 */
  border: 1px solid #ccc;
}

.container img {
  width: 100%; /* 让图片宽度充满容器 */
  height: auto; /* 保持图片比例，防止变形 */
  display: block; /* 消除图片下方的微小间隙 */
}
</style>
