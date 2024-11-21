package com.forty.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 112313358L;

    private String userAccount;

    private String password;
}
