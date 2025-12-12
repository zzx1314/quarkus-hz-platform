import { http } from "@/utils/http";

type Result = {
  code: number;
  msg: string;
  data?: Array<any>;
};

type ResultOne = {
  code: number;
  msg: string;
  data?: any;
};

type ResultPage = {
  code: number;
  msg: string;
  data?: {
    records: Array<any>;
    total: number;
  };
};

const dronesTaskUrls = {
  page: `/api/dronesTask/getPage`,
  save: "/api/dronesTask/create",
  delete: `/api/dronesTask/`,
  update: "/api/dronesTask/update",
  saveFlow: "/api/dronesWorkflow/create",
  updateFlow: "/api/dronesWorkflow/update",
  getFlow: "/api/dronesWorkflow/getWorkflowByTaskId/",
  startTask: "/api/dronesTask/startTask/",
  getRouteByTaskId: "/api/dronesWorkflow/getRouteByTaskId/",
  getCommandJsonString: "/api/dronesWorkflow/getCommandJsonString/"
};

// 分页
export const dronesTaskPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesTaskUrls.page, query);
};
// 保存
export const dronesTaskSave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesTaskUrls.save, param);
};
// 保存流程
export const dronesTaskSaveFlow = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesTaskUrls.saveFlow, param);
};
// 修改流程
export const dronesTaskUpdateFlow = (param?: object) => {
  return http.axiosPut<Result>(dronesTaskUrls.updateFlow, param);
};
// 查询流程
export const dronesTaskGetFlow = (id: string | number) => {
  return http.axiosGetRequest<Result>(`${dronesTaskUrls.getFlow}${id}`, {});
};
// 修改
export const dronesTaskUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesTaskUrls.update, param);
};
// 删除
export const dronesTaskDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesTaskUrls.delete + param);
};
// 启动
export const dronesTaskStart = (param?: object) => {
  return http.axiosGetRequest<Result>(dronesTaskUrls.startTask + param, {});
};
// 获取任务下的所有执行航线
export const dronesTaskGetRouteByTaskId = (id: string | number) => {
  return http.axiosGetRequest<Result>(
    `${dronesTaskUrls.getRouteByTaskId}${id}`,
    {}
  );
};

// 获取任务下的指令集JSON字符串
export const droneGetCommandJsonString = (id: string | number) => {
  return http.axiosGetRequest<ResultOne>(
    `${dronesTaskUrls.getCommandJsonString}${id}`,
    {}
  );
};
