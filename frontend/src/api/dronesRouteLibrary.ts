import { http } from "@/utils/http";

type Result = {
  code: number;
  msg: string;
  data?: Array<any>;
};

type ResultPage = {
  code: number;
  msg: string;
  data?: {
    records: Array<any>;
    total: number;
  };
};

const dronesRouteLibraryUrls = {
  page: `/api/dronesRouteLibrary/getPage`,
  save: "/api/dronesRouteLibrary/create",
  delete: `/api/dronesRouteLibrary/`,
  update: "/api/dronesRouteLibrary/update",
  getRoute: "/api/dronesRouteLibrary/getRoute/",
  getSelectOption: "/api/dronesRouteLibrary/getSelectOption",
  getSelectOptionPoint: "/api/dronesRouteLibrary/getSelectOptionPoint/",
  saveRouteData: "/api/dronesRouteLibrary/saveRouteData",
  startOrStopRoute: "/api/dronesRouteLibrary/startOrStopRoute/",
  getRouteItemByDto: "/api/dronesRouteItem/getByDto"
};

// 分页
export const dronesRouteLibraryPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesRouteLibraryUrls.page, query);
};
// 保存
export const dronesRouteLibrarySave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesRouteLibraryUrls.save, param);
};
// 保存航线数据
export const dronesRouteLibrarySaveRouteData = (param?: object) => {
  return http.axiosPostRequest<Result>(
    dronesRouteLibraryUrls.saveRouteData,
    param
  );
};
// 修改
export const dronesRouteLibraryUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesRouteLibraryUrls.update, param);
};
// 删除
export const dronesRouteLibraryDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesRouteLibraryUrls.delete + param);
};
// 获取航线
export const dronesRouteLibraryGetRoute = (param?: object) => {
  return http.axiosGetRequest<Result>(
    dronesRouteLibraryUrls.getRoute + param,
    ""
  );
};
// 获取下拉选项
export const dronesRouteLibraryGetSelectOption = () => {
  return http.axiosGetRequest<Result>(
    dronesRouteLibraryUrls.getSelectOption,
    {}
  );
};
/**
 * 航点下拉数据
 */
export const dronesRouteLibraryGetSelectOptionPoint = (param?: any) => {
  return http.axiosGetRequest<Result>(
    dronesRouteLibraryUrls.getSelectOptionPoint + param,
    {}
  );
};
// 启动或停止航线
export const dronesRouteLibraryStartOrStopRoute = (param?: any) => {
  return http.axiosGetRequest<Result>(
    dronesRouteLibraryUrls.startOrStopRoute + param.id + "/" + param.status,
    {}
  );
};

/**
 * 获取航点列表
 * @param query
 * @returns
 */
export const getRouteItemByDto = (query?: object) => {
  return http.axiosGetRequest<Result>(
    dronesRouteLibraryUrls.getRouteItemByDto,
    query
  );
};
