package com.forty.model.dto.roleassignment;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleAssignmentQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleName;

    private String userAccount;
}
