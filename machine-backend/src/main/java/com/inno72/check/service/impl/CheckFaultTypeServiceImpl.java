package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckFaultTypeMapper;
import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.check.service.CheckFaultTypeService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
@Service
@Transactional
public class CheckFaultTypeServiceImpl extends AbstractService<Inno72CheckFaultType> implements CheckFaultTypeService {
    @Resource
    private Inno72CheckFaultTypeMapper inno72CheckFaultTypeMapper;

	@Override
	public List<Inno72CheckFaultType> findByPage(String keyword) {
		// TODO Auto-generated method stub
		return inno72CheckFaultTypeMapper.selectByPage(keyword);
	}

    
    
    
}
