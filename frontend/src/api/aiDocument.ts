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

const aiDocumentUrls = {
  page: `/api/aiDocument/getPage`,
  paragraphPage: `/api/aiDocument/paragraphPage`,
  uploadFile: `/api/aiDocument/uploadFile`,
  uploadFileDoc: `/api/aiDocument/uploadFileDoc`,
  save: "/api/aiDocument/save",
  delete: `/api/aiDocument/`,
  deleteParagraph: `/api/aiDocument/deleteParagraph/`,
  update: "/api/aiDocument/update",
  updateParagraph: "/api/aiDocument/updateParagraph",
  storeParagraph: "/api/aiDocument/storeParagraph",
  hitTest: "/api/aiDocument/hitTest",
  previewFile: "/api/aiDocument/previewFile",
  getDocByKbId: "/api/aiDocument/getDocByKbId/"
};

// 文档分页
export const aiDocumentPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiDocumentUrls.page, query);
};
// 文档段落分页
export const aiDocumentParagraphPage = (query?: object) => {
  return http.axiosGetRequest<ResultPage>(aiDocumentUrls.paragraphPage, query);
};
// 文档保存
export const aiDocumentSave = (param?: object) => {
  return http.axiosPostRequest<Result>(aiDocumentUrls.save, param);
};
// 文档上传
export const aiDocumentUploadFile = (param?: object) => {
  return http.uploadFile<Result>(aiDocumentUrls.uploadFile, param);
};
export const aiDocumentUploadDoc = (param?: object) => {
  return http.uploadFile<Result>(aiDocumentUrls.uploadFileDoc, param);
};
// 存储段落
export const aiDocumentStoreParagraph = (param?: object) => {
  return http.axiosPostRequest<Result>(aiDocumentUrls.storeParagraph, param);
};
// 文档修改
export const aiDocumentUpdate = (param?: object) => {
  return http.axiosPut<Result>(aiDocumentUrls.update, param);
};
// 更新段落
export const aiDocumentUpdateParagraph = (param?: object) => {
  return http.axiosPut<Result>(aiDocumentUrls.updateParagraph, param);
};
// 文档删除
export const aiDocumentDelete = (param?: object) => {
  return http.axiosDelete<Result>(aiDocumentUrls.delete + param);
};
// 删除段落
export const aiDocumentDeleteParagraph = (param?: any) => {
  return http.axiosDelete<Result>(
    aiDocumentUrls.deleteParagraph + param.id + "/" + param.knowledgeId
  );
};
// 命中测试
export const aiDocumentHitTest = (param?: object) => {
  return http.axiosPost<Result>(aiDocumentUrls.hitTest, param);
};
// 文档预览
export const aiDocumentPreviewFile = (param?: object) => {
  return http.axiosGetRequest<Result>(aiDocumentUrls.previewFile, param);
};
export const aiDocumentGetByKbId = (query?: object) => {
  return http.axiosGetRequest<Result>(aiDocumentUrls.getDocByKbId + query, {});
};
