package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.vo.Inno72NeedExportStore;
import com.inno72.Interact.vo.Merchant;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractMerchantMapper extends Mapper<Inno72InteractMerchant> {

	List<Merchant> selectMerchantByInteractId(@Param("interactId") String interactId);

	int insertInteractMerchantList(@Param("list") List<Inno72InteractMerchant> list);

	List<Inno72NeedExportStore> findMachineSellerId(Map<String, Object> params);

}