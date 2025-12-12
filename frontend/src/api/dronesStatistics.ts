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

const dronesStatisticsBaseUrls = {
  statisticsAll: `/api/dronesStatistics/statisticsAll`,
  statisticsAllType: `/api/dronesStatistics/statisticsAllType`,
  statisticsDeviceTypeNumber: "/api/dronesStatistics/statisticsDeviceTypeNumber"
};

// 统计数量
export const statisticsAll = (query?: object) => {
  return http.axiosGetRequest<Result>(
    dronesStatisticsBaseUrls.statisticsAll,
    query
  );
};
// 统计设备创建的数量
export const statisticsAllType = (param?: number) => {
  return http.axiosGetRequest<ResultOne>(
    dronesStatisticsBaseUrls.statisticsAllType,
    param
  );
};
// 统计设备类型的数量
export const statisticsDocNumber = (param?: object) => {
  return http.axiosGetRequest<ResultOne>(
    dronesStatisticsBaseUrls.statisticsDeviceTypeNumber,
    param
  );
};
