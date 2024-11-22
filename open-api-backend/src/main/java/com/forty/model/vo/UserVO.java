package com.forty.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class UserVO implements Serializable {
    private Long id;

    private String userAccount;

    private String password;

    private String unionId;

    private String mpOpenId;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private Date createTime;

    private Date updateTime;
    private static final long serialVersionUID = 1L;
}
