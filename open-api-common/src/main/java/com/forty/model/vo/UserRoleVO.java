package com.forty.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRoleVO implements Serializable {
    private int roleId;

    private String roleName;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 22311L;
}
