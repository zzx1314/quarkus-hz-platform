#include "Report.h"

char deviceIdParam[64] = "";
static struct lws *wsi = NULL;

void sighdl(int sig)
{
    lwsl_notice("%d traped", sig);
    exit_sig = 1;
}

void send_message(struct lws *wsi, const char *message)
{
    size_t len = strlen(message);
    unsigned char *buf = malloc(LWS_SEND_BUFFER_PRE_PADDING + len + LWS_SEND_BUFFER_POST_PADDING);
    if (buf == NULL)
    {
        lwsl_err("Failed to allocate memory for message\n");
        return;
    }
    memcpy(&buf[LWS_SEND_BUFFER_PRE_PADDING], message, len);
    lws_write(wsi, &buf[LWS_SEND_BUFFER_PRE_PADDING], len, LWS_WRITE_TEXT);
    free(buf);
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
        lws_callback_on_writable(wsi);
        break;
    case LWS_CALLBACK_CLIENT_RECEIVE: // 接受数据的回调
        lwsl_notice("Rx: %s\n", (char *)in);
        break;
    case LWS_CALLBACK_CLIENT_WRITEABLE:
        memset(data->buf, 0, sizeof(data->buf));
        char *msg = (char *)&data->buf[LWS_PRE];
        char *keep_str = websocket_send_data();
        data->len = sprintf(msg, keep_str, ++data->msg_count);
        lws_write(wsi, &data->buf[LWS_PRE], data->len, LWS_WRITE_TEXT);
        free(keep_str);
        lws_set_timer_usecs(wsi, 60 * LWS_USEC_PER_SEC);
        lws_set_timeout(wsi, 1, 60);
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
