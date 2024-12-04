package com.forty.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserInterfaceVO implements Serializable {

    private Integer interfaceId;

    private Long userId;

    private String name;

    private String description;

    private String url;

    private String method;

    private Long totalCalls;

    private Integer totalCount;

    private Integer remainCount;

    private static final long serialVersionUID = 1213412322L;
}
