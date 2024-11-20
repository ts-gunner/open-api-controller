package com.forty.common;

public enum CodeStatus {

    SUCCESS(200, "Success"),
    PARAM_ERROR(400, "Param Error"),
    NOT_LOGIN(401, "Not Login"),
    NO_AUTH(40100, "No Auth"),
    TOKEN_UNAVAILABLE(40101, "Token Unavailable"),
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
