package com.inno72.store.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.store.mapper.Inno72CheckGoodsNumMapper;
import com.inno72.store.model.Inno72CheckGoodsNum;
import com.inno72.store.service.CheckGoodsNumService;

/**
 * Created by CodeGenerator on 2019/01/14.
 */
@Service
@Transactional
public class CheckGoodsNumServiceImpl extends AbstractService<Inno72CheckGoodsNum> implements CheckGoodsNumService {
	@Resource
	private Inno72CheckGoodsNumMapper inno72CheckGoodsNumMapper;

}
