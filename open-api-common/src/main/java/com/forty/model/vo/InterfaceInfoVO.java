package com.forty.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class InterfaceInfoVO implements Serializable {

    private Integer id;

    private String name;

    private String description;

    private String url;

    private String method;

    private String requestHeader;

    private String responseHeader;

    private String requestBody;

    private String responseBody;

    private boolean status;

    private String userAccount;

    private Long totalCalls;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1213412322L;
}
