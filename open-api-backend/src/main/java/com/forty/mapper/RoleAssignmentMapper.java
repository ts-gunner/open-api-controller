package com.forty.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forty.model.entity.RoleAssignment;
import com.forty.model.vo.RoleAssignmentVO;

import java.util.List;

public interface RoleAssignmentMapper extends BaseMapper<RoleAssignment> {
    List<RoleAssignmentVO> getRoleAssignmentList();

}
