package com.inno72.project.service.impl;

import tk.mybatis.mapper.entity.Condition;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ChannelMapper;
import com.inno72.project.model.Inno72Channel;
import com.inno72.project.service.ChannelService;
import com.inno72.system.model.Inno72User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	public Result<String> saveModel(Inno72Channel model) {
		logger.info("--------------------渠道新增-------------------");
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		int n= inno72ChannelMapper.getCount(model.getChannelCode());
		if (n>0) {
			return Results.failure("渠道编码已存在，请确认！");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		model.setId(StringUtil.getUUID());
		model.setCreateId(userId);
		model.setUpdateId(userId);
		super.save(model);
		return Results.success("操作成功");
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("--------------------渠道删除-------------------");
		
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		
		int n= inno72ChannelMapper.selectIsUseing(id);
		if (n>0) {
			return Results.failure("渠道使用中，不能删除！");
		}
		Inno72Channel model = inno72ChannelMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());
		
		super.update(model);
		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModel(Inno72Channel model) {
		logger.info("--------------------渠道更新-------------------");
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Channel m = inno72ChannelMapper.selectByPrimaryKey(model.getId());
		int n= inno72ChannelMapper.getCount(model.getChannelCode());
		if (n>0 && !m.getChannelCode().equals(model.getChannelCode())) {
			return Results.failure("渠道编码已存在，请确认！");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());
		super.update(model);
		return Results.success("操作成功");
	}

	@Override
	public List<Inno72Channel> findByPage(String keyword) {
		logger.info("---------------------分页获取渠道列表-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword=Optional.ofNullable(keyword).map(a->a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		return inno72ChannelMapper.selectByPage(params);
	}
	
	@Override
	public List<Inno72Channel> getList(Inno72Channel channel) {
		logger.info("---------------------渠道列表查询-------------------");
		channel.setIsDelete(0);
		Condition condition = new Condition( Inno72Channel.class);
	   	condition.createCriteria().andEqualTo(channel);
		return super.findByCondition(condition);
	}
    
    
}
