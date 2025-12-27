export default {
  path: "/docUpload",
  redirect: "/ai/knowledge/upload",
  name: "AiDocumentUpload",
  meta: {
    title: "文件上传"
  },
  children: [
    {
      path: "/ai/knowledge/upload",
      name: "docUpload",
      component: () => import("@/views/ai/knowledge/upload.vue"),
      meta: {
        title: "文件上传",
        activePath: "/ai/knowledge/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
