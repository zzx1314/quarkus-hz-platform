package org.huazhi.exception;

import org.huazhi.util.R;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionHandler implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(R.failed("systemerror", exception.getMessage()))
                .build();
    }
}
