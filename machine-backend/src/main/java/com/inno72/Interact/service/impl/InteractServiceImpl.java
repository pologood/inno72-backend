package com.inno72.Interact.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.service.InteractService;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.common.AbstractService;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractServiceImpl extends AbstractService<Inno72Interact> implements InteractService {
	private static Logger logger = LoggerFactory.getLogger(InteractServiceImpl.class);
	@Resource
	private Inno72InteractMapper inno72InteractMapper;

	@Override
	public List<InteractListVo> findByPage(String keyword, Integer status) {
		logger.info("---------------------活动分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("status", status);

		return inno72InteractMapper.selectByPage(params);
	}

}
