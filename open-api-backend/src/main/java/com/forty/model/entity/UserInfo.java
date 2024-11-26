package com.forty.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_info")
public class UserInfo implements Serializable {

    /**
     * 用户id，用户量不多，不需要考虑分库分表
     */
    @TableId(type= IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    @TableField("user_account")
    private String userAccount;
    /**
     * 密码
     */
    private String password;
    /**
     * 微信开放平台id
     */
    @TableField("union_id")
    private String unionId;
    /**
     * 公众号openid
     */
    @TableField("mp_open_id")
    private String mpOpenId;
    /**
     * 用户昵称
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户头像
     */
    @TableField("user_avatar")
    private String userAvatar;
    /**
     * 用户简介
     */
    @TableField("user_profile")
    private String userProfile;
    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
