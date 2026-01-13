<template>
  <div class="main">
    <el-button type="primary" link :icon="useRenderIcon(Back)" @click="back()"
      >返回</el-button
    >
    <!-- 顶部选择控件 -->
    <div v-if="type !== 'datadash'" style="margin-top: 10px">
      <el-select
        v-model="modelId"
        placeholder="选择模型"
        clearable
        style="
          margin-left: 10px;
          margin-right: 10px;
          margin-bottom: 10px;
          width: 200px;
        "
        @change="getImageBase64"
      >
        <el-option
          v-for="option in modelOptions"
          :key="option.value"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
      <el-button type="primary" style="margin-bottom: 10px" @click="exportPath"
        >保存路径数据</el-button
      >
      <el-button type="danger" style="margin-bottom: 10px" @click="clearPath"
        >清除路径</el-button
      >
    </div>
    <!-- 上下布局容器 -->
    <div class="vertical-container">
      <!-- 地图 -->
      <div class="map-container">
        <canvas
          ref="canvas"
          @mousedown="handleMouseDown"
          @mousemove="handleMouseMove"
          @contextmenu.prevent
        />
      </div>
      <!-- 下方信息区 -->
      <div v-if="type === 'datadash'" class="info-container">
        <!-- 状态卡片 -->
        <el-card class="status-card">
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
                  <div>{{ battery }}</div>
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
                  <div>{{ course }}</div>
                </div>
              </div>
              <div class="item">
                <GisLayerPoi style="font-size: 30px; margin: 10px" />
                <div>
                  位置
                  <div>{{ location }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
        <!-- 视频卡片 -->
        <el-card class="video-card">
          <template #header>
            <div class="card-header">
              <span class="label-container">
                <div class="left">
                  <LucideDrone style="font-size: 25px" />
                  无人机视频监控
                </div>
                <div class="right">
                  <el-dropdown @command="handleCommandCheck">
                    <el-button
                      link
                      style="color: #409eff; font-size: 15px"
                      type="primary"
                      :icon="useRenderIcon(DownloadIcon)"
                      >目标检测</el-button
                    >
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="start"
                          >开启</el-dropdown-item
                        >
                        <el-dropdown-item command="stop">关闭</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                  <el-dropdown @command="handleCommand">
                    <el-button
                      link
                      style="color: #67c23a; font-size: 15px"
                      type="primary"
                      :icon="useRenderIcon(DownloadIcon)"
                      >目标跟踪</el-button
                    >
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="deepsort_start"
                          >开启</el-dropdown-item
                        >
                        <el-dropdown-item command="deepsort_stop"
                          >关闭</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </span>
            </div>
          </template>
          <div class="container-status">
            <img ref="videoImg" width="500" height="300" />
          </div>
        </el-card>
      </div>
    </div>

    <el-dialog
      v-model="dialogFormVisibleTreacking"
      title="目标跟踪"
      width="800px"
      @close="cancel"
    >
      <div>
        <el-select
          v-model="trackTarget"
          placeholder="选择目标"
          clearable
          style="
            margin-left: 10px;
            margin-right: 10px;
            margin-bottom: 10px;
            width: 200px;
          "
        >
          <el-option
            v-for="option in trackTargetOptions"
            :key="option.value"
            :value="option.value"
            :label="option.label"
          />
        </el-select>
        <el-button
          type="primary"
          link
          :icon="useRenderIcon(Position)"
          @click="issueCommandServer"
          >下发跟踪目标</el-button
        >
      </div>
      <div class="img-wrapper">
        <img ref="videoImgTrack" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, h } from "vue";
import {
  dronesRouteLibraryGetRoute,
  dronesRouteLibrarySaveRouteData
} from "@/api/dronesRouteLibrary";
import { dronesModelGetSelectOption } from "@/api/dronesModel";
import {
  dronesDeviceGetStatus,
  dronesDeviceStartImage,
  dronesDeviceStopImage
} from "@/api/dronesDevice";
import { dronesCommandIssueCommand } from "@/api/dronesCommand";
import { dronesCommandIssueCommonCommand } from "@/api/dronesCommand";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags.js";
import { router } from "@/store/utils.js";
import { ElMessageBox, ElSwitch } from "element-plus";

