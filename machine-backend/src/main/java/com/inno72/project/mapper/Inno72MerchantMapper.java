package com.inno72.project.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.vo.Inno72MerchantVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MerchantMapper extends Mapper<Inno72Merchant> {

	List<Inno72MerchantVo> selectByPage(Map<String, Object> params);

	int selectIsUseing(String id);

	int getCount(@Param("code") String code);
}