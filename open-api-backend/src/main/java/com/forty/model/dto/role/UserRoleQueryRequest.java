package com.forty.model.dto.role;

import com.forty.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleQueryRequest extends PageRequest implements Serializable {

    private String roleName;

}
