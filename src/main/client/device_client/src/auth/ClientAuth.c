#include "ClientAuth.h"

// 回调函数，用于接收 curl 返回的数据
size_t WriteCallback(void *contents, size_t size, size_t nmemb, void *userp)
{
    (void)userp;
    size_t totalSize = size * nmemb;
    char **token = (char **)userp;

    cJSON *json = cJSON_Parse(contents);
    if (json == NULL)
    {
        const char *error_ptr = cJSON_GetErrorPtr();
        if (error_ptr != NULL)
        {
            fprintf(stderr, "Error before: %s\n", error_ptr);
        }
        return totalSize;
    }

    // 解析JSON数据
    cJSON *item = cJSON_GetObjectItemCaseSensitive(json, "data");
    if (cJSON_IsString(item) && item->valuestring != NULL)
    {
        *token = strdup(item->valuestring);
        if (*token == NULL)
        {
            fprintf(stderr, "Memory allocation failed for token.\n");
            cJSON_Delete(json);
            return totalSize;
        }
        printf("data: %s\n", *token);
    }
    cJSON_Delete(json); // 清理JSON对象
    return totalSize;
}

Status ClientAuth(char **token)
{
    char tokenString[32];
    char deviceId[16] = "9527";
    char requestUrl[128];

    CURL *curl;
    CURLcode res;
    curl_global_init(CURL_GLOBAL_DEFAULT);
    curl = curl_easy_init();

    sprintf(requestUrl, "%s://%s:%s%s%s", CONTRACT, HOST, PORT, AUTH_URL, deviceId);

    printf("request url = %s\n", requestUrl);

    if (curl)
    {
        curl_easy_setopt(curl, CURLOPT_URL, requestUrl);
        // 设置客户端证书路径
        curl_easy_setopt(curl, CURLOPT_SSLCERT, CLIENT_CERT_PATH);
        // 设置客户端私钥路径
        curl_easy_setopt(curl, CURLOPT_SSLKEY, CLIENT_KEY_PATH);
        // 设置 CA 证书路径
        curl_easy_setopt(curl, CURLOPT_CAINFO, CLIENT_ROOT_CERT_PATH);
        /* 可选：设置证书验证 */
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 1L);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 2L);
        // 设置回调函数以接收响应数据
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, token);
        /* 执行请求 */
        res = curl_easy_perform(curl);

        /* 检查错误 */
        if (res != CURLE_OK)
        {
            fprintf(stderr, "curl_easy_perform() failed: %s\n", curl_easy_strerror(res));
            curl_easy_cleanup(curl);
            curl_global_cleanup();
            return -1;
        }
        /* 清理 */
        curl_easy_cleanup(curl);
    }
    curl_global_cleanup();
    return 0;
}
