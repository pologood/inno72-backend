package com.inno72.store.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Service;
import com.inno72.store.model.Inno72StoreGoods;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreGoodsService extends Service<Inno72StoreGoods> {

	List<Map<String, Object>> findStoreGoodsByPage(String keyword);

}
