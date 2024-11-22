package com.forty.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("role_assignment")
public class RoleAssignment implements Serializable {

    @TableId(type= IdType.AUTO)
    private Long id;

    @TableField("role_id")
    private Integer roleId;

    @TableField("user_id")
    private long userId;

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
