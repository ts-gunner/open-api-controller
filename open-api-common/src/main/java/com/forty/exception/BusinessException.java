package com.forty.exception;


import com.forty.common.CodeStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(CodeStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public BusinessException(CodeStatus status, String message) {
        super(message);
        this.code = status.getCode();
    }

}
