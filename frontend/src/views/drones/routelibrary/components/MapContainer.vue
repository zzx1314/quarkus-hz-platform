<script setup>
import { onMounted, onUnmounted, ref } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import { message } from "@/utils/message";
import { SUCCESS } from "@/api/base.js";
import { dronesRouteLibrarySaveRouteData } from "@/api/dronesRouteLibrary";
import Compass from "../../datadashboard/components/Compass.vue";
import High from "../../datadashboard/components/High.vue";
import Speed from "../../datadashboard/components/Speed.vue";
import RiBattery2ChargeLine from "~icons/ri/battery-2-charge-line";
import RiWifiLine from "~icons/ri/wifi-line";
import RiFlightTakeoffLine from "~icons/ri/flight-takeoff-line";
import GisLayerPoi from "~icons/gis/layer-poi";
import LucideDrone from "~icons/lucide/drone";
import MdiTimerMinusOutline from "~icons/mdi/timer-minus-outline";
import LsiconPathOutline from "~icons/lsicon/path-outline";
import FluentClipboardNumber12328Regular from "~icons/fluent/clipboard-number-123-28-regular";
import EosIconsDrone from "~icons/eos-icons/drone";

let map = null;
let AMapLib = null;
let polyline = null;
let polylineEditor = null;
const points = ref([]);
const markers = [];

let overlay = null; // 点击捕获层
let keyHandler = null;

const drawing = ref(false); // 是否在绘制中

defineOptions({
  name: "MapContainer"
});

const props = defineProps({
  id: {
    type: Number,
    required: false
  },
  type: {
    type: String,
    default: "route"
  },
  routeData: {
    type: String,
    default: ""
  }
});

//  转换路径为数值数组
function toPlainPathArray(arr) {
  return arr
    .map(p => {
      const lng = Number(p[0]);
      const lat = Number(p[1]);
      return isNaN(lng) || isNaN(lat) ? null : [lng, lat];
    })
    .filter(Boolean);
}

// 添加透明覆盖层
function addOverlay() {
  if (!map || overlay) return;
  const container = document.getElementById("container");

  overlay = document.createElement("div");
  overlay.style.position = "absolute";
  overlay.style.top = "0";
  overlay.style.left = "0";
  overlay.style.right = "0";
  overlay.style.bottom = "0";
  overlay.style.cursor = "crosshair";
  overlay.style.zIndex = 9999;
  container.appendChild(overlay);

  overlay.addEventListener("click", onOverlayClick);
  overlay.addEventListener("contextmenu", onOverlayRightClick); //捕获右键
  console.log(" 启用绘制点击层");
}

// 移除透明层
function removeOverlay() {
  if (overlay) {
    overlay.removeEventListener("click", onOverlayClick);
    overlay.removeEventListener("contextmenu", onOverlayRightClick);
    overlay.remove();
    overlay = null;
    console.log("🔴 已移除绘制点击层");
  }
}

// 点击添加点
function onOverlayClick(e) {
  if (!drawing.value) return;

  const container = document.getElementById("container");
  const rect = container.getBoundingClientRect();
  const pixel = new AMap.Pixel(e.clientX - rect.left, e.clientY - rect.top);
  const lnglatObj = map.containerToLngLat(pixel);
  const lnglat = [lnglatObj.lng, lnglatObj.lat];

  points.value.push(lnglat);

  if (points.value.length >= 2) {
    polyline.setPath(toPlainPathArray(points.value));
  }

  const circleMarker = new AMap.CircleMarker({
    center: lnglat,
    radius: 6,
    strokeColor: "#fff",
    strokeWeight: 2,
    fillColor: "#FFD700",
    fillOpacity: 1,
    zIndex: 10
  });
  map.add(circleMarker);
  markers.push(circleMarker);

  console.log("添加节点:", lnglat);
}

// 覆盖层右键结束绘制
function onOverlayRightClick(e) {
  e.preventDefault();
  finishDrawing();
}

// 结束绘制并进入编辑
function finishDrawing() {
  if (!drawing.value) return;
  drawing.value = false;

  // 自动闭合路径
  if (points.value.length > 2) {
    const first = points.value[0];
    const last = points.value[points.value.length - 1];
    if (first[0] !== last[0] || first[1] !== last[1]) {
      points.value.push(first);
      polyline.setPath(points.value);
    }
  }

  removeOverlay();
  polylineEditor.open();
  console.log("✏️ 进入编辑模式");
}

// 重置画布
function resetDrawing() {
  console.log("🧹 重置画布");
  drawing.value = false;
  polylineEditor?.close();
  map.clearMap();
  removeOverlay();

  points.value = [];
  markers.length = 0;

  polyline = new AMap.Polyline({
    strokeColor: "#00BFFF",
    strokeWeight: 4,
    lineJoin: "round",
    clickable: false
  });
  map.add(polyline);

  polylineEditor = new AMapLib.PolylineEditor(map, polyline);

  polylineEditor.on("open", () => {
    console.log("✏️ 折线编辑器开启");
    drawing.value = false;
    removeOverlay();
  });

  polylineEditor.on("close", () => {
    console.log("折线编辑器关闭");
  });

  drawing.value = true;
  addOverlay();
  console.log("开始绘制（左键添加点，右键结束）");
}

