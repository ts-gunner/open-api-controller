package com.forty.exception;

import com.forty.common.CodeStatus;

public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, final String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(CodeStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public BusinessException(CodeStatus status, String message) {
        super(message);
        this.code = status.getCode();
    }

    public int getCode() {
        return code;
    }
}
