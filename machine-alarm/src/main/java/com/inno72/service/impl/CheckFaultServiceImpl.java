package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72CheckFaultMapper;
import com.inno72.mapper.Inno72CheckFaultRemarkMapper;
import com.inno72.model.Inno72CheckFault;
import com.inno72.model.Inno72CheckFaultRemark;
import com.inno72.service.CheckFaultService;
@Service
@Transactional
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {

	@Resource
	private Inno72CheckFaultMapper inno72CheckFaultMapper;

	@Resource
	private Inno72CheckFaultRemarkMapper inno72CheckFaultRemarkMapper;
	@Override
	public List<Inno72CheckFault> findByPage(Object condition) {
		return null;
	}
	@Override
	public Integer saveCheckFault(Inno72CheckFault checkFault){
		inno72CheckFaultMapper.insertSelective(checkFault);
		return 1;
	}

	@Override
	public Integer saveCheckFaultRemark(Inno72CheckFaultRemark checkFaultRemark) {
		inno72CheckFaultRemarkMapper.insertSelective(checkFaultRemark);
		return 1;
	}
}
