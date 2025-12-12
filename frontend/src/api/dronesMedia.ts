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

const dronesMediaUrls = {
  page: `/api/dronesMedia/getPage`,
  save: "/api/dronesMedia/create",
  delete: `/api/dronesMedia/`,
  update: "/api/dronesMedia/update",
  uploadFile: "/api/dronesMedia/uploadFile",
  downloadByPath: "/api/dronesMedia/downFile"
};

// 分页
export const dronesMediaPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesMediaUrls.page, query);
};
// 保存
export const dronesMediaSave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesMediaUrls.save, param);
};
// 修改
export const dronesMediaUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesMediaUrls.update, param);
};
// 删除
export const dronesMediaDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesMediaUrls.delete + param);
};
// 上传文件
export const dronesMediaUploadFile = (param?: object) => {
  return http.uploadFile<Result>(dronesMediaUrls.uploadFile, param);
};
// 下载文件
export const dronesMediaDownloadByPath = (param?: any) => {
  return http.downloadUrlMode(
    dronesMediaUrls.downloadByPath,
    "get",
    param.fileName,
    param
  );
};
