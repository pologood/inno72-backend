package com.inno72.machine.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.check.mapper.Inno72CheckUserMachineMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineCheckAppBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.machine.mapper.Inno72AdminAreaMapper;
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.CutAppVo;
import com.inno72.machine.vo.MachineStartAppBean;
import com.inno72.machine.vo.SendMessageBean;
import com.inno72.machine.vo.SupplyRequestVo;
import com.inno72.plugin.http.HttpClient;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private Inno72CheckUserMachineMapper inno72CheckUserMachineMapper;

	@Resource
	private Inno72AdminAreaMapper inno72AdminAreaMapper;

	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;

	@Resource
	private Inno72CheckUserMapper inno72CheckUserMapper;

	@Resource
	private MachineCheckAppBackendProperties machineCheckAppBackendProperties;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Result<String> setMachine(SupplyRequestVo vo) {
		String localeId = vo.getLocaleId();
		String machineCode = vo.getMachineCode();
		if (StringUtil.isEmpty(localeId) || StringUtil.isEmpty(machineCode)) {
			return Results.failure("参数不能为空");
		}
		Inno72Machine machine = inno72MachineMapper.getMachineByCode(machineCode);
		if (machine == null) {
			return Results.failure("机器不存在");
		}
		String machineId = machine.getId();
		Integer machineStatus = machine.getMachineStatus();
		if (machineStatus.equals(3) || machineStatus.equals(4)) {
			Inno72CheckUser checkUser = UserUtil.getUser();
			machine.setLocaleId(localeId);
			machine.setMachineStatus(4);
			machine.setUpdateId(checkUser.getId());
			machine.setUpdateTime(LocalDateTime.now());
			machine.setInsideTime(LocalDateTime.now());
			inno72MachineMapper.updateByPrimaryKeySelective(machine);
			Condition condition = new Condition(Inno72CheckUserMachine.class);
			condition.createCriteria().andEqualTo("checkUserId", checkUser.getId()).andEqualTo("machineId", machineId);
			List<Inno72CheckUserMachine> userMachines = inno72CheckUserMachineMapper.selectByCondition(condition);
			if (userMachines == null || userMachines.size() == 0) {
				Inno72CheckUserMachine userMachine = new Inno72CheckUserMachine();
				userMachine.setId(StringUtil.getUUID());
				userMachine.setCheckUserId(checkUser.getId());
				userMachine.setMachineId(machineId);
				inno72CheckUserMachineMapper.insertSelective(userMachine);
			}
			machine = inno72MachineMapper.getMachineByCode(machineCode);
			String detail = "用户" + checkUser.getName() + "，在互动管家中进行机器入厂操作，选择点位是：" + machine.getLocaleStr();
			StringUtil.logger(CommonConstants.LOG_TYPE_IN_FACTORY, machine.getMachineCode(), detail);
		} else {
			return Results.failure("机器状态有误");
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72Machine> getMachineList() {
		String chekUserId = UserUtil.getUser().getId();
		List<Inno72Machine> list = inno72MachineMapper.machineListByPage(chekUserId);
		List<Inno72Machine> list1 = new ArrayList<>();
		List<Inno72Machine> list2 = new ArrayList<>();
		List<Inno72Machine> list3 = new ArrayList<>();
		List<Inno72Machine> list4 = new ArrayList<>();
		for (Inno72Machine machine : list) {
			int lackGoodsStatus = machine.getLackGoodsStatus();
			int faultStatus = machine.getFaultStatus();
			if (lackGoodsStatus == 1 && faultStatus == -1) {
				list1.add(machine);
			} else if (lackGoodsStatus == 1 && faultStatus == 1) {
				list2.add(machine);
			} else if (lackGoodsStatus == 0 && faultStatus == -1) {
				list3.add(machine);
			} else if (lackGoodsStatus == 0 && faultStatus == 1) {
				list4.add(machine);
			}
		}
		List<Inno72Machine> resultList = new ArrayList<>();
		resultList.addAll(list1);
		resultList.addAll(list2);
		resultList.addAll(list3);
		resultList.addAll(list4);
		return resultList;
	}

	/**
	 * 查询一级地址
	 */
	@Override
	public Result<List<Inno72AdminArea>> findFirstLevelArea() {
		// String userId = UserUtil.getUser().getId();
		// Inno72CheckUser user =
		// inno72CheckUserMapper.selectByPrimaryKey(userId);
		// String area = user.getArea();
		Map<String, Object> map = new HashMap<>();
		// if(StringUtil.isNotEmpty(area)){
		// Inno72AdminArea adminArea =
		// inno72AdminAreaMapper.selectByPrimaryKey(area);
		// map.put("level",adminArea.getLevel());
		// map.put("code",adminArea.getCode());
		// }
		List<Inno72AdminArea> list = inno72AdminAreaMapper.selectFistLevelArea(map);
		return ResultGenerator.genSuccessResult(list);
	}

	/**
	 * 查询单个一级地址及以下地址
	 */
	@Override
	public Result<Inno72AdminArea> cityLevelArea(Inno72AdminArea adminArea) {
		String area = "";
		// if(StringUtil.isEmpty(area)){
		// Inno72CheckUser user =
		// inno72CheckUserMapper.selectByPrimaryKey(UserUtil.getUser().getId());
		// area = user.getArea();
		// if(StringUtil.isEmpty(area)){
		// Results.failure("用户未设置区域");
		// }
		// }
		Map<String, Object> map = new HashMap<>();
		map.put("code", adminArea.getCode());
		Inno72AdminArea inno72AdminArea = inno72AdminAreaMapper.cityLevelArea(map);
		return ResultGenerator.genSuccessResult(inno72AdminArea);
	}

	@Override
	public Result<List<Inno72Locale>> selectLocaleByAreaCode(String areaCode) {
		List<Inno72Locale> list = inno72LocaleMapper.selectLocaleByAreaCode(areaCode);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<Map<String, Object>> selectMachineLocale(Inno72Machine inno72Machine) {
		String machineCode = inno72Machine.getMachineCode();
		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("参数不能为空");
		}
		Inno72Machine machineLocale = inno72MachineMapper.getMachineByCode(machineCode);
		if (machineLocale != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("localeStr", machineLocale.getLocaleStr());
			map.put("localeId", machineLocale.getLocaleId());
			Condition condition = new Condition(Inno72CheckUserMachine.class);
			condition.createCriteria().andEqualTo("checkUserId", UserUtil.getUser().getId()).andEqualTo("machineId",
					machineLocale.getId());

			List<Inno72CheckUserMachine> list = inno72CheckUserMachineMapper.selectByCondition(condition);
			int machineFlag = 0;
			if (list != null && list.size() > 0) {
				machineFlag = 1;
			}
			map.put("machineFlag", machineFlag);
			return ResultGenerator.genSuccessResult(map);
		} else {
			return Results.failure("机器有误");
		}
	}

	@Override
	public Result<Inno72Machine> getMachine(Inno72Machine inno72Machine) {
		String machineCode = inno72Machine.getMachineCode();
		if (StringUtil.isEmpty(machineCode)) {
			Results.failure("参数缺失");
		}
		Inno72Machine machine = inno72MachineMapper.getMachineByCode(machineCode);
		return ResultGenerator.genSuccessResult(machine);
	}

	@Override
	public Result<List<Inno72Machine>> getSupplyMachineList(Inno72Machine inno72Machine) {
		String keyword = inno72Machine.getKeyword();
		Inno72CheckUser user = UserUtil.getUser();
		String checkUserId = user.getId();
		Map<String, Object> map = new HashMap<>();
		map.put("checkUserId", checkUserId);
		if (StringUtil.isNotEmpty(keyword)) {
			map.put("keyword", keyword);
		}
		List<Inno72Machine> list = inno72MachineMapper.selectMachineByParam(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> cutApp(CutAppVo vo) {
		String machineCode = vo.getMachineCode();
		SendMessageBean msg = new SendMessageBean();
		msg.setEventType(2);
		msg.setSubEventType(1);
		msg.setMachineId(machineCode);
		List<MachineStartAppBean> sl = new ArrayList<>();
		MachineStartAppBean bean = new MachineStartAppBean();
		bean.setStartStatus(1);
		bean.setAppPackageName(vo.getAppPackageName());
		bean.setAppType(2);
		sl.add(bean);
		msg.setData(sl);
		String url = machineCheckAppBackendProperties.get("sendAppMsgUrl");
		logger.info("切换APP获取环境配置Url:" + url);
		List<SendMessageBean> msgList = new ArrayList<>();
		logger.info("切换app上传参数：{}", JSON.toJSONString(msgList));
		msgList.add(msg);
		try {
			String result = HttpClient.post(url, JSON.toJSONString(msgList));
			logger.info("切换APP执行结果：" + result);
			if (!StringUtil.isEmpty(result)) {
				JSONObject $_result = JSON.parseObject(result);
				if ($_result.getInteger("code") == 0) {
					String r = $_result.getJSONObject("data").getString(machineCode);
					if (!"发送成功".equals(r)) {
						return Results.failure(r);
					}
				} else {
					return Results.failure($_result.getString("msg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("发送失败");
		}
		return ResultGenerator.genSuccessResult();
	}

}
