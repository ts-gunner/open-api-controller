package com.forty.model.dto.roleassignment;

import com.forty.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAssignmentQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleName;

    private String userAccount;
}
