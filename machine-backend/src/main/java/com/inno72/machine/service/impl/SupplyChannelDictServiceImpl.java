package com.inno72.machine.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.machine.mapper.Inno72SupplyChannelDictMapper;
import com.inno72.machine.model.Inno72SupplyChannelDict;
import com.inno72.machine.service.SupplyChannelDictService;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class SupplyChannelDictServiceImpl extends AbstractService<Inno72SupplyChannelDict>
		implements SupplyChannelDictService {
	@Resource
	private Inno72SupplyChannelDictMapper inno72SupplyChannelDictMapper;

	@Override
	public List<Inno72SupplyChannelDict> getAll() {
		return inno72SupplyChannelDictMapper.selectAll();
	}
}
