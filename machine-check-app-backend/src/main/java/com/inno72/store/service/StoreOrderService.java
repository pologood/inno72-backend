package com.inno72.store.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.vo.StoreOrderVo;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreOrderService extends Service<Inno72StoreOrder> {

	Result<String> saveOrder(StoreOrderVo storeOrderVo);
}
