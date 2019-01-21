package com.inno72.store.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.vo.StoreOrderVo;

@org.apache.ibatis.annotations.Mapper
public interface Inno72StoreOrderMapper extends Mapper<Inno72StoreOrder> {

	List<Map<String, Object>> selectReceiveOrderByPage(Map<String, Object> map);

	List<Map<String, Object>> selectSendOrderByPage(Map<String, Object> map);

	StoreOrderVo selectOrderById(String id);

	List<Map<String, Object>> getMerchantList(Map<String, Object> map);

	List<Map<String, Object>> getGoodsList(Map<String, Object> map);

	List<Map<String, Object>> getActivityList(Map<String, Object> map);

	List<Map<String, Object>> getCheckUserList(Map<String, Object> map);

	int selectPendingStorageCount(@Param("userId") String userId);

	int selectPendingOutStoreCount(@Param("userId") String userId);

	int selectUnOutStorageCount(@Param("userId") String userId);
}