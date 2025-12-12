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

const dronesDeviceUrls = {
  page: `/api/dronesDevice/getPage`,
  save: "/api/dronesDevice/create",
  delete: `/api/dronesDevice/`,
  update: "/api/dronesDevice/update",
  getSelectOption: "/api/dronesDevice/getSelectOption",
  getStatus: "/api/dronesDevice/getStatus/"
};

// 分页
export const dronesDevicePage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(dronesDeviceUrls.page, query);
};
// 保存
export const dronesDeviceSave = (param?: object) => {
  return http.axiosPostRequest<Result>(dronesDeviceUrls.save, param);
};
// 修改
export const dronesDeviceUpdate = (param?: object) => {
  return http.axiosPut<Result>(dronesDeviceUrls.update, param);
};
// 删除
export const dronesDeviceDelete = (param?: object) => {
  return http.axiosDelete<Result>(dronesDeviceUrls.delete + param);
};
// 获取下拉选项
export const dronesDeviceGetSelectOptions = (query?: object) => {
  return http.axiosGetRequest<Result>(dronesDeviceUrls.getSelectOption, query);
};
// 获取设备状态
export const dronesDeviceGetStatus = (id: string | number) => {
  return http.axiosGetRequest<Result>(dronesDeviceUrls.getStatus + id, {});
};
