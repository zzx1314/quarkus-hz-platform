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

const aiStatisticsBaseUrls = {
  statisticsAll: `/api/aiStatistics/statisticsAll`,
  statisticsAllType: `/api/aiStatistics/statisticsAllType`,
  statisticsDocNumber: "/api/aiStatistics/statisticsDocNumber"
};

// 统计数量
export const statisticsAll = (query?: object) => {
  return http.axiosGetRequest<Result>(
    aiStatisticsBaseUrls.statisticsAll,
    query
  );
};
// 统计一周内，知识库，MCP，应用，文档创建的数量
export const statisticsAllType = (param?: number) => {
  return http.axiosGetRequest<ResultOne>(
    aiStatisticsBaseUrls.statisticsAllType,
    param
  );
};
// 统计在知识库中文档的数量
export const statisticsDocNumber = (param?: object) => {
  return http.axiosGetRequest<ResultOne>(
    aiStatisticsBaseUrls.statisticsDocNumber,
    param
  );
};
