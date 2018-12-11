package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.vo.ShopsVo;
import com.inno72.Interact.vo.TreeVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractShopsMapper extends Mapper<Inno72InteractShops> {

	ShopsVo selectInteractShopsById(Map<String, Object> params);

	List<ShopsVo> selectMerchantShops(Map<String, Object> params);

	List<TreeVo> selectMerchantShopsTree(Map<String, Object> params);

	List<Map<String, Object>> getShopsList(Map<String, Object> params);

	int insertInteractShopsList(@Param("list") List<Inno72InteractShops> list);
}