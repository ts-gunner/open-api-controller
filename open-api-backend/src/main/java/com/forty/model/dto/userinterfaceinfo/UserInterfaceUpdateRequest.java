package com.forty.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInterfaceUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long userId;

    private Integer interfaceId;

    private Integer totalCount;

    private Integer remainCount;

    private Integer status;
}
