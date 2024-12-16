package com.forty.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TokenData implements Serializable {
    private Long userId;
    private String userAccount;
    private List<String> roles;

    private static final long serialVersionUID = 124311214L;

}
