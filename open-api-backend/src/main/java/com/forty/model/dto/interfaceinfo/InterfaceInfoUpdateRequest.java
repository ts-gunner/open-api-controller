package com.forty.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoUpdateRequest implements Serializable {
    private static final long serialVersionUID = 155646L;
    private Integer interfaceId;
    private String interfaceName;
    private String interfaceDescription;
    private Boolean APIStatus;
    private String method;
    private String url;
    private String requestHeader;
    private String responseHeader;
    private String requestBody;
    private String responseBody;
}
