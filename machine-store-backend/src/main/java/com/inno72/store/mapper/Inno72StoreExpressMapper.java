package com.inno72.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72StoreExpress;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreExpressMapper extends Mapper<Inno72StoreExpress> {

	int insertStoreExpressList(@Param("list") List<Inno72StoreExpress> list);
}