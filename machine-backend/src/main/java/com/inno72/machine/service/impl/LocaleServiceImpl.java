package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.service.LocaleService;
import com.inno72.machine.vo.Inno72LocaleVo;
import com.inno72.machine.vo.MachineLocaleInfo;
import com.inno72.share.mapper.Inno72AdminAreaMapper;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class LocaleServiceImpl extends AbstractService<Inno72Locale> implements LocaleService {
	private static Logger logger = LoggerFactory.getLogger(LocaleServiceImpl.class);

	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;
	@Resource
	private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Override
	public Result<String> saveModel(Inno72Locale model) {
		logger.info("---------------------点位新增-------------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			model.setId(StringUtil.getUUID());
			model.setCreateId(userId);
			model.setUpdateId(userId);

			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("---------------------点位删除-------------------");
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		int n = inno72LocaleMapper.selectIsUseing(id);
		if (n > 0) {
			return Results.failure("机器使用中，不能删除！");
		}
		// 已排期未结束，不能修改
		int m = inno72LocaleMapper.selectIsUseingPlan(id);
		if (m > 0) {
			return Results.failure("此点位机器已进行活动排期，暂不能删除！");
		}
		Inno72Locale model = inno72LocaleMapper.selectByPrimaryKey(id);
		// 判断是否可以删除
		model.setIsDelete(1);
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());

		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<MachineLocaleInfo> selectLocaleByMachineCode(List<MachineLocaleInfo> list) {

		List<String> machineCodes = new ArrayList<>();
		for (MachineLocaleInfo machineLocaleInfo : list) {
			String machineCode = machineLocaleInfo.getMachineCode();
			machineCodes.add(machineCode);
		}
		List<MachineLocaleInfo> machineLocaleInfos = inno72LocaleMapper.selectLocaleByMachineCode(machineCodes);
		return machineLocaleInfos;
	}

	@Override
	public Result<String> updateModel(Inno72Locale model) {
		logger.info("---------------------点位更新-------------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			// 已排期未结束，不能修改
			int n = inno72LocaleMapper.selectIsUseingPlan(model.getId());
			if (n > 0) {
				return Results.failure("此点位机器已进行活动排期，暂不能修改！");
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Inno72LocaleVo findById(String id) {
		logger.info("---------------------点位详情（返回省市县商圈）-------------------");
		Inno72LocaleVo vo = inno72LocaleMapper.selectById(id);
		String areaCode = vo.getAreaCode();// 一共9位 省前2位后补0 市4位后补0 县6位后补0 商圈直接取
		String province = StringUtil.getAreaParentCode(areaCode, 1);
		String city = StringUtil.getAreaParentCode(areaCode, 2);
		String district = StringUtil.getAreaParentCode(areaCode, 3);
		vo.setProvince(province);
		vo.setCity(city);
		vo.setDistrict(district);
		vo.setCircle(areaCode);

		return inno72LocaleMapper.selectById(id);
	}

	@Override
	public List<Inno72LocaleVo> findByPage(String code, String keyword) {
		logger.info("---------------------分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		params.put("keyword", keyword);

		List<Inno72LocaleVo> list = inno72LocaleMapper.selectWithAuthByPage(params);

		return list;
	}

	@Override
	public List<Inno72LocaleVo> getList(String code, String keyword) {
		logger.info("---------------------列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		if (StringUtil.isNotEmpty(code)) {
			int num = StringUtil.getAreaCodeNum(code);
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		params.put("keyword", keyword);

		return inno72LocaleMapper.selectWithAuthByList(params);
	}

}
