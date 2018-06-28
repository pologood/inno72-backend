package com.inno72.common;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface SelectByPageMapper<T> {

	/**
	 * 查询全部结果
	 *
	 * @return
	 */
	@SelectProvider(type = CustomSelectProvider.class, method = "dynamicSQL")
	List<T> selectByPage();
}
