package com.inno72.service.impl;

import com.inno72.mapper.Inno72ChannelMapper;
import com.inno72.model.Inno72Activity;
import com.inno72.model.Inno72Channel;
import com.inno72.service.ChannelService;

import tk.mybatis.mapper.entity.Condition;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	public void deleteById(String id) {
		// TODO 活动逻辑删除
		logger.info("--------------------渠道删除-------------------");
		Inno72Channel model = inno72ChannelMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);//0正常,1结束
		
		model.setCreateId("");
		model.setUpdateId("");
		
		super.update(model);
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
	public List<Inno72Channel> findByPage(Inno72Channel channel) {
		// TODO 分页获取渠道列表
		logger.info("---------------------分页获取渠道列表-------------------");
		
		channel.setIsDelete(0);
		Condition condition = new Condition( Inno72Channel.class);
	   	condition.createCriteria().andEqualTo(channel);
		
		return super.findByPage(condition);
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
