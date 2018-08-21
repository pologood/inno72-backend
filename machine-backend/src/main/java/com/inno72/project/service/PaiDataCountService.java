package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72PaiDataCount;
import com.inno72.project.vo.Inno72ActivityPaiDataVo;

import java.util.List;
import java.util.Map;

public interface PaiDataCountService extends Service<Inno72PaiDataCount> {

    void addData();

    Result<Map<String,Object>> findList(Inno72PaiDataCount inno72PaiDataCount);

    void addTotalData();

    Result<List<Inno72ActivityPaiDataVo>> findTotalDataList();
}
