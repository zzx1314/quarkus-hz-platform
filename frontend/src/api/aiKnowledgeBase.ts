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

const aiKnowledgeBaseUrls = {
  page: `/api/aiKnowledgeBase/getPage`,
  detail: `/api/aiKnowledgeBase/getById/`,
  save: "/api/aiKnowledgeBase/save",
  delete: `/api/aiKnowledgeBase/`,
  update: "/api/aiKnowledgeBase/update",
  allSelectOption: `/api/aiKnowledgeBase/allSelectOption`
};

// 知识库分页
export const aiKnowledgeBasePage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiKnowledgeBaseUrls.page, query);
};
// 知识库详情
export const aiKnowledgeBaseDetail = (param?: number) => {
  return http.axiosGetRequest<ResultOne>(
    aiKnowledgeBaseUrls.detail + param,
    {}
  );
};
// 知识库保存
export const aiKnowledgeBaseSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiKnowledgeBaseUrls.save, param);
};
// 知识库修改
export const aiKnowledgeBaseUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiKnowledgeBaseUrls.update, param);
};
// 知识库删除
export const aiKnowledgeBaseDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiKnowledgeBaseUrls.delete + param);
};
// 知识库下拉框
export const aiKnowledgeBaseAllSelectOption = () => {
  return http.axiosGetRequest<Result>(aiKnowledgeBaseUrls.allSelectOption, {});
};
