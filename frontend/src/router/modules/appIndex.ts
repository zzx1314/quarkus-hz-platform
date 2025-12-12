export default {
  path: "/appIndex",
  redirect: "/ai/application/flowApp",
  name: "AiApplicationIndex",
  meta: {
    title: "流程编排"
  },
  children: [
    {
      path: "/ai/application/flowApp",
      name: "appFlow",
      component: () => import("@/views/ai/application/flowApp.vue"),
      meta: {
        title: "流程编排",
        activePath: "/ai/application/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
