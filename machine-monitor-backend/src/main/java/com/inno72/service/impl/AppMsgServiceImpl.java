package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72AppMsgMapper;
import com.inno72.model.Inno72AppMsg;
import com.inno72.service.AppMsgService;

/**
 * Created by CodeGenerator on 2018/08/16.
 */
@Service
@Transactional
public class AppMsgServiceImpl extends AbstractService<Inno72AppMsg> implements AppMsgService {
	@Resource
	private Inno72AppMsgMapper inno72AppMsgMapper;

}
