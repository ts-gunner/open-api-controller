package com.forty.model.dto.interfaceinfo;

import com.forty.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Integer interfaceId;

    private String interfaceName;

    private String method;

    private String userAccount;

    private Boolean status;

    private static final long serialVersionUID = 1L;
}
