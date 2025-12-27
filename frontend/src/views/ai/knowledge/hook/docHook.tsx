import { isString, isEmpty } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import {
  useRouter,
  useRoute,
  type LocationQueryRaw,
  type RouteParamsRaw
} from "vue-router";

export function useDocument() {
  const route = useRoute();
  const router = useRouter();
  const getParameter = isEmpty(route.params) ? route.query : route.params;

  function toDocDetail(parameter: LocationQueryRaw | RouteParamsRaw) {
    Object.keys(parameter).forEach(param => {
      if (!isString(parameter[param])) {
        parameter[param] = parameter[param].toString();
      }
    });
    // 保存信息到标签页
    useMultiTagsStoreHook().handleTags("push", {
      path: `/ai/document/index`,
      name: "AiDocumentIndex",
      query: parameter,
      meta: {
        title: {
          zh: `${parameter.name} - 文档页面`,
          en: `${parameter.name} - document`
        },
        dynamicLevel: 3
      }
    });
    // 路由跳转
    router.push({ name: "AiDocumentIndex", query: parameter });
  }

  // 用于页面刷新，重新获取浏览器地址栏参数并保存到标签页
  const initToDetail = () => {
    if (getParameter) toDocDetail(getParameter);
  };

  return { toDocDetail, initToDetail, getParameter, router };
}