import { SUCCESS } from "@/api/base";
import { message } from "@/utils/message";
import WebSocketClient from "@/components/ReWebSocket";
import RiBattery2ChargeLine from "~icons/ri/battery-2-charge-line";
import RiWifiLine from "~icons/ri/wifi-line";
import RiFlightTakeoffLine from "~icons/ri/flight-takeoff-line";
import GisLayerPoi from "~icons/gis/layer-poi";
import LucideDrone from "~icons/lucide/drone";
import RiRestartFill from "~icons/ri/restart-fill";
import RiStopCircleLine from "~icons/ri/stop-circle-line";
import Back from "~icons/ep/back";
import Position from "~icons/ep/position";
import DownloadIcon from "~icons/ep/arrow-down";
import { useRenderIcon } from "@/components/ReIcon/src/hooks.js";
import { tr } from "element-plus/es/locale/index.mjs";

/* =========================
 *  props & 常量
 * ========================= */
const props = defineProps({
  deviceId: {
    type: Number,
    default: null
  },
  deviceIdStr: {
    type: String,
    default: ""
  },
  taskId: {
    type: String,
    default: null
  },
  type: {
    type: String,
    default: ""
  },
  routeId: {
    type: Number,
    default: null
  },
  routeData: {
    type: String,
    default: ""
  },
  paramModelId: {
    type: Number,
    default: null
  }
});

const routeId = props.routeId;
const routeData = props.routeData;
const paramModelId = props.paramModelId;
const type = props.type;
console.log("routeData:", type);

/* =========================
 * 基础状态 refs
 * ========================= */
const canvas = ref(null);
const imgBase64 = ref("");
const modelId = ref(null);
const modelOptions = ref([]);

const speed = ref(0.5);
const height = ref(0);
const battery = ref(100);
const course = ref(0);
const location = ref("");
let ctx = null;

/* =========================
 * Canvas / 坐标 / SVG
 * ========================= */
const img = new Image();
const scale = 2;

let cachedDroneIcon = null;
let cachedStartPortIcon = null;

// 坐标转换
const resolution = ref(0.05);
const origin = ref([-8.7, -17.7]);

const svgIcon = `
<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path fill="#17436e" fill-rule="evenodd" d="M8.626 7.077a3.5 3.5 0 1 0-1.549 1.549L8.764 12l-1.687 3.374a3.5 3.5 0 1 0 1.549 1.549L12 15.236l3.374 1.687a3.5 3.5 0 1 0 1.549-1.549L15.236 12l1.687-3.374a3.5 3.5 0 1 0-1.549-1.549L12 8.764zm-1.79-.895a1.5 1.5 0 1 0-.654.654l-.447-.894a.5.5 0 1 1 .207-.207zm10.982.654a1.5 1.5 0 1 0-.654-.654l.894-.447a.5.5 0 1 1 .207.207zm-.654 10.982a1.5 1.5 0 1 0 .654-.654l.447.894a.5.5 0 1 1-.207.207zm-10.982-.654a1.5 1.5 0 1 0 .654.654l-.894.447q.057.106.058.235a.5.5 0 1 1-.265-.442zm6.945-5.027q.022.16.097.31L14 14l-1.553-.776a1 1 0 0 0-.894 0L10 14l.776-1.553a1 1 0 0 0 0-.894L10 10l1.553.776a1 1 0 0 0 .894 0L14 10l-.776 1.553a1 1 0 0 0-.097.584" clip-rule="evenodd"/></svg>
`;

const startport = `
<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
		<g fill="none">
			<path d="m12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035q-.016-.005-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427q-.004-.016-.017-.018m.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093q.019.005.029-.008l.004-.014l-.034-.614q-.005-.018-.02-.022m-.715.002a.02.02 0 0 0-.027.006l-.006.014l-.034.614q.001.018.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01z" />
			<path fill="#ee0d51" d="M10.92 2.868a1.25 1.25 0 0 1 2.16 0l2.795 4.798l5.428 1.176a1.25 1.25 0 0 1 .667 2.054l-3.7 4.141l.56 5.525a1.25 1.25 0 0 1-1.748 1.27L12 19.592l-5.082 2.24a1.25 1.25 0 0 1-1.748-1.27l.56-5.525l-3.7-4.14a1.25 1.25 0 0 1 .667-2.055l5.428-1.176z" />
		</g>
	</svg>
`;

