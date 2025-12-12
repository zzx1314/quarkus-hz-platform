#include <stdio.h>
#include <string.h>
#include <curl/curl.h>

int main(void)
{
    CURL *curl;
    CURLcode res;

    // 初始化libcurl
    curl = curl_easy_init();
    if (curl)
    {
        // 设置URL
        curl_easy_setopt(curl, CURLOPT_URL, "https://agent:4433/api/client/register");

        // 设置POST数据（JSON格式）
        const char *postFields = "{"
                                 "\"deviceId\": \"9527\","
                                 "\"deviceIp\": \"192.168.137.101\","
                                 "\"os\": \"CleverOS\","
                                 "\"osVersion\": \"2.0\","
                                 "\"arch\": \"aarch64\""
                                 "}";

        // 设置HTTP头信息
        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Content-Type: application/json");
        headers = curl_slist_append(headers, "Authorization: 24956ccd61134f93b07dea3856a1a061");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        // 设置POST数据
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, postFields);

        curl_easy_setopt(curl, CURLOPT_CAINFO, "/home/zhangzexin/IdeaProjects/hz_server/src/main/client/cer/rootca.crt");
        // 执行请求
        res = curl_easy_perform(curl);

        // 检查错误
        if (res != CURLE_OK)
        {
            fprintf(stderr, "curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
        }

        // 清理
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }

    return 0;
}
