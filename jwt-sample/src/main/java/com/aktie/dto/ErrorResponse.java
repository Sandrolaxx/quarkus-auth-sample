package com.aktie.dto;

public record ErrorResponse(String error, String date, String errorCode, String errorMsg) {
}
