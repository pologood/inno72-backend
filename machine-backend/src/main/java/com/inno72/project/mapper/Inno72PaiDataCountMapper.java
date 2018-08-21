package com.inno72.project.mapper;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72PaiDataCount;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72PaiDataCountMapper extends Mapper<Inno72PaiDataCount> {

    public List<Inno72PaiDataCount> selectList();

    public List<Inno72PaiDataCount> selectByParam(Map<String, Object> map);

}
