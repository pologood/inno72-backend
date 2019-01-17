package com.inno72.store.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.vo.StoreOrderVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreOrderService extends Service<Inno72StoreOrder> {

	Result<Object> saveModel(Inno72StoreOrder model);

	List<Map<String, Object>> findSendOrderByPage(String date, String keyword);

	List<Map<String, Object>> findReceiveOrderByPage(String date, String keyword);

	StoreOrderVo findDetailById(String id);

	Result<Object> receiverConfirm(StoreOrderVo storeOrderVo);

	List<Map<String, Object>> getMerchantList(String keyword);

	List<Map<String, Object>> getGoodsList(String merchantId, String keyword);

	List<Map<String, Object>> getActivityList(String keyword);

	List<Map<String, Object>> getCheckUserList(String keyword);

	Result<Map<String,Object>> getHomePageInfo();
}
