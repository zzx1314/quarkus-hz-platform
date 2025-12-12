<template>
  <div class="compass-container">
    <div ref="chartRef" class="chart" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount, nextTick } from "vue";
import * as echarts from "echarts";

const chartRef = ref(null);
let chartInstance = null;
const angle = ref(0);

function degToChineseDir(v) {
  v = Math.round(v) % 360;
  const dirs = [
    { d: 0, label: "北" },
    { d: 45, label: "东北" },
    { d: 90, label: "东" },
    { d: 135, label: "东南" },
    { d: 180, label: "南" },
    { d: 225, label: "西南" },
    { d: 270, label: "西" },
    { d: 315, label: "西北" }
  ];
  const nearest = (Math.round(v / 45) * 45) % 360;
  const found = dirs.find(x => x.d === nearest);
  return found ? found.label : "";
}

function initChart() {
  chartInstance = echarts.init(chartRef.value);
  updateChartOption();
  window.addEventListener("resize", handleResize);
}

function updateChartOption() {
  if (!chartInstance) return;

  const size = chartRef.value.clientWidth; // 容器宽度
  const scale = size / 230; // 以200px为设计基准

  const option = {
    series: [
      {
        name: "航向",
        type: "gauge",
        radius: `${70 * scale}%`,
        startAngle: 90,
        endAngle: -269.9999,
        min: 0,
        max: 360,
        splitNumber: 16,
        axisLine: {
          lineStyle: { width: 8 * scale, color: [[1, "#333"]] }
        },
        axisTick: {
          distance: -10 * scale,
          length: 8 * scale,
          lineStyle: { color: "#666", width: 1 * scale }
        },
        splitLine: {
          distance: -16 * scale,
          length: 14 * scale,
          lineStyle: { color: "#444", width: 2 * scale }
        },
        axisLabel: {
          distance: -36 * scale,
          fontSize: 16 * scale,
          fontWeight: "bold",
          color: "#fff",
          formatter(value) {
            if (value % 45 === 0) return degToChineseDir(value);
            return "";
          }
        },
        pointer: {
          length: `${63 * scale}%`,
          width: 6 * scale,
          itemStyle: { color: "#d9534f" }
        },
        detail: {
          formatter(value) {
            const v = Math.round(value) % 360;
            const dir = degToChineseDir(v);
            return `${v}°\n${dir}`;
          },
          fontSize: 18 * scale,
          lineHeight: 28 * scale,
          offsetCenter: [0, "68%"],
          color: "#fff"
        },
        title: {
          show: true,
          offsetCenter: [0, "-10%"],
          fontSize: 16 * scale,
          color: "#333",
          formatter: "航向"
        },
        data: [{ value: angle.value }]
      },
      {
        type: "pie",
        z: 1,
        radius: [`${78 * scale}%`, `${83 * scale}%`],
        silent: true,
        label: { show: false },
        data: Array.from({ length: 36 }, (_, i) => ({
          value: 1,
          itemStyle: { color: i % 5 === 0 ? "#666" : "#ddd" }
        }))
      }
    ]
  };

  chartInstance.setOption(option, true);
}

function setAngle(deg) {
  if (!chartInstance) return;
  deg = (Number(deg) + 360) % 360;
  chartInstance.setOption({
    series: [{ name: "航向", data: [{ value: deg }] }]
  });
}

function handleResize() {
  if (chartInstance) {
    chartInstance.resize();
    updateChartOption();
  }
}

watch(angle, newVal => setAngle(newVal));

onMounted(async () => {
  await nextTick();
  initChart();
});

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose();
    chartInstance = null;
  }
  window.removeEventListener("resize", handleResize);
});
</script>

<style scoped>
.chart {
  width: 200px;
  height: 200px;
  margin: 0 auto;
}
</style>
