#include "Report.h"

char deviceIdParam[64] = "";
static struct lws *wsi = NULL;

void sighdl(int sig)
{
    lwsl_notice("%d traped", sig);
    exit_sig = 1;
}

char *websocket_send_data()
{
    char *str;

    cJSON *root = cJSON_CreateObject();
    if (root == NULL)
    {
        fprintf(stderr, "Failed to create JSON object\n");
        return NULL;
    }

    cJSON_AddItemToObject(root, "type", cJSON_CreateString("heartbeat"));
    cJSON_AddItemToObject(root, "deviceId", cJSON_CreateString(deviceIdParam));
    cJSON_AddItemToObject(root, "status", cJSON_CreateString("success"));
    str = cJSON_Print(root);
    printf("sendMessage = %s\n", str);
    cJSON_Delete(root);

    return str;
}

int callback(struct lws *wsi, enum lws_callback_reasons reason, void *user, void *in, size_t len)
{
    SessionData *data = (SessionData *)user;
    switch (reason)
    {
    case LWS_CALLBACK_CLIENT_ESTABLISHED: // 连接时的回调
        lwsl_notice("Connected to server ok!\n");
        start_send_binary(data, "/home/zhangzexin/test.jpg"); // 指定文件路径
        lws_callback_on_writable(wsi);
        break;
    case LWS_CALLBACK_CLIENT_RECEIVE: // 接受数据的回调
        lwsl_notice("Rx: %s\n", (char *)in);
        break;
    case LWS_CALLBACK_CLIENT_WRITEABLE:
        unsigned char *p = &data->buf[LWS_PRE];
        // 1️ 发送 file_start JSON
        if (data->fileStartJson) {
            char *str = cJSON_Print(data->fileStartJson);
            data->len = snprintf((char *)p, MAX_PAYLOAD_SIZE, "%s", str);
            lws_write(wsi, p, data->len, LWS_WRITE_TEXT);
            free(str);
            cJSON_Delete(data->fileStartJson);
            data->fileStartJson = NULL;
        }
        // 2️ 发送二进制文件
        if (data->sending_binary && data->fp) {
            size_t n = fread(p, 1, MAX_PAYLOAD_SIZE, data->fp);
            if (n > 0) {
                lws_write(wsi, p, n, LWS_WRITE_BINARY);
                lws_callback_on_writable(wsi);  // 下一次回调继续发送下一片
                return 0; // 立即返回，等待下一次 WRITEABLE
            } else {
                fclose(data->fp);
                data->fp = NULL;
                data->sending_binary = 0;
                // 立即发送 file_end
                if (data->fileEndJson) {
                    lws_callback_on_writable(wsi); // 确保回调触发 file_end 发送
                }
            }
            break;
        }

        // 3️ 发送 file_end JSON
        if (data->fileEndJson) {
            char *str = cJSON_Print(data->fileEndJson);
            data->len = snprintf((char *)p, MAX_PAYLOAD_SIZE, "%s", str);
            lws_write(wsi, p, data->len, LWS_WRITE_TEXT);
            free(str);
            cJSON_Delete(data->fileEndJson);
            data->fileEndJson = NULL;
            // 发送完 file_end 后，再触发一次 WRITEABLE 以保证心跳继续
            lws_callback_on_writable(wsi);
        }

        // 4️ 否则发送心跳
        if (!data->fileStartJson && !data->sending_binary && !data->fileEndJson) {
            char *json = websocket_send_data();
            if (json) {
                data->len = snprintf((char *)p, MAX_PAYLOAD_SIZE, "%s", json);
                lws_write(wsi, p, data->len, LWS_WRITE_TEXT);
                free(json);
            }
        }

        lws_set_timer_usecs(wsi, 60 * LWS_USEC_PER_SEC); // 下次心跳
        break;

    case LWS_CALLBACK_TIMER:
        lws_set_timeout(wsi, 1, 60);
        lws_callback_on_writable(wsi);
        break;
    default:
        break;
    }
    return 0;
}

struct lws_protocols protocols[] = {
    {
        "wss",
        callback,
        sizeof(SessionData),
        MAX_PAYLOAD_SIZE,
    },
    {
        NULL, NULL, 0 // 结束标记
    }};

void start_send_binary(SessionData *data, const char *filename)
{
    data->fp = fopen(filename, "rb");
    if (!data->fp) {
        lwsl_err("Failed to open file %s\n", filename);
        return;
    }
    data->sending_binary = 1;

    // 生成唯一 streamId
    char streamId[32];
    sprintf(streamId, "stream-%ld", time(NULL));

    // file_start JSON
    data->fileStartJson = cJSON_CreateObject();
    cJSON_AddItemToObject(data->fileStartJson, "type", cJSON_CreateString("file_start"));
    cJSON_AddItemToObject(data->fileStartJson, "streamId", cJSON_CreateString(streamId));
    cJSON_AddItemToObject(data->fileStartJson, "fileName", cJSON_CreateString(filename));

    // file_end JSON（暂不发送，WRITEABLE 回调处理）
    data->fileEndJson = cJSON_CreateObject();
    cJSON_AddItemToObject(data->fileEndJson, "type", cJSON_CreateString("file_end"));
    cJSON_AddItemToObject(data->fileEndJson, "streamId", cJSON_CreateString(streamId));
}


Status Report(char *token, char *deviceId)
{
    sprintf(deviceIdParam, "%s", deviceId);

    signal(SIGTERM, sighdl);

    char websocket_url[128] = "";
    int port = 4433;
    char addr_port[256] = {0};
    sprintf(addr_port, "%s:%u", "agent", port & 65535);

    sprintf(websocket_url, "/api/notice/%s/%s", deviceId, token);

    struct lws_context_creation_info ctx_info = {0};
    ctx_info.port = CONTEXT_PORT_NO_LISTEN;
    ctx_info.iface = NULL;
    ctx_info.protocols = protocols;
    ctx_info.gid = -1;
    ctx_info.uid = -1;
    ctx_info.options = LWS_SERVER_OPTION_DO_SSL_GLOBAL_INIT;
    ctx_info.client_ssl_ca_filepath = "/home/zhangzexin/IdeaProjects/hz_server/src/main/client/cer/rootca.crt";

    struct lws_context *context = lws_create_context(&ctx_info);
    struct lws_client_connect_info conn_info = {0};
    conn_info.context = context;
    conn_info.address = "agent";
    conn_info.port = port;
    conn_info.path = websocket_url;
    conn_info.ssl_connection = LCCSCF_USE_SSL;
    conn_info.origin = addr_port;
    conn_info.host = addr_port;
    conn_info.protocol = protocols[0].name;

    wsi = lws_client_connect_via_info(&conn_info);

    while (!exit_sig)
    {
        lws_service(context, 1000);
    }
    lws_context_destroy(context);

    return 0;
}
