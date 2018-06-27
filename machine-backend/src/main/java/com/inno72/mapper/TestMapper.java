package com.inno72.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.inno72.model.Test;

@Mapper
public interface TestMapper {

	Test test();

}
