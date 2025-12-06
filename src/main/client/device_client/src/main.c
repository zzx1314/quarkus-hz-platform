#include "ClientAuth.h"
#include "Common.h"
#include "Report.h"

int main(void)
{
	char *token = NULL;
	char *deviceId = "9527";

	// 请求认证
	ClientAuth(&token);
	if (token != NULL)
	{
		printf("token%s\n", token);
	}
	else
	{
		printf("failed to authen");
	}

	// 上报数据
	Report(token, deviceId);
	return 0;
}
