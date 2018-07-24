package com.inno72.share.service.impl;


import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.share.mapper.Inno72AdminAreaMapper;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.share.service.AdminAreaService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class AdminAreaServiceImpl extends AbstractService<Inno72AdminArea> implements AdminAreaService {
    @Resource
    private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Override
	public List<Inno72AdminArea> getLiset(String code) {
		Condition condition = new Condition( Inno72AdminArea.class);
	   	 if (StringUtil.isEmpty(code)) {
	   		 condition.createCriteria().andCondition("level = 1");
	   	 }else{
	   		 condition.createCriteria().andCondition("parent_code = "+code);
	   	 }
		return inno72AdminAreaMapper.selectByCondition(condition);
	}
    

}
