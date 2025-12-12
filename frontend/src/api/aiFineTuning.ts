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

const aiFineTuningUrls = {
  page: `/api/aiFineTuning/getPage`,
  save: "/api/aiFineTuning/save",
  delete: `/api/aiFineTuning/`,
  update: "/api/aiFineTuning/update"
};

// 微调分页
export const aiFineTuningPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiFineTuningUrls.page, query);
};
// 微调保存
export const aiFineTuningSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiFineTuningUrls.save, param);
};
// 微调修改
export const aiFineTuningUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiFineTuningUrls.update, param);
};
// 微调删除
export const aiFineTuningDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiFineTuningUrls.delete + param);
};
