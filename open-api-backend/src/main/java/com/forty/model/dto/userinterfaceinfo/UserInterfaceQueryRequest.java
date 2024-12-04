package com.forty.model.dto.userinterfaceinfo;


import com.forty.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Integer interfaceId;

    private Integer status;
}
