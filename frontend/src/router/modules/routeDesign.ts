export default {
  path: "/routeDesign",
  redirect: "/drones/routelibrary/planning",
  name: "dronesRouteDesign",
  meta: {
    title: "航线规划"
  },
  children: [
    {
      path: "/drones/routelibrary/planning",
      name: "dronesRoutePlanning",
      component: () => import("@/views/drones/routelibrary/planning/index.vue"),
      meta: {
        title: "航线规划",
        activePath: "/drones/routelibrary/index",
        showLink: false,
        showParent: false
      }
    }
  ]
} satisfies RouteConfigsTable;
