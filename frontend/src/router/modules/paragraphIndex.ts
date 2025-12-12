export default {
  path: "/paragraphIndex",
  redirect: "/ai/document/paragraph/index",
  name: "AiParagraphIndex",
  meta: {
    title: "段落列表"
  },
  children: [
    {
      path: "/ai/document/paragraph/index",
      name: "paragraphIndex",
      component: () => import("@/views/ai/document/paragraph/index.vue"),
      meta: {
        title: "段落列表",
        activePath: "/ai/knowledge/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
