package com.forty.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.forty.model.entity.UserInterfaceInfo;
import com.forty.model.vo.UserInterfaceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceVO> getUserInterfaceVOList(@Param(Constants.WRAPPER) QueryWrapper<UserInterfaceVO> wrapper);
}
