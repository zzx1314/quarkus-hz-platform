#include "ClientAuth.h"
#include "Common.h"
#include "Report.h"

int main(void)
{
	char *token = NULL;
	char *deviceId = "00022025C0600001";

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
