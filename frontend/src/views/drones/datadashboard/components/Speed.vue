<template>
  <div ref="chartRef" class="chart" />
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref, nextTick } from "vue";
import * as echarts from "echarts";

const chartRef = ref(null);
let chartInstance = null;

const drawChart = () => {
  if (!chartInstance || !chartRef.value) return;
  const size = chartRef.value.clientWidth;
  const scale = size / 200; // 200 为基准尺寸

  chartInstance.clear();

  const option = {
    series: [
      {
        type: "gauge",
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 5,
        itemStyle: {
          color: "#58D9F9",
          shadowColor: "rgba(0,138,255,0.45)",
          shadowBlur: 10 * scale
        },
        progress: {
          show: true,
          roundCap: true,
          width: 16 * scale
        },
        pointer: {
          icon: "circle",
          width: 6 * scale,
          length: "60%",
          offsetCenter: [0, 0]
        },
        axisLine: {
          roundCap: true,
          lineStyle: {
            width: 16 * scale,
            color: [[1, "#EAEAEA"]]
          }
        },
        axisTick: {
          splitNumber: 2,
          length: 4 * scale,
          lineStyle: {
            width: 1 * scale,
            color: "#aaa"
          }
        },
        splitLine: {
          length: 8 * scale,
          lineStyle: {
            width: 1.5 * scale,
            color: "#777"
          }
        },
        axisLabel: {
          distance: 15 * scale,
          color: "#444",
          fontSize: 10 * scale,
          fontWeight: 500
        },
        title: { show: false },
        detail: {
          backgroundColor: "rgba(255,255,255,0.9)",
          borderColor: "#ccc",
          borderWidth: 1.5 * scale,
          width: 60 * scale,
          height: 26 * scale,
          lineHeight: 26 * scale,
          borderRadius: 6 * scale,
          offsetCenter: [0, 40 * scale],
          valueAnimation: true,
          formatter: value => `{val|${value.toFixed(0)}}{unit|km/h}`,
          rich: {
            val: {
              fontSize: 16 * scale,
              fontWeight: "bold",
              color: "#333",
              padding: [0, 2 * scale, 0, 0]
            },
            unit: {
              fontSize: 10 * scale,
              color: "#666"
            }
          }
        },
        data: [{ value: 10 }]
      }
    ]
  };

  chartInstance.setOption(option, true);
};

const handleResize = () => {
  chartInstance.resize();
  drawChart();
};

onMounted(async () => {
  await nextTick();
  chartInstance = echarts.init(chartRef.value);
  drawChart();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize);
  chartInstance?.dispose();
});
</script>

<style scoped>
.chart {
  width: 200px;
  height: 200px;
  margin: auto;
}
</style>
