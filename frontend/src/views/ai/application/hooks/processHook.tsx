import { isString, isEmpty } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import {
  useRouter,
  useRoute,
  type LocationQueryRaw,
  type RouteParamsRaw
} from "vue-router";

export function useProcess() {
  const route = useRoute();
  const router = useRouter();
  const getParameter = isEmpty(route.params) ? route.query : route.params;

  function toProcessDetail(parameter: LocationQueryRaw | RouteParamsRaw) {
    Object.keys(parameter).forEach(param => {
      if (!isString(parameter[param])) {
        parameter[param] = parameter[param].toString();
      }
    });
    // 保存信息到标签页
    useMultiTagsStoreHook().handleTags("push", {
      path: `/ai/application/flowApp`,
      name: "AiApplicationIndex",
      query: parameter,
      meta: {
        title: {
          zh: `${parameter.name} - 流程编排`,
          en: `${parameter.name} - process`
        },
        dynamicLevel: 3
      }
    });
    // 路由跳转
    router.push({ name: "AiApplicationIndex", query: parameter });
  }

  // 用于页面刷新，重新获取浏览器地址栏参数并保存到标签页
  const initToDetail = () => {
    if (getParameter) toProcessDetail(getParameter);
  };

  return { toProcessDetail, initToDetail, getParameter, router };
}
