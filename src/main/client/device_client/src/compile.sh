 gcc -g -o main -Icommon -Iauth -Ireport  common/Common.h auth/ClientAuth.h report/Report.h auth/ClientAuth.c report/Report.c  main.c -lcurl -lcjson -lwebsockets -lpthread
