export default {
  path: "/docIndex",
  redirect: "/ai/document/index",
  name: "AiDocumentIndex",
  meta: {
    title: "文档列表"
  },
  children: [
    {
      path: "/ai/document/index",
      name: "docIndex",
      component: () => import("@/views/ai/document/index.vue"),
      meta: {
        title: "文档列表",
        activePath: "/ai/knowledge/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
