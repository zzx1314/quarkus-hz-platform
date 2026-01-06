#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <arpa/inet.h>
#include <unistd.h>

#define SERVER_IP "127.0.0.1"
#define SERVER_PORT 9000

#define FRAME_HEADER_LEN 20
#define MAGIC 0xCAFEBABE

typedef enum {
    FRAME_INIT = 0,
    FRAME_MESSAGE = 1,
    FRAME_FILE_DATA = 2,
    FRAME_FINISH = 3,
    FRAME_ERROR = 4
} FrameType;

// 简单写一个 Header 打包函数（大端）
void pack_header(uint8_t *header, FrameType type, uint32_t body_len, uint64_t sequence) {
    uint32_t magic = htonl(MAGIC);
    uint32_t length = htonl(body_len);
    uint64_t seq = htobe64(sequence); // big endian

    memcpy(header, &magic, 4);
    header[4] = 1; // version
    header[5] = (uint8_t)type;
    header[6] = 0; // flags
    header[7] = 0; // reserved
    memcpy(header + 8, &length, 4);
    memcpy(header + 12, &seq, 8);
}

// 发送一个 Frame（Header + Body）
int send_frame(int sockfd, FrameType type, const uint8_t *body, uint32_t body_len, uint64_t seq) {
    uint8_t header[FRAME_HEADER_LEN];
    pack_header(header, type, body_len, seq);

    // 先发送 header
    if (write(sockfd, header, FRAME_HEADER_LEN) != FRAME_HEADER_LEN) {
        perror("write header");
        return -1;
    }

    // 再发送 body
    if (body_len > 0) {
        if (write(sockfd, body, body_len) != body_len) {
            perror("write body");
            return -1;
        }
    }

    return 0;
}

int main() {
    int sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) { perror("socket"); return -1; }

    struct sockaddr_in serv_addr;
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(SERVER_PORT);
    inet_pton(AF_INET, SERVER_IP, &serv_addr.sin_addr);

    if (connect(sockfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0) {
        perror("connect"); return -1;
    }

    printf("Connected to server.\n");

    uint64_t seq = 1;

    // 1 发送 INIT Frame
    const char *init_json = "{\"taskType\":\"FILE_UPLOAD\",\"taskId\":\"123456\",\"taskMeta\":{\"fileName\":\"test.txt\",\"fileSize\":1024}}";
    send_frame(sockfd, FRAME_INIT, (uint8_t*)init_json, strlen(init_json), seq++);
    printf("INIT sent.\n");

    // 2 发送 FILE_DATA Frame（示例文件，写死几个字节）
    FILE *fp = fopen("test.txt", "rb");
    if (!fp) { perror("fopen"); return -1; }

    uint8_t buffer[1024];
    size_t n;
    while ((n = fread(buffer, 1, sizeof(buffer), fp)) > 0) {
        send_frame(sockfd, FRAME_FILE_DATA, buffer, n, seq++);
        printf("FILE_DATA sent: %zu bytes\n", n);
    }
    fclose(fp);

    // 3 发送 FINISH Frame
    const char *finish_json = "{\"success\":true,\"bytesTransferred\":1024}";
    send_frame(sockfd, FRAME_FINISH, (uint8_t*)finish_json, strlen(finish_json), seq++);
    printf("FINISH sent.\n");

    close(sockfd);
    printf("Connection closed.\n");
    return 0;
}
