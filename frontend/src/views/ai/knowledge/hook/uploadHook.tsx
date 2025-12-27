import { isString, isEmpty } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import {
  useRouter,
  useRoute,
  type LocationQueryRaw,
  type RouteParamsRaw
} from "vue-router";

export function useDetail() {
  const route = useRoute();
  const router = useRouter();
  const getParameter = isEmpty(route.params) ? route.query : route.params;

  function toDetail(parameter: LocationQueryRaw | RouteParamsRaw) {
    Object.keys(parameter).forEach(param => {
      if (!isString(parameter[param])) {
        parameter[param] = parameter[param].toString();
      }
    });
    // 保存信息到标签页
    useMultiTagsStoreHook().handleTags("push", {
      path: `/ai/knowledge/upload`,
      name: "AiDocumentUpload",
      query: parameter,
      meta: {
        title: {
          zh: `${parameter.name} - 上传页面`,
          en: `${parameter.name} - Upload`
        },
        dynamicLevel: 3
      }
    });
    // 路由跳转
    router.push({ name: "AiDocumentUpload", query: parameter });
  }

  // 用于页面刷新，重新获取浏览器地址栏参数并保存到标签页
  const initToDetail = () => {
    if (getParameter) toDetail(getParameter);
  };

  return { toDetail, initToDetail, getParameter, router };
}
