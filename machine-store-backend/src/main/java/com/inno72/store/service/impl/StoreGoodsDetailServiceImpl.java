package com.inno72.store.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.store.mapper.Inno72StoreGoodsDetailMapper;
import com.inno72.store.model.Inno72StoreGoodsDetail;
import com.inno72.store.service.StoreGoodsDetailService;

/**
 * Created by CodeGenerator on 2019/01/16.
 */
@Service
@Transactional
public class StoreGoodsDetailServiceImpl extends AbstractService<Inno72StoreGoodsDetail>
		implements StoreGoodsDetailService {
	@Resource
	private Inno72StoreGoodsDetailMapper inno72StoreGoodsDetailMapper;

}
