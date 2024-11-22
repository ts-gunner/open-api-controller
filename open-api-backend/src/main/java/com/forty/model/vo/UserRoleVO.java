package com.forty.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class UserRoleVO implements Serializable {
    private int roleId;

    private String roleName;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 22311L;
}
