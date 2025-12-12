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

const dronesModelUrls = {
  page: `/api/dronesModel/getPage`,
  save: "/api/dronesModel/create",
  delete: `/api/dronesModel/`,
  update: "/api/dronesModel/update",
  uploadFile: "/api/dronesModel/uploadFile",
  getSelectOption: "/api/dronesModel/getSelectOption"
};

// 分页
export const dronesModelPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesModelUrls.page, query);
};
// 保存
export const dronesModelSave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesModelUrls.save, param);
};
// 修改
export const dronesModelUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesModelUrls.update, param);
};
// 删除
export const dronesModelDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesModelUrls.delete + param);
};
// 上传文件
export const dronesModelUploadFile = (param?: object) => {
  return http.uploadFile<Result>(dronesModelUrls.uploadFile, param);
};
// 获取下拉选项
export const dronesModelGetSelectOption = () => {
  return http.axiosGetRequest<Result>(dronesModelUrls.getSelectOption, {});
};
