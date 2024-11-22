package com.forty.model.dto.user;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserAddRequest implements Serializable {
    private String userAccount;
    private String password;
    private String username;
    private String userProfile;

    private static final long serialVersionUID = 1L;

}