function savePoints() {
  if (points.value.length < 2) {
    console.log("路径过短，请至少添加两个点");
    message("路径过短，请至少添加两个点", {
      type: "error"
    });
    return;
  }
  console.log("保存路径:", JSON.stringify(points.value));
  const data = {
    id: props.id,
    routeData: JSON.stringify(points.value)
  };
  dronesRouteLibrarySaveRouteData(data).then(res => {
    if (res.code === SUCCESS) {
      console.log("保存成功");
      message("保存成功！", { type: "success" });
    }
  });
}

function loadSavedRoute() {
  console.log("回显路径:", props.routeData);
  const route = JSON.parse(props.routeData);
  if (route && Array.isArray(route) && route.length > 0) {
    const validPath = toPlainPathArray(route);
    points.value = validPath;
    polyline.setPath(validPath);

    // 添加节点标记
    validPath.forEach(lnglat => {
      const circleMarker = new AMap.CircleMarker({
        center: lnglat,
        radius: 6,
        strokeColor: "#fff",
        strokeWeight: 2,
        fillColor: "#FFD700",
        fillOpacity: 1,
        zIndex: 10
      });
      map.add(circleMarker);
      markers.push(circleMarker);
    });

    // 适配视野
    map.setFitView(polyline);
    console.log("路线已回显:", validPath);
  }
}

onMounted(() => {
  AMapLoader.load({
    key: "1efebfae072be8e3e6df10edfbf22abb",
    version: "2.0",
    plugins: ["AMap.PolylineEditor"]
  })
    .then(AMap => {
      AMapLib = AMap;
      map = new AMap.Map("container", {
        viewMode: "3D",
        zoom: 11,
        center: [116.397428, 39.90923]
      });

      // 添加标记点
      const content = `<div class="custom-content-marker">
<img src="/src/assets/svg/drone.svg">
</div>`;
      const marker = new AMap.Marker({
        content: content, //自定义点标记覆盖物内容
        position: [116.397428, 39.90923], //基点位置
        offset: new AMap.Pixel(-13, -30) //相对于基点的偏移位置
      });
      map.add(marker);

      polyline = new AMap.Polyline({
        strokeColor: "#00BFFF",
        strokeWeight: 4,
        lineJoin: "round",
        clickable: false
      });
      map.add(polyline);

      polylineEditor = new AMapLib.PolylineEditor(map, polyline);

      polylineEditor.on("open", () => {
        console.log("折线编辑器开启");
        drawing.value = false;
        removeOverlay();
      });

      polylineEditor.on("close", () => {
        console.log("折线编辑器关闭");
      });

      // 键盘快捷键
      keyHandler = e => {
        if (e.key === "e") {
          if (!drawing.value) {
            drawing.value = true;
            addOverlay();
            console.log("进入绘制模式（左键添加点，右键结束）");
          }
        } else if (e.key === "q") {
          drawing.value = false;
          polylineEditor.close();
          removeOverlay();
          console.log("退出编辑模式");
        } else if (e.key === "r") {
          resetDrawing();
        }
      };
      window.addEventListener("keydown", keyHandler);

      // 回显路线
      if (props.type === "route") {
        loadSavedRoute();
      }
    })
    .catch(e => console.error("地图加载失败：", e));
});

onUnmounted(() => {
  window.removeEventListener("keydown", keyHandler);
  polylineEditor?.close();
  removeOverlay();
  map?.destroy();
});
</script>

