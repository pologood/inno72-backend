package com.inno72.machine.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Tag;

@org.apache.ibatis.annotations.Mapper
public interface Inno72TagMapper extends Mapper<Inno72Tag> {

	List<String> selectAllList();

	List<Inno72Tag> selectTagByPage(Map<String, Object> params);

	int insertTagList(@Param("list") List<Inno72Tag> list);
}