#ifndef REPORT_H
#define REPORT_H

#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <libwebsockets.h>
#include <time.h>
#include <signal.h>
#include "Common.h"
#include <cjson/cJSON.h>

#define MAX_PAYLOAD_SIZE 10 * 1024

static volatile int exit_sig = 0;

typedef struct
{
    int msg_count;
    unsigned char buf[LWS_PRE + MAX_PAYLOAD_SIZE];
    int len;

    /* 新增二进制文件支持 */
    FILE *fp;               // 文件指针
    int sending_binary;     // 是否在发送二进制
    cJSON *fileStartJson;  // 新增：存储 file_start JSON
    cJSON *fileEndJson;    // 新增：存储 file_end JSON
} SessionData;

Status Report(char *token, char *deviceId);


void start_send_binary(SessionData *data, const char *filename);


#endif
