#include <stdio.h>
#include <curl/curl.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include "cJSON.h"
#include <libwebsockets.h>
#include <string.h>
#include <time.h>
#include <signal.h>  

char token[128] = "6e56419efa9a4a69bd67f001b4948f30";
char id[64] = "9527";

static volatile int exit_sig = 0;  
#define MAX_PAYLOAD_SIZE  10 * 1024  

void sighdl(int sig) {  
    lwsl_notice("%d traped", sig);  
    exit_sig = 1;  
}  
struct session_data {  
    int msg_count;  
    unsigned char buf[LWS_PRE + MAX_PAYLOAD_SIZE];  
    int len;  
};  


char *websocket_send_data()
{
    char *str;
    
    cJSON *root = cJSON_CreateObject();
    if (root == NULL) {
        fprintf(stderr, "Failed to create JSON object\n");
        return NULL;
    }

    cJSON_AddItemToObject(root, "type", cJSON_CreateString("heartbeat"));
    cJSON_AddItemToObject(root, "deviceId", cJSON_CreateString(id));
    str = cJSON_Print(root);
    printf("sendMessage = %s\n", str);
    cJSON_Delete(root);

    return str;
}

int callback(struct lws *wsi, enum lws_callback_reasons reason, void *user, void *in, size_t len) 
{  
    struct session_data *data = (struct session_data *)user;
    switch (reason) {  
        case LWS_CALLBACK_CLIENT_ESTABLISHED:   // 连接时的回调  
            lwsl_notice("Connected to server ok!\n"); 
	    lws_callback_on_writable(wsi);
            break;  
        case LWS_CALLBACK_CLIENT_RECEIVE:       // 接受数据的回调  
            lwsl_notice("Rx: %s\n", (char *)in);
            break;
    	case LWS_CALLBACK_CLIENT_WRITEABLE:
            memset(data->buf, 0, sizeof(data->buf));  
            char *msg = (char *)&data->buf[LWS_PRE];
	    char *keep_str = websocket_send_data();
            data->len = sprintf(msg, keep_str, ++data->msg_count);
	    lws_write(wsi, &data->buf[LWS_PRE], data->len, LWS_WRITE_TEXT);
            free(keep_str);
	    lws_set_timer_usecs(wsi, 20 * LWS_USEC_PER_SEC);
    	    lws_set_timeout(wsi, 1, 60);
            break;
        case LWS_CALLBACK_TIMER:
            lws_callback_on_writable(wsi);
	    break;
	default:
	    break;
    }  
    return 0;  
}
struct lws_protocols protocols[] = {  
    {  
        "wss", callback, sizeof(struct session_data), MAX_PAYLOAD_SIZE,
    },  
    {  
        NULL, NULL, 0 // 结束标记  
    }  
};  

int websocket_handle() 
{  
    signal(SIGTERM, sighdl);  
    
    char websocket_url[128] = "";
    int port = 4433;
    char addr_port[256] = { 0 };
    sprintf(addr_port, "%s:%u", "huazhi", port & 65535 );

    sprintf(websocket_url, "/notice/%s/%s", id, token);
 
    struct lws_context_creation_info ctx_info = { 0 };  
    ctx_info.port = CONTEXT_PORT_NO_LISTEN;
    ctx_info.iface = NULL; 
    ctx_info.protocols = protocols;
    ctx_info.gid = -1;
    ctx_info.uid = -1;   
    ctx_info.options = LWS_SERVER_OPTION_DO_SSL_GLOBAL_INIT;
    ctx_info.client_ssl_ca_filepath = "/opt/hz/middleware/certs/client/rootca.crt";

    struct lws_context *context = lws_create_context(&ctx_info);  
    struct lws_client_connect_info conn_info = { 0 };  
    conn_info.context = context;  
    conn_info.address = "huazhi";
    conn_info.port = port;
    conn_info.path = websocket_url; 
    conn_info.ssl_connection = LCCSCF_USE_SSL;
    conn_info.origin = addr_port;  
    conn_info.host = addr_port;  
    conn_info.protocol = protocols[0].name;  

    struct lws *wsi = lws_client_connect_via_info(&conn_info);  

    while (!exit_sig) {
        lws_service(context, 1000);
   }
    lws_context_destroy(context);  

    return 0;  
}  


int main()
{
	websocket_handle();
	return 0;
}
