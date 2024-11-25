package com.forty.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class TokenData {
    private Long userId;
    private String userAccount;
    private List<String> roles;

}
