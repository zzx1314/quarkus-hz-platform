<script setup lang="ts">
import { useDark, useECharts } from "@pureadmin/utils";
import { type PropType, ref, computed, watch, nextTick } from "vue";

const props = defineProps({
  mcpData: {
    type: Array as PropType<Array<number>>,
    default: () => []
  },
  applicationData: {
    type: Array as PropType<Array<number>>,
    default: () => []
  },
  documentData: {
    type: Array as PropType<Array<number>>,
    default: () => []
  },
  aiKnowledgeBaseData: {
    type: Array as PropType<Array<number>>,
    default: () => []
  }
});

const { isDark } = useDark();

const theme = computed(() => (isDark.value ? "dark" : "light"));

const chartRef = ref();
const { setOptions } = useECharts(chartRef, {
  theme
});

watch(
  () => props,
  async () => {
    await nextTick(); // 确保DOM更新完成后再执行
    setOptions({
      container: ".bar-card",
      color: ["#41b6ff", "#e86033ce", "#5BECABFF", "#BA53FFFF"],
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "none"
        }
      },
      grid: {
        top: "20px",
        left: "50px",
        right: 0
      },
      legend: {
        data: ["知识库数量", "应用数量", "MCP数量", "文档数量"],
        textStyle: {
          color: "#606266",
          fontSize: "0.875rem"
        },
        bottom: 0
      },
      xAxis: [
        {
          type: "category",
          data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
          axisLabel: {
            fontSize: "0.875rem"
          },
          axisPointer: {
            type: "shadow"
          }
        }
      ],
      yAxis: [
        {
          type: "value",
          axisLabel: {
            fontSize: "0.875rem"
          },
          splitLine: {
            show: false // 去网格线
          }
          // name: "单位: 个"
        }
      ],
      series: [
        {
          name: "知识库数量",
          type: "bar",
          barWidth: 10,
          itemStyle: {
            color: "#41b6ff",
            borderRadius: [10, 10, 0, 0]
          },
          data: props.aiKnowledgeBaseData
        },
        {
          name: "应用数量",
          type: "bar",
          barWidth: 10,
          itemStyle: {
            color: "#e86033ce",
            borderRadius: [10, 10, 0, 0]
          },
          data: props.applicationData
        },
        {
          name: "MCP数量",
          type: "bar",
          barWidth: 10,
          itemStyle: {
            color: "#5BECABFF",
            borderRadius: [10, 10, 0, 0]
          },
          data: props.mcpData
        },
        {
          name: "文档数量",
          type: "bar",
          barWidth: 10,
          itemStyle: {
            color: "#BA53FFFF",
            borderRadius: [10, 10, 0, 0]
          },
          data: props.documentData
        }
      ]
    });
  },
  {
    deep: true,
    immediate: true
  }
);
</script>

<template>
  <div ref="chartRef" style="width: 100%; height: 365px" />
</template>
