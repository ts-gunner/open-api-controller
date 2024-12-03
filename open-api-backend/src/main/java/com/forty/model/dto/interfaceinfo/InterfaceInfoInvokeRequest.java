package com.forty.model.dto.interfaceinfo;


import com.forty.model.enums.RequestBodyTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private String interfaceId;

    private String bodyType;

    private String userRequestHeader;

    private String userRequestParams;

    private static final long serialVersionUID = 156456849L;
}
