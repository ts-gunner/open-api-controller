package com.forty.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleAssignmentVO implements Serializable {

    private int roleId;

    private String roleName;

    private Long userId;

    private String userAccount;
}
