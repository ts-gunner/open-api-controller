package com.forty.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.forty.model.entity.RoleAssignment;
import com.forty.model.vo.RoleAssignmentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleAssignmentMapper extends BaseMapper<RoleAssignment> {
    List<RoleAssignmentVO> getRoleAssignmentList(
            IPage<RoleAssignmentVO> page,
            @Param(Constants.WRAPPER) QueryWrapper<RoleAssignment> queryWrapper
    );

}
