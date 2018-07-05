package com.inno72.project.service.impl;

import tk.mybatis.mapper.entity.Condition;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ChannelMapper;
import com.inno72.project.model.Inno72Channel;
import com.inno72.project.service.ChannelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ChannelServiceImpl extends AbstractService<Inno72Channel> implements ChannelService {
	private static Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);
	
    @Resource
    private Inno72ChannelMapper inno72ChannelMapper;

	@Override
	public void save(Inno72Channel model) {
		// TODO 活动添加
		logger.info("--------------------渠道新增-------------------");
		model.setId(StringUtil.getUUID());
		model.setCreateId("");
		model.setUpdateId("");
		super.save(model);
	}

	@Override
	public Result<String> delById(String id) {
		// TODO 活动逻辑删除
		logger.info("--------------------渠道删除-------------------");
		
		int n= inno72ChannelMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("渠道使用中，不能删除！");
		}
		Inno72Channel model = inno72ChannelMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public void update(Inno72Channel model) {
		// TODO 渠道更新
		logger.info("--------------------渠道更新-------------------");
		
		model.setCreateId("");
		model.setUpdateId("");
		super.update(model);
	}

	@Override
	public List<Inno72Channel> findByPage(String keyword) {
		// TODO 分页获取渠道列表
		logger.info("---------------------分页获取渠道列表-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		return inno72ChannelMapper.selectByPage(params);
	}
	
	@Override
	public List<Inno72Channel> getList(Inno72Channel channel) {
		// TODO 渠道列表
		logger.info("---------------------渠道列表查询-------------------");
		channel.setIsDelete(0);
		Condition condition = new Condition( Inno72Channel.class);
	   	condition.createCriteria().andEqualTo(channel);
		return super.findByCondition(condition);
	}
    
    
}
