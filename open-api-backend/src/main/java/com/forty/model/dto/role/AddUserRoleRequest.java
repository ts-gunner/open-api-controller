package com.forty.model.dto.role;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddUserRoleRequest implements Serializable {

    private int roleId;

    private long userId;

    private static final long serialVersionUID = 112313358L;
}
