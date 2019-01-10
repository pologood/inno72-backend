package com.inno72.store.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.vo.StoreOrderVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreExpressService extends Service<Inno72StoreExpress> {

	Result<Object> saveModel(StoreOrderVo storeOrderVo);

}
