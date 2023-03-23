package com.example.mongodb.common.result;



public enum ResultCode {
    SUCCESS(200),
    FAIL(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    SYSTEM_ERROR(500),
    SERVICE_ERROR(501);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
