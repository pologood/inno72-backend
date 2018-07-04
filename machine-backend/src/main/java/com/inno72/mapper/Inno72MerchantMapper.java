package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Merchant;
import com.inno72.vo.Inno72MerchantVo;

public interface Inno72MerchantMapper extends Mapper<Inno72Merchant> {
	
	List<Inno72MerchantVo> selectByPage(Inno72MerchantVo params);
}