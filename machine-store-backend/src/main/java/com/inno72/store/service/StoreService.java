package com.inno72.store.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.vo.StoreVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreService extends Service<Inno72Store> {

	Result<Object> saveModel(StoreVo model);

	Result<Object> updateModel(StoreVo model);

	List<StoreVo> findByPage(String keyword);

	List<Inno72Store> getStoreList(String keyword);
}
