<template>
  <div class="main">
    <div v-if="routeType === '离线地图'">
      <MapContainerOffline
        :routeData="routeData"
        :type="routeType"
        :routeId="routeId"
        :paramModelId="paramModelId"
      />
    </div>
    <div v-if="routeType === '在线地图'" class="main">
      <MapContainer :id="routeId" :routeData="routeData" />
    </div>
  </div>
</template>

<script setup>
import { usePlanning } from "@/views/drones/routelibrary/planning/planningHook";
import MapContainer from "@/views/drones/routelibrary/components/MapContainer.vue";
import MapContainerOffline from "../components/MapContainerOffline.vue";

const { getParameter } = usePlanning();

const routeType = getParameter.type;
const routeId = Number(getParameter.id);
const routeData = getParameter.routeData;
const paramModelId = Number(getParameter.modelId);
</script>

<style scoped lang="scss">
.main {
  position: relative;
}

.deviceInfo {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 1000;
}

:deep(.el-card) {
  --el-card-padding: 0px;
  --el-card-bg-color: #001f3f;
}

.card-header {
  display: flex;
  height: 30px;
  padding-top: 5px;
  padding-left: 5px;
  text-align: left;
  color: #fff;
}

.container-status {
  display: flex;
  flex-direction: column;
  width: 400px;
  height: 100px;
}

.container-status .row {
  display: flex;
  align-items: center;
  flex: 1;
}

.container-status .row .item {
  flex: 1;
  display: flex;
  width: 200px;
  height: 50px;
  color: #fff;
}
</style>