// 将 SVG 转成 Image 对象
function loadSVGImage(svg) {
  return new Promise(resolve => {
    const img = new Image();
    img.onload = () => resolve(img);
    img.onerror = e => console.error(e);
    img.src = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(svg);
  });
}

// 绘制图标
async function drawIcon(x, y) {
  if (!cachedDroneIcon) {
    cachedDroneIcon = await loadSVGImage(svgIcon);
  }
  ctx.drawImage(
    cachedDroneIcon,
    x - cachedDroneIcon.width / 2,
    y - cachedDroneIcon.height / 2
  );
}

async function drawStartPort(x, y) {
  if (!cachedStartPortIcon) {
    cachedStartPortIcon = await loadSVGImage(startport);
  }
  ctx.drawImage(
    cachedStartPortIcon,
    x - cachedStartPortIcon.width / 2,
    y - cachedStartPortIcon.height / 2
  );
}

/* =========================
 * 路径编辑状态
 * ========================= */

//  仅保留一条路径
let currentPath = [];
let drawing = true;
let mousePos = null;

/* =========================
 * 坐标转换
 * ========================= */
// 将世界坐标转换为像素坐标
function worldToPixel([wx, wy]) {
  const originalX = (wx - origin.value[0]) / resolution.value;
  const originalY = img.height - (wy - origin.value[1]) / resolution.value;

  return {
    x: originalX * scale,
    y: originalY * scale
  };
}

function pixelToWorld(p) {
  const originalX = p.x / scale;
  const originalY = p.y / scale;

  const wx = origin.value[0] + originalX * resolution.value;
  const wy = origin.value[1] + (img.height - originalY) * resolution.value;

  return [Number(wx.toFixed(2)), Number(wy.toFixed(2))];
}

/* =========================
 * Canvas 绘制
 * ========================= */
// 重绘：背景 + 唯一路径 + 实时预览 + 点
function redraw() {
  ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);
  ctx.drawImage(img, 0, 0, canvas.value.width, canvas.value.height);

  // 绘制唯一的路径
  if (currentPath.length > 0) {
    drawPath(currentPath);
    drawMarkers(currentPath);
  }

  // 航点1与世界坐标(0,0)连接线
  if (currentPath.length > 0) {
    const zeroPoint = worldToPixel([0, 0]);
    const firstPoint = currentPath[0];

    ctx.strokeStyle = "green"; // 连接线颜色
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(zeroPoint.x, zeroPoint.y);
    ctx.lineTo(firstPoint.x, firstPoint.y);
    ctx.stroke();
    ctx.setLineDash([]);
  }

  // 虚线预览
  if (drawing && mousePos && currentPath.length > 0) {
    const last = currentPath[currentPath.length - 1];
    ctx.strokeStyle = "rgba(0,0,255,0.5)";
    ctx.setLineDash([5, 5]);
    ctx.beginPath();
    ctx.moveTo(last.x, last.y);
    ctx.lineTo(mousePos.x, mousePos.y);
    ctx.stroke();
    ctx.setLineDash([]);
  }

  // 点
  ctx.fillStyle = "yellow";
  // 点
  currentPath.forEach(p => {
    // 外圈（边框）
    ctx.beginPath();
    ctx.arc(p.x, p.y, 6, 0, Math.PI * 2); // 半径稍大，作为边框
    ctx.fillStyle = "black";
    ctx.fill();

    // 内圈（黄色点）
    ctx.beginPath();
    ctx.arc(p.x, p.y, 5, 0, Math.PI * 2); // 半径比边框小
    ctx.fillStyle = "yellow";
    ctx.fill();
  });
  // 绘制svg的标记
  if (currentPath[0]) {
    drawIcon(currentPath[0].x, currentPath[0].y);
  }
  // 世界坐标 0,0 绘制 startport
  const zeroPoint = worldToPixel([0, 0]);
  drawStartPort(zeroPoint.x, zeroPoint.y);
}

