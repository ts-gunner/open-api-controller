package com.forty.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("interface_info")
public class InterfaceInfo implements Serializable {

    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口描述
     */
    private String description;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 请求头
     */
    @TableField("request_header")
    private String requestHeader;
    /**
     * 响应头
     */
    @TableField("response_header")
    private String responseHeader;
    /**
     * 接口状态 0 - 关闭  1 - 开启
     */
    private boolean status;

    /**
     * 创建人
     */
    @TableField("user_id")
    private int userId;

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
