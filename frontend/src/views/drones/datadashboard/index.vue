<script setup lang="ts">
import MapContainer from "@/views/drones/routelibrary/components/MapContainer.vue";
import MapContainerOffline from "../routelibrary/components/MapContainerOffline.vue";
import { useRoute } from "vue-router";
import { onMounted, ref } from "vue";
import { SUCCESS } from "@/api/base";
import { dronesTaskGetRouteByTaskId } from "@/api/dronesTask";

const route = useRoute();
const type = Array.isArray(route.query.type)
  ? route.query.type[0]
  : route.query.type;

const taskId = Array.isArray(route.query.taskId)
  ? route.query.taskId[0]
  : route.query.taskId;

const modelType = ref(null);
const routeData = ref(null);
const modelId = ref(null);
const routeId = ref(null);
const deviceId = ref(null);

onMounted(() => {
  console.log("taskId", taskId);
  dronesTaskGetRouteByTaskId(taskId).then(res => {
    if (res.code === SUCCESS && res.data) {
      console.log("res.data", res.data);
      routeData.value = res.data[0].routeData;
      modelType.value = res.data[0].routeType;
      routeId.value = res.data[0].id;
      modelId.value = res.data[0].modelId;
      deviceId.value = res.data[0].deviceId;
      console.log("modelId", modelType.value);
    }
  });
});

defineOptions({
  name: "DataIndex"
});
</script>

<template>
  <div class="w-full h-full text-center">
    <MapContainer v-if="modelType === '在线地图'" :type="type" />
    <MapContainerOffline
      v-if="modelType === '离线地图'"
      :param-model-id="modelId"
      :route-id="routeId"
      :route-data="routeData"
      :type="type"
      :device-id="deviceId"
    />
  </div>
</template>
