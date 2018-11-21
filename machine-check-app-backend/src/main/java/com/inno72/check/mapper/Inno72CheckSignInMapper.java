package com.inno72.check.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.check.vo.SignVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckSignInMapper extends Mapper<Inno72CheckSignIn> {
	List<Inno72CheckSignIn> selectByMonth(Map<String, Object> map);

	List<MachineSignInVo> selectMachineSignList(Map<String,Object> map);

	List<SignVo> selectMonth(Map<String, Object> map);

	int selectDaySignInNum(Map<String, Object> map);
}
