package com.forty.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInterfaceAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Integer interfaceId;

    private Integer remainCount;

    private Integer status;
}
