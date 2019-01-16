package com.inno72.store.service;

import java.util.List;

import com.inno72.check.model.Inno72CheckGoodsNum;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.vo.StoreOrderVo;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreOrderService extends Service<Inno72StoreOrder> {

	Result<String> saveOrder(StoreOrderVo storeOrderVo);

	List<Inno72StoreOrder> findOrderByPage(StoreOrderVo storeOrderVo);

	Result<String> updateOrder(StoreOrderVo storeOrderVo);

	Inno72StoreOrder findOrderById(String id);

	Result<List<Inno72CheckGoodsNum>> findActivityList(StoreOrderVo storeOrderVo);
}
