package com.inno72.store.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.store.model.Inno72StoreGoods;
import com.inno72.store.vo.StoreOrderVo;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreGoodsService extends Service<Inno72StoreGoods> {

	List<Inno72StoreGoods> findStoreGoods(StoreOrderVo storeOrderVo);
}
