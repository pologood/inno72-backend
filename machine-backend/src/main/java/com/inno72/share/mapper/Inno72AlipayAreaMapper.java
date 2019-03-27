package com.inno72.share.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.share.model.Inno72AlipayArea;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AlipayAreaMapper extends Mapper<Inno72AlipayArea> {

	int insertAlipayAreaList(@Param("list") List<Inno72AlipayArea> list);
}