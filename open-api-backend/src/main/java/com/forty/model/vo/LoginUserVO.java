package com.forty.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
public class LoginUserVO implements Serializable {

    /**
     * 用户id，用户量不多，不需要考虑分库分表
     */
    private Long id;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 微信开放平台id
     */
    private String unionId;
    /**
     * 公众号openid
     */
    private String mpOpenId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}