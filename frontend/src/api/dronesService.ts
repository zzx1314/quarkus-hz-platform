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

const dronesServicesUrls = {
  page: `/api/dronesServices/getPage`,
  save: "/api/dronesServices/create",
  delete: `/api/dronesServices/`,
  update: "/api/dronesServices/update",
  getSelectOption: "/api/dronesServices/getSelectOption"
};

// 分页
export const dronesServicesPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesServicesUrls.page, query);
};
// 保存
export const dronesServicesSave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesServicesUrls.save, param);
};
// 修改
export const dronesServicesUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesServicesUrls.update, param);
};
// 删除
export const dronesServicesDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesServicesUrls.delete + param);
};
// 获取下拉选项
export const dronesServicesGetSelectOptions = (query?: object) => {
  return http.axiosGetRequest<Result>(
    dronesServicesUrls.getSelectOption,
    query
  );
};
