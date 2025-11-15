#include <stdio.h>
#include <curl/curl.h>

int main(void)
{
  CURL *curl;
  CURLcode res;

  curl_global_init(CURL_GLOBAL_DEFAULT);

  curl = curl_easy_init();
  if(curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "https://huazhi:4433/api/device/oBusLogs/reportLog");

    // 设置请求头
    struct curl_slist *headers = NULL;
    headers = curl_slist_append(headers, "Content-Type: multipart/form-data");
    headers = curl_slist_append(headers, "Authorization: 51f5fb387a7f4df0b7d5e5b77bc16c76");
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(curl, CURLOPT_CAINFO, "/opt/hz/middleware/certs/client/rootca.crt");

    // 构造表单数据
    struct curl_httppost *formpost = NULL;
    struct curl_httppost *lastptr = NULL;

    // 添加文件字段
    curl_formadd(&formpost,
                 &lastptr,
                 CURLFORM_COPYNAME, "file",
                 CURLFORM_FILE, "/root/test.log",
                 CURLFORM_END);

    // 添加 devId 字段
    const char *devId = "9527"; // 例如
    curl_formadd(&formpost,
                 &lastptr,
                 CURLFORM_COPYNAME, "devId",
                 CURLFORM_COPYCONTENTS, devId,
                 CURLFORM_END);

    // 设置表单数据
    curl_easy_setopt(curl, CURLOPT_HTTPPOST, formpost);

    // 执行请求
    res = curl_easy_perform(curl);

    // 检查错误
    if(res != CURLE_OK)
      fprintf(stderr, "curl_easy_perform() failed: %s\n",
              curl_easy_strerror(res));

    // 清理
    curl_easy_cleanup(curl);
    curl_formfree(formpost);
    curl_slist_free_all(headers);
  }

  curl_global_cleanup();

  return 0;
}
