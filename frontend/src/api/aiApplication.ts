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

const aiApplicationUrls = {
  page: `/api/aiApplication/getPage`,
  save: "/api/aiApplication/create",
  delete: `/api/aiApplication/`,
  update: "/api/aiApplication/update",
  chat: "/api/aiApplication/chat",
  isEnable: "/api/aiApplication/isEnable/",
  saveProcess: "/api/aiProcess/save",
  getByProcessByAppId: "/api/aiProcess/getByAppId/",
  updateProcess: "/api/aiProcess/update"
};

// 应用分页
export const aiApplicationPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiApplicationUrls.page, query);
};
// 应用保存
export const aiApplicationSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiApplicationUrls.save, param);
};
// 应用修改
export const aiApplicationUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiApplicationUrls.update, param);
};
// 应用删除
export const aiApplicationDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiApplicationUrls.delete + param);
};
// 应用聊天
export const aiApplicationChat = (param?: object) => {
  return http.axiosGetRequest<Result>(aiApplicationUrls.chat, param);
};
// 启动应用
export const aiApplicationEnable = (param?: any) => {
  return http.axiosGetRequest<ResultPage>(
    aiApplicationUrls.isEnable + param.id + "/" + !param.isSetup,
    {}
  );
};
// 保存流程
export const saveProcess = (param?: object) => {
  return http.axiosPostRequest<Result>(aiApplicationUrls.saveProcess, param);
};
// 获取流程
export const getByProcessByAppId = (param?: object) => {
  return http.axiosGetRequest<Result>(
    aiApplicationUrls.getByProcessByAppId + param,
    null
  );
};
// 修改流程
export const updateProcess = (param?: object) => {
  return http.axiosPut<Result>(aiApplicationUrls.updateProcess, param);
};
