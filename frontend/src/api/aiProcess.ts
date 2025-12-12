import { http } from "@/utils/http";

type Result = {
  code: number;
  msg: string;
  data?: Array<any>;
};

const aiProcessUrls = {
  page: `/api/aiProcess/getPage`,
  save: "/api/aiProcess/save",
  delete: `/api/aiProcess/`,
  update: "/api/aiProcess/update",
  getByAppId: `/api/aiProcess/getByAppId/`
};

// 按照应用id进行查询
export const aiProcessGetById = (query?: object) => {
  return http.axiosGetRequest<Result>(aiProcessUrls.getByAppId + query, {});
};
// 应用保存
export const aiProcessSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiProcessUrls.save, param);
};
// 应用修改
export const aiProcessUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiProcessUrls.update, param);
};
// 应用删除
export const aiProcessDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiProcessUrls.delete + param);
};
