package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72ActivityDataCount;

import java.util.List;

public interface ActivityDataCountService extends Service<Inno72ActivityDataCount> {

    void addData();

    Result<List<Inno72ActivityDataCount>> findList(Inno72ActivityDataCount inno72ActivityDataCount);
}
