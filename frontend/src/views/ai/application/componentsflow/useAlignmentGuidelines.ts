import { ref } from "vue";
import type { Node } from "@vue-flow/core";
import { useVueFlow } from "@vue-flow/core";

interface GuidelineState {
  x: number | null;
  y: number | null;
}

export function useAlignmentGuidelines(tolerance = 5) {
  const guideline = ref<GuidelineState>({ x: null, y: null });
  const { getNodes, onNodeDrag, onNodeDragStop } = useVueFlow();

  function getCenter(node: Node) {
    const width = (node as any).dimensions?.width || 0;
    const height = (node as any).dimensions?.height || 0;
    return {
      cx: node.position.x + width / 2,
      cy: node.position.y + height / 2,
      left: node.position.x,
      right: node.position.x + width,
      top: node.position.y,
      bottom: node.position.y + height
    };
  }

  onNodeDrag(({ node }) => {
    guideline.value = { x: null, y: null };
    const moving = getCenter(node);

    for (const other of getNodes.value) {
      if (other.id === node.id) continue;
      const target = getCenter(other);

      // 垂直中心对齐
      if (Math.abs(moving.cx - target.cx) <= tolerance) {
        guideline.value.x = target.cx;
      }

      // 左对齐
      if (Math.abs(moving.left - target.left) <= tolerance) {
        guideline.value.x = target.left;
      }

      // 右对齐
      if (Math.abs(moving.right - target.right) <= tolerance) {
        guideline.value.x = target.right;
      }

      // 水平中心对齐
      if (Math.abs(moving.cy - target.cy) <= tolerance) {
        guideline.value.y = target.cy;
      }

      // 顶部对齐
      if (Math.abs(moving.top - target.top) <= tolerance) {
        guideline.value.y = target.top;
      }

      // 底部对齐
      if (Math.abs(moving.bottom - target.bottom) <= tolerance) {
        guideline.value.y = target.bottom;
      }
    }
  });

  onNodeDragStop(() => {
    guideline.value = { x: null, y: null };
  });

  return { guideline };
}
