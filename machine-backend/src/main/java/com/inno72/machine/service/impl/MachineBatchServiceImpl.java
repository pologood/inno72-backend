package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72MachineBatchDetailMapper;
import com.inno72.machine.mapper.Inno72MachineBatchMapper;
import com.inno72.machine.model.Inno72MachineBatch;
import com.inno72.machine.model.Inno72MachineBatchDetail;
import com.inno72.machine.service.MachineBatchService;
import com.inno72.system.model.Inno72User;
import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/17.
 */
@Service
@Transactional
public class MachineBatchServiceImpl extends AbstractService<Inno72MachineBatch> implements MachineBatchService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private Inno72MachineBatchMapper inno72MachineBatchMapper;

	@Resource
	private Inno72MachineBatchDetailMapper inno72MachineBatchDetailMapper;


	@Override
	public Result<String> saveBatch(Inno72MachineBatch batch) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String id = batch.getId();
		String batchName = batch.getBatchName();
		List<Inno72MachineBatchDetail> detailList = batch.getDetailList();
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(batchName) && (detailList == null || detailList.size()==0)){
			return Results.failure("参数缺失");
		}
		Inno72MachineBatch machineBatch = inno72MachineBatchMapper.selectByPrimaryKey(id);
		if(machineBatch != null){
			return Results.failure("已存在此批次");
		}
		batch.setCreateId(mUser.getId());
		batch.setCreateUser(mUser.getName());
		LocalDateTime now = LocalDateTime.now();
		batch.setCreateTime(now);
		batch.setUpdateId(mUser.getId());
		batch.setUpdateUser(mUser.getName());
		batch.setUpdateTime(now);
		inno72MachineBatchMapper.insertSelective(batch);
		for(Inno72MachineBatchDetail detail:detailList){
			detail.setId(StringUtil.getUUID());
			detail.setBatchId(id);
			inno72MachineBatchDetailMapper.insertSelective(detail);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72MachineBatch> batchList(String keyword) {
		List<Inno72MachineBatch> list = inno72MachineBatchMapper.findByPage(keyword);
		return list;
	}

	@Override
	public Result<Inno72MachineBatch> detail(String id) {
		if(StringUtil.isEmpty(id)){
			return Results.failure("参数不能为空");
		}
		Inno72MachineBatch detail = inno72MachineBatchMapper.selectDetailById(id);
		return ResultGenerator.genSuccessResult(detail);
	}

	@Override
	public Result<String> updateBatch(Inno72MachineBatch batch) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String id = batch.getId();
		String batchName = batch.getBatchName();
		List<Inno72MachineBatchDetail> detailList = batch.getDetailList();
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(batchName) && (detailList == null || detailList.size()==0)){
			return Results.failure("参数缺失");
		}
		LocalDateTime now = LocalDateTime.now();
		batch.setUpdateId(mUser.getId());
		batch.setUpdateUser(mUser.getName());
		batch.setUpdateTime(now);
		inno72MachineBatchMapper.updateByPrimaryKeySelective(batch);
		Condition condition = new Condition(Inno72MachineBatchDetail.class);
		condition.createCriteria().andEqualTo("batchId", id);
		inno72MachineBatchDetailMapper.deleteByCondition(condition);
		for(Inno72MachineBatchDetail detail:detailList){
			detail.setId(StringUtil.getUUID());
			detail.setBatchId(id);
			inno72MachineBatchDetailMapper.insertSelective(detail);
		}
		return ResultGenerator.genSuccessResult();
	}


}
