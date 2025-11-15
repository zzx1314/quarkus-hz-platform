#include <stdio.h>
#include <string.h>
#include <curl/curl.h>

int main(void) {
    CURL *curl;
    CURLcode res;

    // 初始化libcurl
    curl = curl_easy_init();
    if(curl) {
        // 设置URL
        curl_easy_setopt(curl, CURLOPT_URL, "https://huazhi:4433/api/device/client/register");

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
        headers = curl_slist_append(headers, "Authorization: 2edc7586dc2a4dc98851de755d9a6c72");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        // 设置POST数据
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, postFields);

        curl_easy_setopt(curl, CURLOPT_CAINFO, "/opt/hz/middleware/certs/client/rootca.crt");
        // 执行请求
        res = curl_easy_perform(curl);

        // 检查错误
        if(res != CURLE_OK){
            fprintf(stderr, "curl_easy_perform() failed: %s\n",
                            curl_easy_strerror(res));
        }

        // 清理
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }

    return 0;
}
