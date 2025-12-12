export default {
  path: "/flowDesign",
  redirect: "/drones/task/flow",
  name: "dronesFlowDesign",
  meta: {
    title: "流程设计"
  },
  children: [
    {
      path: "/drones/task/flow",
      name: "dronesFlowPlanning",
      component: () => import("@/views/drones/task/flowApp.vue"),
      meta: {
        title: "流程设计",
        activePath: "/drones/task/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
