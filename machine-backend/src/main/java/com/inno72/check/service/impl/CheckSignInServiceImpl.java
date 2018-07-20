package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {
    @Resource
    private Inno72CheckSignInMapper inno72CheckSignInMapper;

	@Override
	public List<Inno72CheckUserVo> findByPage(String code,String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("code", code);
		
		return inno72CheckSignInMapper.selectByPage(params);
	}

    
    
    
}
