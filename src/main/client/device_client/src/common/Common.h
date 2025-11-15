#ifndef COMMON_H
#define COMMON_H

#include <stdio.h>

#define CONTRACT "https"
#define HOST "agent"
#define PORT "443"

#define AUTH_URL "/api/device/client/clientAuth/"

// 客户端证书路径
#define CLIENT_CERT_PATH "/opt/hz/middleware/certs/client/client.crt"
#define CLIENT_KEY_PATH "/opt/hz/middleware/certs/client/client.key"
#define CLIENT_ROOT_CERT_PATH "/opt/hz/middleware/certs/client/rootca.crt"

// 状态码
typedef int Status;

#endif
