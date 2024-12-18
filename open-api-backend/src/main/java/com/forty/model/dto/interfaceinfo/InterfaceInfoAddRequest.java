package com.forty.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoAddRequest implements Serializable {

    private String interfaceName;

    private String description;

    private String url;

    private String method;

    private String requestHeader;

    private String responseHeader;

    private String requestBody;

    private String responseBody;

    private static final long serialVersionUID = 1L;
}
