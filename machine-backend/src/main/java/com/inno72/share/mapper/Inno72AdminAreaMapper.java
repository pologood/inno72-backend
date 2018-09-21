package com.inno72.share.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.share.model.Inno72AdminArea;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AdminAreaMapper extends Mapper<Inno72AdminArea> {

	Inno72AdminArea selectByCode(String code);

	Inno72AdminArea selectMaxByParentCode(String parentCode);

	List<Inno72AdminArea> findByPage(Map<String,Object> map);

	List<Inno72AdminArea> findByParam(Map<String,Object> map);
}