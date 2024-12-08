package com.forty.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_interface_info")
public class UserInterfaceInfo implements Serializable {



    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 调用者id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 接口id
     */
    @TableField("interface_id")
    private Integer interfaceId;
    /**
     * 总调用次数
     */
    @TableField("total_count")
    private Integer totalCount;
    /**
     * 剩余调用次数
     */
    @TableField("remain_count")
    private Integer remainCount;
    /**
     * 状态， 0 - 禁用  1 - 正常
     */
    @TableField("status")
    private Integer status;

    /**
     * 逻辑删除， 0 - 未删除， 1 - 已删除
     */
    @TableField("is_delete")
    @TableLogic
    private boolean isDelete;
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
