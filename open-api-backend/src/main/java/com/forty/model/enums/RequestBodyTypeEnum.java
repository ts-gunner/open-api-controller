package com.forty.model.enums;

public enum RequestBodyTypeEnum {

    FormData("form-data"),
    FormUrlEncoded("x-www-form-url-encoded"),
    Raw("raw");


    private final String typeName;

    RequestBodyTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
