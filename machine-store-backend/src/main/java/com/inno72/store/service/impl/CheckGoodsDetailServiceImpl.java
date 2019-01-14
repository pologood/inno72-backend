package com.inno72.store.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.store.mapper.Inno72CheckGoodsDetailMapper;
import com.inno72.store.model.Inno72CheckGoodsDetail;
import com.inno72.store.service.CheckGoodsDetailService;

/**
 * Created by CodeGenerator on 2019/01/14.
 */
@Service
@Transactional
public class CheckGoodsDetailServiceImpl extends AbstractService<Inno72CheckGoodsDetail>
		implements CheckGoodsDetailService {
	@Resource
	private Inno72CheckGoodsDetailMapper inno72CheckGoodsDetailMapper;

}
