package com.inno72.check.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.check.mapper.Inno72CheckFaultTypeMapper;
import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.check.service.CheckFaultTypeService;
import com.inno72.check.vo.Inno72CheckFaultTypeVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/07/20.
 */
@Service
@Transactional
public class CheckFaultTypeServiceImpl extends AbstractService<Inno72CheckFaultType> implements CheckFaultTypeService {
	private static Logger logger = LoggerFactory.getLogger(CheckFaultTypeServiceImpl.class);

	@Resource
	private Inno72CheckFaultTypeMapper inno72CheckFaultTypeMapper;

	@Override
	public List<Inno72CheckFaultType> findByPage(String keyword) {

		List<Inno72CheckFaultType> list = inno72CheckFaultTypeMapper.selectByPage(keyword);
		List<Inno72CheckFaultType> allList = inno72CheckFaultTypeMapper.selectAllList(keyword);
		for (Inno72CheckFaultType inno72CheckFaultType : list) {
			int ix = allList.indexOf(inno72CheckFaultType);
			if (ix != -1) {
				inno72CheckFaultType.setParentName(allList.get(ix).getParentName());
			}
		}
		return list;
	}

	@Override
	public Result<String> saveModel(Inno72CheckFaultTypeVo model) {

		logger.info("----------------故障类型添加--------------");
		logger.info("参数:{}", JSON.toJSONString(model));
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		try {
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			if (StringUtil.isBlank(model.getName())) {
				return Results.failure("请填写类型名称");
			}
			List<Inno72CheckFaultType> solutions = model.getSolutions();
			if (null == solutions || solutions.size() == 0) {
				return Results.failure("请填写解决方案");
			}
			// 类型code
			String code = StringUtil.getUUID();
			model.setCode(code);
			model.setLevel(1);
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			// 子级解决方案
			List<Inno72CheckFaultType> insertFaultTypeList = new ArrayList<>();
			int i = 0;
			for (Inno72CheckFaultType solutionType : solutions) {
				String childcode = StringUtil.getUUID();
				solutionType.setCode(childcode);
				solutionType.setParentCode(code);
				solutionType.setParentName(model.getName());
				solutionType.setLevel(2);
				solutionType.setCreateId(mUserId);
				solutionType.setUpdateId(mUserId);
				solutionType.setCreateTime(LocalDateTime.now());
				solutionType.setUpdateTime(LocalDateTime.now());
				solutionType.setSeq(i++);
				insertFaultTypeList.add(solutionType);
			}
			inno72CheckFaultTypeMapper.insertFaultTypeList(insertFaultTypeList);

			inno72CheckFaultTypeMapper.insert(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success();
	}

	@Override
	public Result<String> updateModel(Inno72CheckFaultTypeVo model) {

		logger.info("----------------故障类型编辑--------------");
		logger.info("参数:{}", JSON.toJSONString(model));
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		try {
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			if (StringUtil.isBlank(model.getName())) {
				return Results.failure("请填写类型名称");
			}
			List<Inno72CheckFaultType> solutions = model.getSolutions();
			if (null == solutions || solutions.size() == 0) {
				return Results.failure("请填写解决方案");
			}
			// 类型code

			model.setUpdateId(mUserId);
			model.setUpdateTime(LocalDateTime.now());
			// 子级解决方案
			List<Inno72CheckFaultType> insertFaultTypeList = new ArrayList<>();
			int i = 0;
			for (Inno72CheckFaultType solutionType : solutions) {
				String childcode = StringUtil.getUUID();
				solutionType.setCode(childcode);
				solutionType.setParentCode(model.getCode());
				solutionType.setParentName(model.getName());
				solutionType.setLevel(2);
				solutionType.setCreateId(mUserId);
				solutionType.setUpdateId(mUserId);
				solutionType.setCreateTime(LocalDateTime.now());
				solutionType.setUpdateTime(LocalDateTime.now());
				solutionType.setSeq(i++);
				insertFaultTypeList.add(solutionType);
			}
			// 删除原来解决方案
			inno72CheckFaultTypeMapper.deleteByParentCode(model.getCode());

			// 添加新的解决方案
			inno72CheckFaultTypeMapper.insertFaultTypeList(insertFaultTypeList);

			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success();
	}

	@Override
	public Inno72CheckFaultTypeVo selectTypeDetail(String code) {
		return inno72CheckFaultTypeMapper.selectTypeDetail(code);
	}

}
