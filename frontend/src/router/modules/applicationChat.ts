export default {
  path: "/appChat",
  redirect: "/ai/application/applicationChat",
  name: "AiApplicationChat",
  meta: {
    title: "应用对话"
  },
  children: [
    {
      path: "/ai/application/applicationChat",
      name: "appChat",
      component: () => import("@/views/ai/application/applicationChat.vue"),
      meta: {
        title: "应用对话",
        activePath: "/ai/application/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
