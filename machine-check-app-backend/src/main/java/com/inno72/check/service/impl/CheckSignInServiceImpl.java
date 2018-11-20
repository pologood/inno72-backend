package com.inno72.check.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.check.vo.SignVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;

@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {

	@Resource
	private Inno72CheckSignInMapper checkSignInMapper;

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private MongoOperations mongoTpl;

	@Override
	public Result<String> add(Inno72CheckSignIn signIn) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String machineId = signIn.getMachineId();
		if (StringUtil.isEmpty(machineId)) {
			return Results.failure("参数缺失");
		}
		signIn.setCreateTime(LocalDateTime.now());
		signIn.setId(StringUtil.getUUID());
		signIn.setCheckUserId(UserUtil.getUser().getId());

		// ------------------------ 判断是否第一次打卡
		Map<String, Object> map = new HashMap<>();
		String checkUserId = UserUtil.getUser().getId();
		map.put("checkUserId", checkUserId);
		map.put("machineId", machineId);
		int num = checkSignInMapper.selectDaySignInNum(map);
		if (num == 0) {// 当天首次打卡默认有效
			signIn.setStatus(0);
		}
		// ------------------------ 判断是否第一次打卡
		int count = checkSignInMapper.insertSelective(signIn);
		if (count != 1) {
			Results.failure("打卡失败");
		}
		Inno72Machine machine = inno72MachineMapper.getMachineById(machineId);
		StringUtil.logger(CommonConstants.LOG_TYPE_MACHINE_SIGN, machine.getMachineCode(),
				"用户" + checkUser.getName() + "，在互动管家中进行机器打卡");
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<List<SignVo>> findByMonth(SignVo signVo) {
		Map<String, Object> map = new HashMap<>();
		String machineId = signVo.getMachineId();
		String checkUserId = UserUtil.getUser().getId();
		map.put("checkUserId", checkUserId);
		map.put("machineId", machineId);
		map.put("signDateStr", signVo.getSignDateStr());
		List<SignVo> list = checkSignInMapper.selectMonth(map);
		LocalDate localDate = DateUtil.toDate(signVo.getSignDateStr(), DateUtil.DF_ONLY_YMD_S1);
		List<String> dateList = DateUtil.getMonthFullDay(localDate);
		Map<String, SignVo> signVoMap = new HashMap<>();
		for (SignVo vo : list) {
			String date = vo.getSignDateStr();
			int type = vo.getType();
			if (!signVoMap.containsKey(date)) {
				signVoMap.put(date, vo);
			} else if (type == 1) {
				signVoMap.put(date, vo);
			}
		}
		List<SignVo> resultList = new ArrayList<>();
		for (String date : dateList) {
			if (signVoMap.containsKey(date)) {
				resultList.add(signVoMap.get(date));
			} else {
				SignVo unVo = new SignVo();
				unVo.setSignDateStr(date);
				unVo.setMachineId(machineId);
				unVo.setCheckUserId(checkUserId);
				if (DateUtil.toDateOld(date, DateUtil.DF_ONLY_YMD_S1_OLD).before(new Date())) {
					unVo.setType(3);
				} else {
					unVo.setType(4);
				}
				resultList.add(unVo);
			}
		}
		return ResultGenerator.genSuccessResult(resultList);
	}

	@Override
	public Result<List<MachineSignInVo>> findMachineSignList() {
		List<MachineSignInVo> list = checkSignInMapper.selectMachineSignList(UserUtil.getUser().getId());
		if (list != null && list.size() > 0) {
			for (MachineSignInVo vo : list) {
				List<Inno72CheckSignIn> signInList = vo.getSignInList();
				if (signInList != null && signInList.size() > 0) {
					vo.setSignInStatus(1);
					vo.setCreateTime(signInList.get(0).getCreateTime());
				} else {
					vo.setSignInStatus(-1);
				}
				vo.setSignInList(null);
			}
		}

		return ResultGenerator.genSuccessResult(list);
	}
}
