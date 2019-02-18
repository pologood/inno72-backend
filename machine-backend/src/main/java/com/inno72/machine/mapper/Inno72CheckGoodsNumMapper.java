package com.inno72.machine.mapper;


import java.util.List;
import java.util.Map;

import com.inno72.machine.model.Inno72CheckGoodsNum;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckGoodsNumMapper extends Mapper<Inno72CheckGoodsNum> {

	List<Inno72CheckGoodsNum> selectListByPage(Map<String,Object> map);
}
