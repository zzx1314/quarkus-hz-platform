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

const aiMcpUrls = {
  page: `/api/aiMcp/getPage`,
  save: "/api/aiMcp/save",
  delete: `/api/aiMcp/`,
  update: "/api/aiMcp/update",
  uploadFile: "/api/aiMcp/uploadFile",
  allSelectOption: "/api/aiMcp/allSelectOption",
  enableMcp: `/api/aiMcp/enableMcp/`
};

// MCP服务分页
export const aiMcpPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiMcpUrls.page, query);
};
// MCP服务保存
export const aiMcpSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiMcpUrls.save, param);
};
// MCP服务修改
export const aiMcpUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiMcpUrls.update, param);
};
// MCP服务删除
export const aiMcpDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiMcpUrls.delete + param);
};
// MCP服务上传文件
export const aiMcpUploadFile = (param?: object) => {
  return http.uploadFile<Result>(aiMcpUrls.uploadFile, param);
};
// MCP服务下拉框
export const aiMcpSelect = () => {
  return http.axiosGetRequest<Result>(aiMcpUrls.allSelectOption, {});
};
// MCP服务启用
export const aiMcpEnable = (param?: any) => {
  return http.axiosPut<Result>(
    aiMcpUrls.enableMcp + param.id + "/" + param.enable,
    {}
  );
};
