package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72ActivityDataCount;

import java.util.Map;

public interface ActivityDataCountService extends Service<Inno72ActivityDataCount> {

    void addData();

    Result<Map<String,Object>> findList(Inno72ActivityDataCount inno72ActivityDataCount);

    Result<String> setHistory();
}
