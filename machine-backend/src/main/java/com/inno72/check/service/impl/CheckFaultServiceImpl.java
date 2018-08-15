package com.inno72.check.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.mapper.Inno72CheckFaultRemarkMapper;
import com.inno72.check.mapper.Inno72CheckFaultTypeMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultRemark;
import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckFaultService;
import com.inno72.check.vo.Inno72CheckFaultVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/07/19.
 */
@Service
@Transactional
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {
	private static Logger logger = LoggerFactory.getLogger(CheckFaultServiceImpl.class);

	@Resource
	private Inno72CheckFaultMapper inno72CheckFaultMapper;
	@Resource
	private Inno72CheckFaultRemarkMapper inno72CheckFaultRemarkMapper;
	@Resource
	private Inno72CheckUserMapper inno72CheckUserMapper;
	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Resource
	private Inno72CheckFaultTypeMapper inno72CheckFaultTypeMapper;

	@Override
	public Result<String> saveModel(Inno72CheckFault model) {

		logger.info("----------------工单添加--------------");
		logger.info("参数:{}", JSON.toJSONString(model));
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		try {
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setId(StringUtil.getUUID());
			model.setSubmitId(mUserId);
			model.setSubmitUser(mUser.getName());
			model.setSubmitTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			model.setSource(2);// 来源：1.巡检上报，2.运营派单，3.报警派单
			model.setStatus(1);// 工单状态（1.待接单，2.处理中，3.已完成，4.已确认，5.已关闭
			String time = DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S2);
			model.setCode("F" + StringUtil.createRandomCode(6) + time);
			// 故障类：显示机器编号+出现紧急/普通+故障类型 故障，请尽快处理；补货/缺货类：显示机器编号+需要 紧急/普通
			// 补货，请尽快处理；报警类：显示机器编号+出现紧急/普通+报警类型，请尽快处理；其他类：显示机器编号+出现紧急/普通问题，请尽快处理
			Integer workType = model.getWorkType();
			Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(model.getMachineId());
			String title = machine.getMachineCode();
			String urgentType = "";
			if (model.getUrgentStatus() == 1) {
				urgentType = "出现普通";
			} else if (model.getUrgentStatus() == 2) {
				urgentType = "出现紧急";
			}

			String typeStr = "";
			if (workType == 1) {
				if (StringUtil.isNotBlank(model.getType())) {
					Inno72CheckFaultType faultType = inno72CheckFaultTypeMapper.selectByPrimaryKey(model.getType());
					typeStr = faultType.getName() + "故障，请尽快处理";
				} else {
					typeStr = "故障，请尽快处理";
				}
			} else if (workType == 2) {
				typeStr = "报警，请尽快处理";
			} else if (workType == 3) {
				typeStr = "补货，请尽快处理";
			} else if (workType == 4) {
				typeStr = "投诉，请尽快处理";
			} else {
				typeStr = "问题，请尽快处理";
			}

			model.setTitle(title + urgentType + typeStr);

			inno72CheckFaultMapper.insert(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success();
	}

	@Override
	public Result<String> updateStatus(String id, int status) {

		logger.info("----------------工单状态操作--------------");
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		try {
			Inno72CheckFault checkFault = inno72CheckFaultMapper.selectByPrimaryKey(id);
			checkFault.setStatus(status);

			checkFault.setUpdateTime(LocalDateTime.now());
			inno72CheckFaultMapper.updateByPrimaryKey(checkFault);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success();
	}

	@Override
	public List<Inno72CheckFault> findByPage(String keyword, String status, String workType, String source, String type,
			String startTime, String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("status", status);
		params.put("workType", workType);
		params.put("source", source);
		params.put("keyword", keyword);
		params.put("type", type);
		params.put("startTime", startTime);
		if (StringUtil.isNotBlank(endTime)) {
			params.put("endTime", endTime + " 23:59:59");
		}

		return inno72CheckFaultMapper.selectByPage(params);
	}

	@Override
	public Result<String> faultAnswer(String id, String remark, String toUserId) {

		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isBlank(remark)) {
				return Results.failure("回复内容不能为空！");
			}
			if (StringUtil.isBlank(id)) {
				return Results.failure("参数异常！");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			Inno72CheckFaultRemark faultRemark = new Inno72CheckFaultRemark();

			faultRemark.setId(StringUtil.getUUID());
			faultRemark.setFaultId(id);
			faultRemark.setRemark(remark);
			faultRemark.setUserId(userId);
			faultRemark.setType(2);
			faultRemark.setCreateTime(LocalDateTime.now());

			inno72CheckFaultRemarkMapper.insert(faultRemark);
			// 派单
			if (StringUtil.isNotBlank(toUserId)) {
				Inno72CheckUser receiveUser = inno72CheckUserMapper.selectByPrimaryKey(toUserId);
				Inno72CheckFault checkFault = inno72CheckFaultMapper.selectByPrimaryKey(id);
				checkFault.setSubmitUser(mUser.getName());
				checkFault.setSubmitId(userId);
				checkFault.setSubmitTime(LocalDateTime.now());
				checkFault.setReceiveId(receiveUser.getId());
				checkFault.setReceiveUser(receiveUser.getName());
				checkFault.setTalkingTime(LocalDateTime.now());

				inno72CheckFaultMapper.updateByPrimaryKey(checkFault);
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Inno72CheckFaultVo selectFaultDetail(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("path", CommonConstants.ALI_OSS);

		return inno72CheckFaultMapper.selectFaultDetail(params);
	}

	@Override
	public List<Inno72CheckUser> selectMachineUserList(String keyword, String machineId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		params.put("machineId", machineId);

		return inno72CheckFaultMapper.selectMachineUserList(params);
	}

}
