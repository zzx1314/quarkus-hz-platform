import { http } from "@/utils/http";

type Result = {
  code: number;
  msg: string;
  data?: Array<any>;
};

type ResultOne = {
  code: number;
  msg: string;
  data: any;
};

type ResultPage = {
  code: number;
  msg: string;
  data?: {
    records: Array<any>;
    total: number;
  };
};

const aiMcpToolsUrls = {
  page: `/api/aiMcpTools/getPage`,
  save: "/api/aiMcpTools/create",
  delete: `/api/aiMcpTools/`,
  update: "/api/aiMcpTools/update",
  findById: `/api/aiMcpTools/`,
  allSelectOption: `/api/aiMcpTools/allSelectOption/`
};

// mcp工具分页
export const aiMcpToolsPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiMcpToolsUrls.page, query);
};
// mcp工具保存
export const aiMcpToolsSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiMcpToolsUrls.save, param);
};
// mcp工具修改
export const aiMcpToolsUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiMcpToolsUrls.update, param);
};
// mcp工具删除
export const aiMcpToolsDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiMcpToolsUrls.delete + param);
};
// mcp工具查询
export const aiMcpToolsFindById = (param?: object) => {
  return http.axiosGetRequest<ResultOne>(aiMcpToolsUrls.findById + param, {});
};
// mcp工具下拉框
export const aiMcpToolsAllSelectOption = (param?: object) => {
  return http.axiosGetRequest<Result>(
    aiMcpToolsUrls.allSelectOption + param,
    {}
  );
};