// 鼠标按下
function handleMouseDown(e) {
  const pos = getMousePos(e);

  if (e.button === 0) {
    // 左键：开始或继续绘制
    if (!drawing) {
      // 如果上一次结束了，则清空旧路径，重新画新路径
      currentPath = [];
      drawing = true;
    }
    currentPath.push(pos);
    redraw();
  } else if (e.button === 2) {
    // 右键结束绘制，但不保存多条路径
    drawing = false;
    mousePos = null;
    redraw();
  }
}

// 鼠标移动（实时预览）
function handleMouseMove(e) {
  if (!drawing || currentPath.length === 0) return;
  mousePos = getMousePos(e);
  redraw();
}

// 鼠标位置
function getMousePos(e) {
  const rect = canvas.value.getBoundingClientRect();
  return {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  };
}

// 绘制路径线
function drawPath(path) {
  if (path.length < 2) return;
  ctx.strokeStyle = "green";
  ctx.lineWidth = 2;

  ctx.beginPath();
  ctx.moveTo(path[0].x, path[0].y);
  for (let i = 1; i < path.length; i++) {
    ctx.lineTo(path[i].x, path[i].y);
  }
  ctx.stroke();
}

// 保存路径（仅 currentPath）
function exportPath() {
  if (currentPath.length === 0) {
    message("请先绘制路径", { type: "warning" });
    return;
  }

  const worldPath = currentPath.map(p => pixelToWorld(p));
  console.log("世界坐标：", worldPath);

  const param = {
    id: routeId,
    modelId: modelId.value,
    routeData: JSON.stringify(worldPath)
  };

  dronesRouteLibrarySaveRouteData(param).then(res => {
    if (res.code === SUCCESS) {
      message("保存成功！", { type: "success" });
    }
  });
}

// 清空路径
function clearPath() {
  currentPath = [];
  drawing = true;
  mousePos = null;
  // 清空画布并绘制背景图
  ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);
  ctx.drawImage(img, 0, 0, canvas.value.width, canvas.value.height);

  // 保留世界坐标 (0,0) 的 startport
  const zeroPoint = worldToPixel([0, 0]);
  drawStartPort(zeroPoint.x, zeroPoint.y);
}

// 绘制编号、起终点
function drawMarkers(path) {
  ctx.font = "15px Arial";
  ctx.textAlign = "center";
  ctx.textBaseline = "top";

  for (let i = 0; i < path.length; i++) {
    const p = path[i];
    const label = `${i + 1}`;
    ctx.fillStyle = "red";
    ctx.fillText(label, p.x, p.y + 8);
  }

  if (path.length > 0) {
    const zeroPoint = worldToPixel([0, 0]);
    ctx.fillStyle = "#4982ee";
    ctx.fillText("起", zeroPoint.x, zeroPoint.y - 20);

    const end = path[path.length - 1];
    ctx.fillStyle = "orange";
    ctx.fillText("终", end.x, end.y - 20);
  }
}

/* =========================
 *  视频流 & WebSocket
 * ========================= */
const webSocketImag = ref(null);
let latestFrame = null;
let rendering = false;
let lastObjectUrl = null;
const videoImg = ref(null);

function renderVideoLoop() {
  if (latestFrame && !rendering && videoImg.value) {
    rendering = true;

    const blob = new Blob([latestFrame], { type: "image/jpeg" });
    const url = URL.createObjectURL(blob);

    videoImg.value.onload = () => {
      if (lastObjectUrl) {
        URL.revokeObjectURL(lastObjectUrl);
      }
      lastObjectUrl = url;
      rendering = false;
    };

    videoImg.value.src = url;
    latestFrame = null;
  }

  requestAnimationFrame(renderVideoLoop);
}

function startWebscoket() {
  webSocketImag.value = new WebSocketClient();

  webSocketImag.value.connect({
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
      latestFrame = data;
    }
  });
}

function startImage() {
  if (!props.deviceId) return;
  const param = {
    deviceId: props.deviceId
  };
  dronesDeviceStartImage(param).then(res => {
    if (res.code === SUCCESS) {
      console.log("start Image");
    }
  });
}

