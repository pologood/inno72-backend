package com.inno72.Interact.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractGameRuleMapper;
import com.inno72.Interact.model.Inno72InteractGameRule;
import com.inno72.Interact.service.InteractGameRuleService;
import com.inno72.common.AbstractService;

/**
 * Created by CodeGenerator on 2018/11/27.
 */
@Service
@Transactional
public class InteractGameRuleServiceImpl extends AbstractService<Inno72InteractGameRule>
		implements InteractGameRuleService {
	@Resource
	private Inno72InteractGameRuleMapper inno72InteractGameRuleMapper;

}
