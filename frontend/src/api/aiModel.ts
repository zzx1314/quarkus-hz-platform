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

const aiModelUrls = {
  page: `/api/aiModel/getPage`,
  save: "/api/aiModel/save",
  delete: `/api/aiModel/`,
  update: "/api/aiModel/update",
  allSelectOption: "/api/aiModel/allSelectOption"
};

// 模型分页
export const aiModelPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiModelUrls.page, query);
};
// 模型保存
export const aiModelSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiModelUrls.save, param);
};
// 模型修改
export const aiModelUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiModelUrls.update, param);
};
// 模型删除
export const aiModelDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiModelUrls.delete + param);
};
export const aiModelAllSelectOption = () => {
  return http.axiosGetRequest<Result>(aiModelUrls.allSelectOption, {});
};
