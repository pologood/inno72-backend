package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72MachineBatch;

/**
 * Created by CodeGenerator on 2018/09/17.
 */
public interface MachineBatchService extends Service<Inno72MachineBatch> {

	public Result<String> saveBatch(Inno72MachineBatch batch);

	public List<Inno72MachineBatch> batchList(String keyword);

	public Result<Inno72MachineBatch> detail(String id);

	public Result<String> updateBatch(Inno72MachineBatch batch);
}
