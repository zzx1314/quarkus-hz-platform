#include <stdio.h>
#include <curl/curl.h>

int main(void) {
    CURL *curl;
    CURLcode res;
    curl_global_init(CURL_GLOBAL_DEFAULT);
    curl = curl_easy_init();
    if(curl) {
        curl_easy_setopt(curl, CURLOPT_URL, "https://huazhi:443/api/device/client/clientAuth/9527");
        // 设置客户端证书路径
        curl_easy_setopt(curl, CURLOPT_SSLCERT, "/opt/hz/middleware/certs/client/client.crt");
        // 设置客户端私钥路径
        curl_easy_setopt(curl, CURLOPT_SSLKEY, "/opt/hz/middleware/certs/client/client.key");
        // 设置 CA 证书路径
        curl_easy_setopt(curl, CURLOPT_CAINFO, "/opt/hz/middleware/certs/client/rootca.crt");
        /* 可选：设置证书验证 */
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 1L);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 2L);

        /* 执行请求 */
        res = curl_easy_perform(curl);
        printf("curl_easy_perform() result: %d\n", res);

        /* 检查错误 */
        if(res != CURLE_OK)
            fprintf(stderr, "curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));

        /* 清理 */
        curl_easy_cleanup(curl);
    }

    curl_global_cleanup();

    return 0;
}
