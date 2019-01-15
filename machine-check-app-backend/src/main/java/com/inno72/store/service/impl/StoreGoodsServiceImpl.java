package com.inno72.store.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.store.mapper.Inno72StoreGoodsMapper;
import com.inno72.store.model.Inno72StoreGoods;
import com.inno72.store.service.StoreGoodsService;
import com.inno72.store.vo.StoreOrderVo;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreGoodsServiceImpl extends AbstractService<Inno72StoreGoods> implements StoreGoodsService {
    @Resource
    private Inno72StoreGoodsMapper inno72StoreGoodsMapper;

	@Override
	public List<Inno72StoreGoods> findStoreGoods(StoreOrderVo storeOrderVo) {
		Map<String,Object> map = new HashMap<>();
		String storeId = storeOrderVo.getStoreId();
		if(StringUtil.isNotEmpty(storeId)){
			map.put("storeId",storeId);
		}
		String keyword = storeOrderVo.getKeyword();
		if(StringUtil.isNotEmpty(keyword)){
			map.put("keyword",keyword);
		}
		String receiveId = UserUtil.getUser().getId();
		List<Inno72StoreGoods> goodsList = inno72StoreGoodsMapper.selectByParam(map);
		return goodsList;
	}
}
