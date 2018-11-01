package com.inno72.Interact.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.Interact.vo.MachineSellerVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractMerchantMapper extends Mapper<Inno72InteractMerchant> {

	List<InteractMerchantVo> selectMerchantByInteractId(@Param("interactId") String interactId);

	List<MachineSellerVo> findMachineSellerId1(@Param("interactId") String interactId);

	List<MachineSellerVo> findMachineSellerId2(@Param("interactId") String interactId);

}