package com.forty.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("secret_info")
public class SecretInfo implements Serializable {

    @TableId(type= IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Long userId;

    @TableField("secret_id")
    private String secretId;

    @TableField("secret_key")
    private String secretKey;

    private Boolean available;

    @TableField("create_time")
    private Date createTime;


    private static final long serialVersionUID = 1L;
}
