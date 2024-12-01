package com.forty.common;

public enum CodeStatus {

    SUCCESS(200, "Success"),
    FAIL(40001, "Fail"),
    PARAM_ERROR(400, "Param Error"),
    DATA_NOT_EXIST(40301, "Data Not Exist"),
    NOT_LOGIN(401, "Not Login"),
    NO_AUTH(40100, "No Auth"),
    SIGN_AUTH_FAILED(401001, "Sign Auth Failed"),
    AUTH_ROLE_FAILED(401002, "Auth Role Failed"),
    TOKEN_UNAVAILABLE(40101, "Token Unavailable"),
    DB_ERROR(50001, "DB Error"),
    SYSTEM_ERROR(500, "System Error");

    private final int code;

    private final String message;

    CodeStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
