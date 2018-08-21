package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72PaiDataCount;

import java.util.Map;

public interface PaiDataCountService extends Service<Inno72PaiDataCount> {

    void addData();

    Result<Map<String,Object>> findList(Inno72PaiDataCount inno72PaiDataCount);

    void addNowData();
}
