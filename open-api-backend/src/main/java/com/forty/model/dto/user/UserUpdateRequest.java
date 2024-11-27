package com.forty.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    private String userName;

    private String userProfile;

}
