package com.inno72.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72AlarmMsgMapper;
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.service.AlarmMsgService;

/**
 * Created by CodeGenerator on 2018/07/19.
 * 
 * @author
 */
@Service
@Transactional
public class AlarmMsgServiceImpl extends AbstractService<Inno72AlarmMsg> implements AlarmMsgService {
	@Resource
	private Inno72AlarmMsgMapper inno72AlarmMsgMapper;

	@Override
	public List<Inno72AlarmMsg> findByPage(Object condition) {
		return null;
	}

}
