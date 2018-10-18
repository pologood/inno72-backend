package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.vo.Inno72InteractMachineGoodsVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractMachineGoodsMapper extends Mapper<Inno72InteractMachineGoods> {

	int insertInteractMachineGoodsList(@Param("list") List<Inno72InteractMachineGoodsVo> list);

	List<Inno72InteractMachineGoodsVo> selectMachineGoods(Map<String, Object> pm);

}