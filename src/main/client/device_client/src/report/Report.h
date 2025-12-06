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
} SessionData;

Status Report(char *token, char *deviceId);

#endif
