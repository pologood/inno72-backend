package com.inno72.Interact.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72InteractGoodsMapper extends Mapper<Inno72InteractGoods> {

	InteractGoodsVo selectInteractGoodsById(String id);

	List<InteractGoodsVo> selectGoods(Map<String, Object> pm);
}