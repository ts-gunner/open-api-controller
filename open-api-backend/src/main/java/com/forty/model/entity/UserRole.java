package com.forty.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("user_role")
public class UserRole implements Serializable {

    @TableId(value = "role_id", type= IdType.AUTO)
    private Integer roleId;

    @TableField("role_name")
    private String roleName;

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
