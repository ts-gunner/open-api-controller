package com.forty.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserVO implements Serializable {
    private Long id;

    private String userAccount;

    private String unionId;

    private String mpOpenId;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private List<String> roles;

    private Date createTime;

    private Date updateTime;
    private static final long serialVersionUID = 1L;
}
