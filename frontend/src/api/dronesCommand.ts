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

const dronesCommandUrls = {
  page: `/api/dronesCommand/getPage`,
  save: "/api/dronesCommand/create",
  delete: `/api/dronesCommand/`,
  update: "/api/dronesCommand/update"
};

// 分页
export const dronesCommandPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesCommandUrls.page, query);
};
// 保存
export const dronesCommandSave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesCommandUrls.save, param);
};
// 修改
export const dronesCommandUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesCommandUrls.update, param);
};
// 删除
export const dronesCommandDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesCommandUrls.delete + param);
};
