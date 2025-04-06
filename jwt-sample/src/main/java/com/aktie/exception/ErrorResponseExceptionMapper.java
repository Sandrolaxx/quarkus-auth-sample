package com.aktie.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jboss.resteasy.reactive.RestResponse.Status;

import com.aktie.dto.ErrorResponse;
import com.aktie.model.EnumErrorCode;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ErrorResponseExceptionMapper implements ExceptionMapper<CustomException> {

    @Override
    public Response toResponse(CustomException ex) {

        var pattern = DateTimeFormatter.ofPattern("dd-MM-YYYY hh:mm:ss");
        var date = LocalDateTime.now().format(pattern);
        int BAD_REQUEST = 400;
        int httpStatus;
        var error = EnumErrorCode.parseByKey(ex.getErrorCode());
        String errorPhrase = "Unknown error";
        String errorCode;

        
        if (error != null) {
            errorPhrase = Status.fromStatusCode(error.getHttpStatus()).getReasonPhrase();
            httpStatus = error.getHttpStatus();
            errorCode = error.getKey();
        } else {
            errorCode = ex.getErrorCode();
            httpStatus = BAD_REQUEST;
        }
        
        var exceptionResponse = new ErrorResponse(ex.getMessage(), date, errorCode, errorPhrase);

        return Response.status(httpStatus).entity(exceptionResponse).build();
    }

}
