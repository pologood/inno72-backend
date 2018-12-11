package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72MerchantUser;
import com.inno72.project.vo.Inno72MerchantUserVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MerchantUserMapper extends Mapper<Inno72MerchantUser> {
	List<Inno72MerchantUserVo> selectByPage(Map<String, Object> params);

	int selectByLoginName(String loginName);

	int selectMerchantId(String s);
}