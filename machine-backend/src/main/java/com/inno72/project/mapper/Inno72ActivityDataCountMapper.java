package com.inno72.project.mapper;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72ActivityDataCount;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72ActivityDataCountMapper extends Mapper<Inno72ActivityDataCount> {

    public List<Inno72ActivityDataCount> selectList();

    public List<Inno72ActivityDataCount> selectByParam(Map<String,Object> map);

    public List<Inno72ActivityDataCount> selectHistoryList();
}
