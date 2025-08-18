package org.hzai.exception;

import org.hzai.util.R;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        // 打印日志
        exception.printStackTrace();

        // 返回统一 JSON 格式
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(R.failed("SYSTEM_ERROR", exception.getMessage()))
                .build();
    }
}

