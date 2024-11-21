package com.forty.model.dto.user;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 3123114251L;

    private String userAccount;

    private String password;

    private String checkPassword;
}
