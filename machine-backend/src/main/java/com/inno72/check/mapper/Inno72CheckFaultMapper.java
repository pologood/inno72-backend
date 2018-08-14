package com.inno72.check.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.vo.Inno72CheckFaultVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckFaultMapper extends Mapper<Inno72CheckFault> {

	List<Inno72CheckFault> selectByPage(Map<String, Object> params);

	Inno72CheckFaultVo selectFaultDetail(Map<String, Object> params);

	List<Inno72CheckUser> getMachineUserList(Map<String, Object> params);

}