function stopImage() {
  if (!props.deviceId) return;
  const param = {
    deviceId: props.deviceId
  };
  dronesDeviceStopImage(param).then(res => {
    if (res.code === SUCCESS) {
      console.log("stop Image");
    }
  });
}

function stopImageOnUnload() {
  if (!props.deviceId) return;
  navigator.sendBeacon(
    "/api/imageReceiver/stop",
    new Blob([JSON.stringify({ deviceId: props.deviceId })], {
      type: "application/json"
    })
  );
}

function handlePageClose() {
  stopImageOnUnload();

  if (webSocketImag.value) {
    webSocketImag.value.disconnect();
  }
  stopWebSocketTrack();
}

/* =========================
 *  地图图片加载
 * ========================= */
// 获取航线图片 base64
function getImageBase64() {
  if (!modelId.value) return;
  dronesRouteLibraryGetRoute(modelId.value).then(res => {
    console.log("航线图片 base64:", res.data);
    imgBase64.value = res.data.route;
    resolution.value = res.data.resolution;
    origin.value = res.data.origin;
    img.src = imgBase64.value;

    img.onload = () => {
      canvas.value.width = img.width * scale;
      canvas.value.height = img.height * scale;
      ctx = canvas.value.getContext("2d");

      if (routeData) {
        try {
          restoreRoute(JSON.parse(routeData));
        } catch (e) {
          console.error("routeData 解析失败:", e);
        }
      }
      redraw();
    };
  });
}

// 获取模型选项
function getModelOptions() {
  dronesModelGetSelectOption().then(res => {
    console.log("模型选项:", res.data);
    modelOptions.value = res.data;
    if (paramModelId) {
      modelId.value = Number(paramModelId);
      getImageBase64();
    }
  });
}

// 恢复航线
function restoreRoute(worldPath) {
  currentPath = worldPath.map(pt => worldToPixel(pt));
  drawing = false;
}

/* =========================
 *  指令下发
 * ========================= */
function issueCommand(paramsInput) {
  console.log("issueCommand", paramsInput);
  const params = {
    deviceId: props.deviceId,
    taskId: Number(props.taskId),
    type: ["yolo", "rtsp"],
    param: paramsInput
  };
  dronesCommandIssueCommand(params).then(res => {
    if (res.code === SUCCESS) {
      message("下发成功！", { type: "success" });
    } else {
      message(res.message, { type: "error" });
    }
  });
}

/* ======================================================
 * 指令-服务
 * ====================================================== */
const dialogFormVisibleTreacking = ref(false);
const issueCommandServer = () => {
  const checked = ref(false);

  ElMessageBox({
    title: "下发跟踪目标",
    showCancelButton: true,
    message: () =>
      h(
        "div",
        {
          style: {
            display: "flex",
            alignItems: "center",
            gap: "12px"
          }
        },
        [
          h("span", "是否下发跟踪目标"),
          h(ElSwitch, {
            modelValue: checked.value,
            "onUpdate:modelValue": val => {
              checked.value = val;
            }
          })
        ]
      )
  })
    .then(() => {
      // 用户点了「确定」才执行
      const param = {
        taskId: props.taskId,
        deviceId: props.deviceIdStr,
        type: "server-command",
        params: {
          head: "track",
          data: trackTarget.value,
          isfollow: checked.value ? "true" : "false"
        }
      };

      dronesCommandIssueCommonCommand(param).then(res => {
        if (res.code === SUCCESS) {
          message("指令下发成功", { type: "success" });
        } else {
          message("指令下发失败", { type: "error" });
        }
        dialogFormVisibleTreacking.value = false;
        trackTarget.value = null;
      });
    })
    .catch(() => {
      // 用户点取消
    });
};
// 处理服务开启和关闭
const handleCommand = command => {
  // 目标跟踪--发送跟踪指令
  console.log("handleCommand", command);
  const param = {
    taskId: props.taskId,
    deviceId: props.deviceIdStr,
    type: "server-command",
    params: {
      head: command
    }
  };
  dronesCommandIssueCommonCommand(param).then(res => {
    if (res.code === SUCCESS) {
      message("指令下发成功", { type: "success" });
    } else {
      message("指令下发失败", { type: "error" });
    }
  });
};
// 检测服务
const handleCommandCheck = command => {
  console.log("handleCommandCheck", command);
  issueCommand(command);
};
/* ======================================================
 * WebSocket--目标跟踪
 * ====================================================== */
