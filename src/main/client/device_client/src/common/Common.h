#ifndef COMMON_H
#define COMMON_H

#include <stdio.h>

#define CONTRACT "https"
#define HOST "agent"
#define PORT "443"

#define AUTH_URL "/api/client/clientAuth/"

// 客户端证书路径
#define CLIENT_CERT_PATH "/home/zhangzexin/IdeaProjects/hz_server/src/main/client/cer/client.crt"
#define CLIENT_KEY_PATH "/home/zhangzexin/IdeaProjects/hz_server/src/main/client/cer/client.key"
#define CLIENT_ROOT_CERT_PATH "/home/zhangzexin/IdeaProjects/hz_server/src/main/client/cer/rootca.crt"

// 状态码
typedef int Status;

#endif
