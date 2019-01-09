package com.inno72.check.mapper;


import java.util.Map;

import com.inno72.check.model.Inno72CheckGoodsNum;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckGoodsNumMapper extends Mapper<Inno72CheckGoodsNum> {

	Inno72CheckGoodsNum selectByparam(Map<String,Object> goodsMap);
}
