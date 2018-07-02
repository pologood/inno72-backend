package com.inno72.service.impl;

import com.inno72.mapper.Inno72AdminAreaMapper;
import com.inno72.model.Inno72AdminArea;
import com.inno72.service.AdminAreaService;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.StringUtil;

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