const videoImgTrack = ref(null);
const webSocketImagTrack = ref(null);
let lastObjectUrlTrack = null;
const trackTargetOptions = ref([]);
const trackTarget = ref(null);
function startWebSocketTrack() {
  if (webSocketImagTrack.value) return;
  webSocketImagTrack.value = new WebSocketClient();

  webSocketImagTrack.value.connect({
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
    onData: function (data, tag) {
      console.log("track tag:", tag); // 0,1,2,
      if (!dialogFormVisibleTreacking.value) {
        dialogFormVisibleTreacking.value = true;
      }
      // 数据处理
      const blob = new Blob([data], { type: "image/jpeg" });
      const url = URL.createObjectURL(blob);

      nextTick(() => {
        if (!videoImgTrack.value) return;

        if (lastObjectUrlTrack) {
          URL.revokeObjectURL(lastObjectUrlTrack);
        }
        if (tag) {
          // 逗号分割字符串
          const ids = tag
            .split(",")
            .map(id => id.trim())
            .filter(id => id !== "");
          trackTargetOptions.value = ids.map(id => ({
            value: id,
            label: `目标-${id}`
          }));
        }
        videoImgTrack.value.src = url;
        lastObjectUrlTrack = url;
      });
    }
  });
}

const cancel = () => {
  dialogFormVisibleTreacking.value = false;
};

function stopWebSocketTrack() {
  if (webSocketImagTrack.value) {
    webSocketImagTrack.value.disconnect();
    webSocketImagTrack.value = null;
  }

  if (lastObjectUrlTrack) {
    URL.revokeObjectURL(lastObjectUrlTrack);
    lastObjectUrlTrack = null;
  }
}

/* ======================================================
 * 返回
 * ====================================================== */
const back = async () => {
  const multiTagsStore = useMultiTagsStoreHook();
  await router.replace({ path: "/drones/routelibrary/index" });
  nextTick(() => {
    multiTagsStore.handleTags("splice", "/drones/routelibrary/planning");
  });
};

let timer = null;
/* =========================
 * 生命周期
 * ========================= */
onMounted(() => {
  startWebscoket();
  renderVideoLoop();
  getModelOptions();
  startWebSocketTrack();
  startImage();
  window.addEventListener("beforeunload", handlePageClose);
  window.addEventListener("pagehide", handlePageClose);
  /* timer = window.setInterval(() => {
    console.log("每 2 秒执行一次任务");
    if (props.deviceId) {
      dronesDeviceGetStatus(props.deviceId).then(res => {
        if (res.code === SUCCESS) {
          console.log("设备状态:", res.data);
          speed.value = res.data.speed;
          height.value = res.data.height;
          battery.value = res.data.battery;
          course.value = res.data.course;
          location.value = res.data.location;
        }
      });
    }
  }, 2000); */
});

onUnmounted(() => {
  window.removeEventListener("beforeunload", handlePageClose);
  window.removeEventListener("pagehide", handlePageClose);
  stopImage();
  stopWebSocketTrack();
  if (timer) {
    clearInterval(timer);
  }
});
</script>

<style scoped lang="scss">
canvas {
  display: block;
}
.vertical-container {
  display: flex;
  flex-direction: row;
  height: calc(100vh - 80px); /* 减去顶部选择控件高度 */
}

/* 左边地图区域 */
.map-container {
  flex: 1; /* 左侧地图占满剩余空间 */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
}

/* 右边信息区：上状态，下视频 */
.info-container {
  flex: 0 0 500px; /* 固定宽度，可调整 */
  display: flex;
  flex-direction: column; /* 上下布局 */
  gap: 10px;
  padding: 10px;
}

.status-card {
  flex: 1;
}

.video-card {
  flex: 2;
}
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
  width: 500px;
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
.label-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}
.left {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
.right {
  margin-left: auto;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.img-wrapper {
  position: relative;
  display: inline-block;
}
</style>