<template>
  <div class="main">
    <div id="container" />
    <div v-if="props.type === 'route'" class="info">
      🟡 按 <b>E</b> 开始绘制<br />
      🖱️ 右键结束绘制并进入编辑（自动闭合）<br />
      ⌨️ 按 <b>Q</b> 退出编辑<br />
      🔁 按 <b>R</b> 重新绘制
      <el-button type="primary" @click="savePoints">保存路径</el-button>
    </div>
    <div v-if="props.type !== 'route'" class="info">
      <el-card style="max-width: 480px">
        <template #header>
          <div class="card-header">
            <LucideDrone style="font-size: 25px" />
            <span style="margin-left: 5px">无人机状态</span>
          </div>
        </template>
        <div class="container-status">
          <div class="row">
            <div class="item">
              <RiBattery2ChargeLine style="font-size: 30px; margin: 10px" />
              <div>
                电量
                <div>10%</div>
              </div>
            </div>
            <div class="item">
              <RiWifiLine style="font-size: 30px; margin: 10px" />
              <div>
                通信
                <div>wifi</div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="item">
              <RiFlightTakeoffLine style="font-size: 30px; margin: 10px" />
              <div>
                航向
                <div>0</div>
              </div>
            </div>
            <div class="item">
              <GisLayerPoi style="font-size: 30px; margin: 10px" />
              <div>
                位置
                <div>0</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    <div v-if="props.type !== 'route'" class="top-right">
      <el-card style="max-width: 480px">
        <template #header>
          <div class="card-header">
            <LucideDrone style="font-size: 25px" />
            <span style="margin-left: 5px">飞行数据</span>
          </div>
        </template>
        <div class="container-status">
          <div class="row">
            <div class="item">
              <div style="display: flex">
                <FluentClipboardNumber12328Regular
                  style="font-size: 30px; margin: 10px"
                />
                <div>
                  飞行次数
                  <div>10次</div>
                </div>
              </div>
            </div>
            <div class="item">
              <div style="display: flex">
                <LsiconPathOutline style="font-size: 30px; margin: 10px" />
                <div>
                  飞行距离
                  <div>10.5km</div>
                </div>
              </div>
            </div>
            <div class="item">
              <div style="display: flex">
                <MdiTimerMinusOutline style="font-size: 30px; margin: 10px" />
                <div>
                  飞行时长
                  <div>10.5h</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    <div v-if="props.type !== 'route'" class="bottom-left">
      <el-card style="max-width: 480px">
        <template #header>
          <div class="card-header">
            <LucideDrone style="font-size: 25px" />
            <span style="margin-left: 5px">夜间视频</span>
          </div>
        </template>
        <div class="fly-data">
          <video
            controls
            autoplay
            :src="'http://192.168.41.227:8080/api/dronesMedia/stream/video.mov'"
            width="640"
            height="360"
          />
        </div>
      </el-card>
    </div>
    <div v-if="props.type !== 'route'" class="bottom-right">
      <el-card style="max-width: 480px">
        <template #header>
          <div class="card-header">
            <LucideDrone style="font-size: 25px" />
            <span style="margin-left: 5px">白天数据</span>
          </div>
        </template>
        <div class="fly-data">
          <video
            controls
            autoplay
            :src="'http://192.168.41.227:8080/api/dronesMedia/stream/video.mov'"
            width="640"
            height="360"
          />
        </div>
      </el-card>
    </div>
    <div v-if="props.type !== 'route'" class="bottom-center">
      <div class="box">
        <el-card style="max-width: 480px">
          <template #header>
            <div class="card-header">
              <LucideDrone style="font-size: 25px" />
              <span style="margin-left: 5px">无人机高度</span>
            </div>
          </template>
          <High />
        </el-card>
      </div>
      <div class="box">
        <el-card style="max-width: 480px">
          <template #header>
            <div class="card-header">
              <LucideDrone style="font-size: 25px" />
              <span style="margin-left: 5px">无人机速度</span>
            </div>
          </template>
          <Speed />
        </el-card>
      </div>
      <div class="box">
        <el-card style="max-width: 480px">
          <template #header>
            <div class="card-header">
              <LucideDrone style="font-size: 25px" />
              <span style="margin-left: 5px">无人机航向</span>
            </div>
          </template>
          <Compass />
        </el-card>
      </div>
    </div>
  </div>
</template>
<style scoped>
.custom-content-marker {
  width: 50px;
  height: 50px;
}
.custom-content-marker img {
  width: 100%;
  height: 100%;
}
:deep(.el-card) {
  --el-card-padding: 0px;
  --el-card-bg-color: #001f3f;
}
#container {
  width: 100%;
  height: 1000px;
  position: relative;
}
.info {
  position: absolute;
  top: 1vw;
  left: 1vw;
  max-width: 30vw;
  word-break: break-word;
}
/* 右上角 */
.top-right {
  position: absolute;
  top: 1vw;
  right: 1vw;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 0.8vw;
  box-shadow: 0 0.2vw 0.6vw rgba(0, 0, 0, 0.2);
  max-width: 30vw;
  word-break: break-word;
}
/* 右下角 */
.bottom-right {
  position: absolute;
  bottom: 1vw;
  right: 1vw;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 0.8vw;
  box-shadow: 0 0.2vw 0.6vw rgba(0, 0, 0, 0.2);
  max-width: 30vw;
  word-break: break-word;
}
/* 左下角 */
.bottom-left {
  position: absolute;
  bottom: 1vw;
  left: 1vw;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 0.8vw;
  box-shadow: 0 0.2vw 0.6vw rgba(0, 0, 0, 0.2);
  max-width: 30vw;
  word-break: break-word;
}
/* 中间容器（包两个 div） */
.bottom-center {
  position: absolute;
  bottom: 1vw;
  left: 50%;
  transform: translateX(-50%); /* 精准居中 */
  display: flex;
  gap: 0.4vw; /* 两个div之间间距 */
}

/* 中间的两个小块 */
.bottom-center .box {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 0.8vw;
  box-shadow: 0 0.2vw 0.6vw rgba(0, 0, 0, 0.2);
}

.fly-data {
  width: 400px;
  height: 200px;
  margin: 0 auto;
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

button {
  margin-top: 8px;
  display: block;
  background-color: #00bfff;
  border: none;
  color: white;
  padding: 4px 10px;
  border-radius: 6px;
  cursor: pointer;
}
button:hover {
  background-color: #0099cc;
}
</style>
