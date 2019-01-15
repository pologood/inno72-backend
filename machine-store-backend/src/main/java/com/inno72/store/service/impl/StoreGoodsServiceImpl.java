package com.inno72.store.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.UserUtil;
import com.inno72.store.mapper.Inno72StoreGoodsMapper;
import com.inno72.store.model.Inno72StoreGoods;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StoreGoodsService;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreGoodsServiceImpl extends AbstractService<Inno72StoreGoods> implements StoreGoodsService {
	@Resource
	private Inno72StoreGoodsMapper inno72StoreGoodsMapper;

	/**
	 * 查询库存商品
	 */
	@Override
	public List<Map<String, Object>> findStoreGoodsByPage(String keyword) {
		Inno72Storekeeper mUser = UserUtil.getKepper();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", mUser.getId());

		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		return inno72StoreGoodsMapper.selectStoreGoodsByPage(params);
	}

}
