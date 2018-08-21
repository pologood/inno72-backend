package com.inno72.project.mapper;

import com.inno72.common.Mapper;
import com.inno72.project.model.Inno72PaiDataCount;
import com.inno72.project.model.Inno72PaiNowData;
import com.inno72.project.vo.Inno72ActivityPaiDataVo;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72PaiNowDataMapper extends Mapper<Inno72PaiNowData> {

    public List<Inno72PaiNowData> selectList();


    public List<Inno72ActivityPaiDataVo> selectPaiNowList();
}
