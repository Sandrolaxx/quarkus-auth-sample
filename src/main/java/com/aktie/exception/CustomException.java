package com.aktie.exception;

public class CustomException extends RuntimeException {

    private String errorCode = "-1";

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(EnumErrorCode error) {
        super(error.getErro());
        this.errorCode = error.getKey();
    }

    public String getErrorCode() {
        return errorCode;
    }

}
