import { isString, isEmpty } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import {
  useRouter,
  useRoute,
  type LocationQueryRaw,
  type RouteParamsRaw
} from "vue-router";

export function useParagraph() {
  const route = useRoute();
  const router = useRouter();
  const getParameter = isEmpty(route.params) ? route.query : route.params;

  function toParagraphDetail(parameter: LocationQueryRaw | RouteParamsRaw) {
    Object.keys(parameter).forEach(param => {
      if (!isString(parameter[param])) {
        parameter[param] = parameter[param].toString();
      }
    });
    // 保存信息到标签页
    useMultiTagsStoreHook().handleTags("push", {
      path: `/ai/document/paragraph/index`,
      name: "AiParagraphIndex",
      query: parameter,
      meta: {
        title: {
          zh: `${parameter.name} - 段落页面`,
          en: `${parameter.name} - Paragraph`
        },
        dynamicLevel: 3
      }
    });
    // 路由跳转
    router.push({ name: "AiParagraphIndex", query: parameter });
  }

  // 用于页面刷新，重新获取浏览器地址栏参数并保存到标签页
  const initToDetail = () => {
    if (getParameter) toParagraphDetail(getParameter);
  };

  return { toParagraphDetail, initToDetail, getParameter, router };
}
