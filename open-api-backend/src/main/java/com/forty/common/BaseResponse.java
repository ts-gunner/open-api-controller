package com.forty.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code = CodeStatus.SUCCESS.getCode();

    private String msg = "successfully";

    private T data;

    public BaseResponse() {}
    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public BaseResponse(int code, String msg) {
        this(code, msg, null);
    }

    public BaseResponse(T data) {
        this(CodeStatus.SUCCESS.getCode(), CodeStatus.SUCCESS.getMessage(), data);
    }
    public  BaseResponse(CodeStatus codeStatus) { this(codeStatus.getCode(), codeStatus.getMessage(), null); }
}
