package com.inno72.Interact.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static Logger logger = LoggerFactory.getLogger(InteractGameRuleServiceImpl.class);
	@Resource
	private Inno72InteractGameRuleMapper inno72InteractGameRuleMapper;

	@Override
	public List<Inno72InteractGameRule> getGameRuleList(String interactId) {
		logger.info("---------------------获取游戏活动掉货规则-------------------");

		Inno72InteractGameRule pm = new Inno72InteractGameRule();
		pm.setInteractId(interactId);
		return inno72InteractGameRuleMapper.select(pm);

	}

}
