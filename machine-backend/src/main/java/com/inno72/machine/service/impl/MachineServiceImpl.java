package com.inno72.machine.service.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.service.CheckFaultService;
import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
import com.inno72.common.LogType;
import com.inno72.common.LogUtil;
import com.inno72.common.MachineBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.common.datetime.LocalDateUtil;
import com.inno72.machine.mapper.Inno72AppScreenShotMapper;
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.model.Inno72AppLog;
import com.inno72.machine.model.Inno72AppScreenShot;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.model.Inno72MachineLocaleDetail;
import com.inno72.machine.service.AppLogService;
import com.inno72.machine.service.AppService;
import com.inno72.machine.service.MachineLocaleDetailService;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.AppStatus;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineExceptionVo;
import com.inno72.machine.vo.MachineInstallAppBean;
import com.inno72.machine.vo.MachineListVo;
import com.inno72.machine.vo.MachineListVo1;
import com.inno72.machine.vo.MachineNetInfo;
import com.inno72.machine.vo.MachinePortalVo;
import com.inno72.machine.vo.MachineStartAppBean;
import com.inno72.machine.vo.MachineStatus;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.MachineStockOutInfo;
import com.inno72.machine.vo.PointLog;
import com.inno72.machine.vo.SendMessageBean;
import com.inno72.machine.vo.SystemStatus;
import com.inno72.machine.vo.UpdateMachineChannelVo;
import com.inno72.machine.vo.UpdateMachineVo;
import com.inno72.msg.MsgUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.project.service.ActivityPlanService;
import com.inno72.system.model.Inno72User;
import com.inno72.utils.page.Pagination;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;
	@Autowired
	private SupplyChannelService supplyChannelService;
	@Autowired
	private MongoOperations mongoTpl;
	@Autowired
	private AppService appService;
	@Autowired
	private MachineBackendProperties machineBackendProperties;
	@Autowired
	private CheckFaultService checkFaultService;
	@Autowired
	private Inno72AppScreenShotMapper inno72AppScreenShotMapper;
	@Autowired
	private ActivityPlanService activityPlanService;
	@Autowired
	private AppLogService appLogService;
	@Autowired
	private MachineLocaleDetailService machineLocaleDetailService;
	@Autowired
	private MsgUtil msgUtil;

	@Override
	public Result<List<MachineListVo>> findMachines(String machineCode, String localCode, String startTime,
			String endTime, String machineType, String machineStatus) {
		Map<String, Object> param = new HashMap<>();
		if (StringUtil.isNotEmpty(localCode)) {
			int num = StringUtil.getAreaCodeNum(localCode);
			String likeCode = localCode.substring(0, num);
			param.put("code", likeCode);
			param.put("num", num);
		}
		machineCode = Optional.ofNullable(machineCode).map(a -> a.replace("'", "")).orElse(machineCode);
		param.put("machineCode", machineCode);
		param.put("machineStatus", machineStatus);
		param.put("machineType", machineType);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		List<MachineListVo> machines = inno72MachineMapper.selectMachinesByPage(param);
		return Results.success(machines);
	}

	@Override
	public List<MachineListVo1> findMachinePlan(String machineCode, String state, String localCode, String startTime,
			String endTime) {
		Map<String, Object> param = new HashMap<>();
		if (StringUtil.isNotEmpty(localCode)) {
			int num = StringUtil.getAreaCodeNum(localCode);
			String likeCode = localCode.substring(0, num);
			param.put("code", likeCode);
			param.put("num", num);
		}
		param.put("machineCode", machineCode);
		param.put("state", state);

		if (StringUtil.isNotBlank(startTime) && StringUtil.isNotBlank(endTime)) {
			param.put("startTime", startTime);
			param.put("endTime", endTime + " 23:59:59");
		}

		List<MachineListVo1> machines = inno72MachineMapper.findMachinePlan(param);

		return machines;
	}

	@Override
	public Result<String> updateLocale(String id, String localeId, String address) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = findById(id);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		if (machine.getMachineStatus() < 2) {
			return Results.failure("机器没有初始化不能修改点位");
		}
		String old = machine.getLocaleId();
		machine.setLocaleId(localeId);
		machine.setAddress(address);
		if (machine.getMachineStatus() != 9) {
			machine.setMachineStatus(4);
		}
		machine.setUpdateId(mUser.getId());
		machine.setUpdateTime(LocalDateTime.now());
		machine.setInsideTime(LocalDateTime.now());
		int result = inno72MachineMapper.updateByPrimaryKeySelective(machine);
		if (result != 1) {
			return Results.failure("修改点位失败");
		}
		Inno72MachineLocaleDetail model = new Inno72MachineLocaleDetail();
		model.setId(StringUtil.getUUID());
		model.setCreateId(mUser.getId());
		model.setLocale(localeId);
		model.setOldLocale(old);
		model.setMachineId(id);
		machineLocaleDetailService.save(model);
		return Results.success();

	}

	@Override
	public Result<List<ChannelListVo>> channelList(String id) {
		Inno72Machine machine = findById(id);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		return supplyChannelService.findChannelListByMachineId(id);
	}

	@Override
	public Result<String> deleteChannel(List<UpdateMachineChannelVo> channels) {
		logger.info("停用渠道传入json{}", JSON.toJSONString(channels));
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Result<String> result = supplyChannelService.deleteChannel(channels);

		if (result.getCode() == Result.SUCCESS) {
			Inno72Machine machine = findBy("id", result.getData());
			if (machine != null) {
				machine.setUpdateId(mUser.getId());
				machine.setUpdateTime(LocalDateTime.now());
				inno72MachineMapper.updateByPrimaryKeySelective(machine);
			}

		}
		return result;

	}

	@Override
	public Result<String> updateGoodsCount(List<UpdateMachineChannelVo> channels) {
		logger.info("更新商品个数传入json{}", JSON.toJSONString(channels));
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Result<String> result = supplyChannelService.updateGoodsCount(channels);
		if (result.getCode() == Result.SUCCESS) {
			Inno72Machine machine = findById(result.getData());
			if (machine != null) {
				machine.setUpdateId(mUser.getId());
				machine.setUpdateTime(LocalDateTime.now());
				inno72MachineMapper.updateByPrimaryKeySelective(machine);
			}

		}
		return result;

	}

	@Override
	public Result<List<MachineNetInfo>> updateMachineListNetStatus(List<MachineNetInfo> list) {

		for (MachineNetInfo machineNetInfo : list) {
			String machineCode = machineNetInfo.getMachineCode();
			Integer netStatus = machineNetInfo.getNetStatus();
			Condition condition = new Condition(Inno72Machine.class);
			condition.createCriteria().andEqualTo("machineCode", machineCode);
			List<Inno72Machine> machine = findByCondition(condition);
			if (null != machine && machine.size() > 0) {
				for (Inno72Machine inno72Machine : machine) {
					if (!inno72Machine.getNetStatus().equals(netStatus)) {
						inno72Machine.setNetStatus(netStatus);
						inno72MachineMapper.updateByPrimaryKeySelective(inno72Machine);
					}
				}

			} else {
				logger.info("机器id传入错误");
			}
		}
		return Results.success(list);
	}

	@Override
	public Result<MachineStatusVo> machineStatus(String machineId) {
		Inno72Machine machine = findById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		MachineStatusVo result = new MachineStatusVo();
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machine.getMachineCode()));
		List<MachineStatus> ms = mongoTpl.find(query, MachineStatus.class, "MachineStatus");
		if (ms != null && !ms.isEmpty()) {
			result.setMachineStatus(ms.get(0));
		}
		List<SystemStatus> ss = mongoTpl.find(query, SystemStatus.class, "SystemStatus");
		if (ss != null && !ss.isEmpty()) {
			result.setSystemStatus(ss.get(0));
		}
		Condition condition = new Condition(Inno72AppScreenShot.class);
		condition.createCriteria().andEqualTo("machineCode", machine.getMachineCode());
		condition.orderBy("createTime").desc();
		Pagination pagination = new Pagination();
		pagination.setPageNo(1);
		pagination.setPageSize(10);
		Pagination.threadLocal.set(pagination);
		List<Inno72AppScreenShot> imgs = inno72AppScreenShotMapper.selectByConditionByPage(condition);
		result.setImgs(imgs);
		return Results.success(result);
	}

	@Override
	public Result<MachineAppStatus> appStatus(String machineId) {
		Inno72Machine machine = findById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machine.getMachineCode()));
		List<MachineAppStatus> ms = mongoTpl.find(query, MachineAppStatus.class, "MachineAppStatus");
		if (ms != null && !ms.isEmpty()) {
			MachineAppStatus machineAppStatus = ms.get(0);
			for (AppStatus status : machineAppStatus.getStatus()) {
				Inno72App app = appService.findBy("appPackageName", status.getAppPackageName());
				if (app != null) {
					status.setAppType(app.getAppType());
					if (status.getVersionCode() == -1) {
						if (app != null) {
							status.setAppName(app.getAppName());
						}
					}
				}
			}
			return Results.success(machineAppStatus);
		}
		return Results.success();
	}

	@Override
	public Result<String> updateInfo(String machineId, Integer updateStatus) {
		Inno72Machine machine = findById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		SendMessageBean msg = new SendMessageBean();
		msg.setEventType(1);
		msg.setSubEventType(updateStatus);
		msg.setMachineId(machine.getMachineCode());
		if (updateStatus == 2) {
			if (machine.getMachineStatus() == 9) {
				List<String> names = new ArrayList<String>();
				Condition condition = new Condition(Inno72App.class);
				condition.createCriteria().andEqualTo("appBelong", 6);
				List<Inno72App> appAll = appService.findByCondition(condition);
				for (Inno72App app : appAll) {
					names.add(app.getAppPackageName());
				}
				msg.setData(names);
			} else {
				List<String> names = new ArrayList<String>();
				Condition condition = new Condition(Inno72App.class);
				condition.createCriteria().andNotEqualTo("appBelong", 6);
				List<Inno72App> appAll = appService.findByCondition(condition);
				for (Inno72App app : appAll) {
					names.add(app.getAppPackageName());
				}
				msg.setData(names);
			}
		}
		return sendMsg(machine.getMachineCode(), msg);
	}

	@Override
	public Result<String> cutApp(String machineId, String appPackageName) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = findById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machine.getMachineCode()));
		List<MachineAppStatus> ms = mongoTpl.find(query, MachineAppStatus.class, "MachineAppStatus");
		if (ms != null && !ms.isEmpty()) {
			String name1 = "未知";
			String version1 = "未知版本";
			String name2 = "未知";
			String version2 = "未知版本";
			MachineAppStatus machineAppStatus = ms.get(0);
			SendMessageBean msg = new SendMessageBean();
			msg.setEventType(2);
			msg.setSubEventType(1);
			msg.setMachineId(machine.getMachineCode());
			List<MachineStartAppBean> sl = new ArrayList<>();
			for (AppStatus status : machineAppStatus.getStatus()) {
				if (status.getAppStatus() == 1) {
					name1 = status.getAppName();
					version1 = status.getVersionName();
				}
				MachineStartAppBean bean = new MachineStartAppBean();
				bean.setAppPackageName(status.getAppPackageName());
				Inno72App app = appService.findBy("appPackageName", status.getAppPackageName());
				bean.setAppType(app.getAppType());
				if (appPackageName.equals(status.getAppPackageName()) || app.getAppType() == 1) {
					bean.setStartStatus(1);
				} else {
					bean.setStartStatus(2);
				}
				if (appPackageName.equals(status.getAppPackageName())) {
					name2 = status.getAppName();
					version2 = status.getVersionName();
				}
				sl.add(bean);
			}
			msg.setData(sl);
			String m = "用户{0}，在erp系统中将运行的App由{1}{2}切换为{3}{4}";
			String mm = MessageFormat.format(m, mUser.getName(), name1, version1, name2, version2);
			LogUtil.logger(LogType.CUT_APP.getCode(), machine.getMachineCode(), mm);
			return sendMsg(machine.getMachineCode(), msg);
		}
		return Results.failure("发送失败");
	}

	@Override
	public Result<String> installApp(String machineId, String appPackageName, String url, Integer versionCode) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = findById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		SendMessageBean msg = new SendMessageBean();
		msg.setEventType(2);
		msg.setSubEventType(2);
		msg.setMachineId(machine.getMachineCode());
		List<MachineInstallAppBean> il = new ArrayList<>();
		MachineInstallAppBean bean = new MachineInstallAppBean();
		bean.setAppPackageName(appPackageName);
		bean.setUrl(url);
		bean.setVersionCode(versionCode);
		il.add(bean);
		msg.setData(il);
		Inno72App app = appService.findBy("appPackageName", appPackageName);
		String m = "用户{0}，在erp系统中升级{1}，升级版本为{2}";
		String mm = MessageFormat.format(m, mUser.getName(), app.getAppName(), versionCode);
		LogUtil.logger(LogType.MACHINE_INSTALL.getCode(), machine.getMachineCode(), mm);
		return sendMsg(machine.getMachineCode(), msg);
	}

	@Override
	public Result<List<String>> findMachineByMachineStatus(int machineStatus) {

		List<String> list = inno72MachineMapper.findMachineByMachineStatus(machineStatus);

		return Results.success(list);
	}

	private Result<String> sendMsg(String machineCode, SendMessageBean... beans) {
		String url = machineBackendProperties.get("sendAppMsgUrl");
		try {
			String result = HttpClient.post(url, JSON.toJSONString(beans));
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
		return Results.success();
	}

	@Override
	public Result<MachinePortalVo> findMachinePortalData() {
		MachinePortalVo vo = new MachinePortalVo();
		Condition condition1 = new Condition(Inno72Machine.class);
		condition1.createCriteria().andEqualTo("machineStatus", 4).andEqualTo("netStatus", 0);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition1);
		vo.setOffline(machines.size());
		vo.setDropGoodsSwitchException(findExceptionMachine(2).getData().size());
		vo.setChannelException(findExceptionMachine(4).getData().size());
		vo.setLockCount(findExceptionMachine(5).getData().size());
		vo.setTrafficCount(findExceptionMachine(6).getData().size());
		vo.setSdCount(findExceptionMachine(7).getData().size());
		List<MachineExceptionVo> stockOutVos = inno72MachineMapper.findStockOutMachines();
		vo.setStockout(stockOutVos == null ? 0 : stockOutVos.size());
		Condition condition = new Condition(Inno72CheckFault.class);
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		condition.createCriteria().andIn("status", list);
		int waitConfirm = 0;
		int processed = 0;
		int waitOrder = 0;
		List<Inno72CheckFault> faults = checkFaultService.findByCondition(condition);
		for (Inno72CheckFault fault : faults) {
			switch (fault.getStatus()) {
			case 1:
				waitOrder += 1;
				break;
			case 2:
				processed += 1;
				break;
			case 3:
				waitConfirm += 1;
				break;
			default:
				break;
			}
		}
		int paiActivityCount = activityPlanService.selectPaiYangActivityCount();
		vo.setPaiActivityCount(paiActivityCount);
		vo.setWaitConfirm(waitConfirm);
		vo.setProcessed(processed);
		vo.setWaitOrder(waitOrder);
		return Results.success(vo);
	}

	@Override
	public Result<List<MachineExceptionVo>> findExceptionMachine(Integer type) {
		Map<String, Object> param = new HashMap<>();

		if (type == 1) {

			List<SystemStatus> netList = mongoTpl.find(new Query(), SystemStatus.class, "SystemStatus");
			Map<String, String> machines = new HashMap<>();
			for (SystemStatus machineLogInfo : netList) {
				LocalDateTime createTime = machineLogInfo.getCreateTime();
				machines.put(machineLogInfo.getMachineId(), DateUtil.toTimeStr(createTime, DateUtil.DF_FULL_S1));
			}
			param.put("type", 1);
			List<MachineExceptionVo> exceptionVos = inno72MachineMapper.findMachines(param);
			Iterator<MachineExceptionVo> it = exceptionVos.iterator();
			while (it.hasNext()) {
				MachineExceptionVo vo = it.next();
				vo.setOfflineTime(Optional.ofNullable(machines.get(vo.getMachineCode())).orElse("未知"));
			}
			Collections.sort(exceptionVos);
			return Results.success(exceptionVos);
		} else if (type == 2) {
			param.put("type", 2);

			List<MachineStatus> statusList = mongoTpl.find(new Query(), MachineStatus.class, "MachineStatus");
			Map<String, MachineStatus> exception = new HashMap<>();
			for (MachineStatus machineStatus : statusList) {
				if (machineStatus.getDropGoodsSwitch() == 0) {
					exception.put(machineStatus.getMachineId(), machineStatus);
				}
			}
			List<MachineExceptionVo> exceptionVos = inno72MachineMapper.findMachines(param);
			List<MachineExceptionVo> result = new ArrayList<>();
			Iterator<MachineExceptionVo> it2 = exceptionVos.iterator();
			while (it2.hasNext()) {
				MachineExceptionVo vo = it2.next();
				if (exception.containsKey(vo.getMachineCode())) {
					MachineStatus status = exception.get(vo.getMachineCode());
					vo.setDropGoodsSwitch(status.getDropGoodsSwitch());
					vo.setUpdateTime(DateUtil.toTimeStr(status.getCreateTime(), DateUtil.DF_FULL_S1));
					result.add(vo);
				}
			}
			return Results.success(result);
		} else if (type == 3) {
			List<MachineExceptionVo> stockOutVos = inno72MachineMapper.findStockOutMachines();
			return Results.success(stockOutVos);
		} else if (type == 4) {
			param.put("type", 4);

			List<MachineStatus> statusList = mongoTpl.find(new Query(), MachineStatus.class, "MachineStatus");
			Map<String, MachineStatus> exception = new HashMap<>();
			for (MachineStatus machineStatus : statusList) {
				if (!StringUtils.isEmpty(machineStatus.getGoodsChannelStatus())
						&& !"[]".equals(machineStatus.getGoodsChannelStatus())) {
					exception.put(machineStatus.getMachineId(), machineStatus);
				}

			}
			List<MachineExceptionVo> exceptionVos = inno72MachineMapper.findMachines(param);
			List<MachineExceptionVo> result = new ArrayList<>();
			Iterator<MachineExceptionVo> it1 = exceptionVos.iterator();
			while (it1.hasNext()) {
				MachineExceptionVo vo = it1.next();
				if (exception.containsKey(vo.getMachineCode())) {
					MachineStatus status = exception.get(vo.getMachineCode());
					String channelStatus = Optional.ofNullable(status.getGoodsChannelStatus())
							.map(a -> a.replace("[]", "")).orElse("");
					vo.setGoodsChannelStatus(channelStatus);
					vo.setUpdateTime(DateUtil.toTimeStr(status.getCreateTime(), DateUtil.DF_FULL_S1));
					result.add(vo);
				}
			}
			return Results.success(result);
		} else if (type == 5) {
			List<MachineExceptionVo> exceptionVos = inno72MachineMapper.findMachineLocked();
			return Results.success(exceptionVos);
		} else if (type == 6) {
			Query query = new Query();
			query.addCriteria(Criteria.where("createTime").gte(getStartTime()).lte(getEndTime()));
			query.addCriteria(Criteria.where("thatdayTraffic").gte(300));
			query.with(new Sort(Sort.Direction.DESC, "createTime"));
			List<SystemStatus> systemStatuss = mongoTpl.find(query, SystemStatus.class, "SystemStatus");
			List<MachineExceptionVo> exceptionVos = new ArrayList<>();
			if (systemStatuss != null) {
				for (SystemStatus status : systemStatuss) {
					MachineExceptionVo vo = new MachineExceptionVo();
					vo.setMachineCode(status.getMachineId());
					vo.setAllTraffic(status.getAllTraffic());
					vo.setMonthTraffic(status.getMonthTraffic());
					vo.setThatdayTraffic(status.getThatdayTraffic());
					exceptionVos.add(vo);
				}
			}
			return Results.success(exceptionVos);
		} else if (type == 7) {
			Query query = new Query();
			query.addCriteria(Criteria.where("sdFree").lte(1500));
			query.with(new Sort(Sort.Direction.DESC, "createTime"));
			List<SystemStatus> systemStatuss = mongoTpl.find(query, SystemStatus.class, "SystemStatus");
			List<MachineExceptionVo> exceptionVos = new ArrayList<>();
			if (systemStatuss != null) {
				for (SystemStatus status : systemStatuss) {
					MachineExceptionVo vo = new MachineExceptionVo();
					vo.setMachineCode(status.getMachineId());
					vo.setSdFree(status.getSdFree());
					vo.setSdTotle(status.getSdTotle());
					exceptionVos.add(vo);
				}
			}
			return Results.success(exceptionVos);
		} else {
			return Results.failure("参数传入错误");
		}

	}

	@Override
	public Result<List<MachineStockOutInfo>> findMachineStockoutInfo(String machineId) {
		List<MachineStockOutInfo> result = inno72MachineMapper.findMachineStockoutInfo(machineId);
		return Results.success(result);
	}

	@Override
	public Result<String> updateMachine(UpdateMachineVo vo) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = findById(vo.getMachineId());
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		if (machine.getMachineStatus() < 2) {
			return Results.failure("机器没有初始化不能修改机器信息");
		}
		String machineId = Optional.ofNullable(vo).map(a -> a.getMachineId()).orElse("");
		String localId = Optional.ofNullable(vo).map(a -> a.getLocaleId()).orElse("");
		String monitorStart = Optional.ofNullable(vo).map(a -> a.getMonitorStart()).orElse("");
		String monitorEnd = Optional.ofNullable(vo).map(a -> a.getMonitorEnd()).orElse("");
		machine.setLocaleId(localId);
		machine.setMonitorStart(monitorStart);
		machine.setMonitorEnd(monitorEnd);
		machine.setUpdateId(mUser.getId());
		machine.setOpenStatus(vo.getOpenStatus());
		machine.setUpdateTime(LocalDateTime.now());
		machine.setId(machineId);
		machine.setMachineStatus(4);
		machine.setUpdateId(mUser.getId());
		int result = inno72MachineMapper.updateByPrimaryKeySelective(machine);
		if (result != 1) {
			return Results.failure("修改机器失败");
		}
		return Results.success();
	}

	@Override
	public Result<String> updateMachineCode(String machineId, String machineCode) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (machine == null) {
			return Results.failure("机器id不存在");
		}
		if (machine.getMachineCode().equals(machineCode)) {
			return Results.failure("机器编号相同");
		}
		Condition condition = new Condition(Inno72Machine.class);
		condition.createCriteria().andEqualTo("machineCode", machineCode);
		List<Inno72Machine> machines = inno72MachineMapper.selectByCondition(condition);
		if (machines == null || machines.isEmpty()) {
			return Results.failure("机器编号不存在");
		}
		Inno72Machine old = machines.get(0);
		if (!machine.getLocaleId().equals(old.getLocaleId())) {
			return Results.failure("机器点位不一致不能修改编号");
		}
		old.setBluetoothAddress(machine.getBluetoothAddress());
		old.setDeviceId(machine.getDeviceId());
		old.setUpdateTime(LocalDateTime.now());
		old.setUpdateId(mUser.getId());
		inno72MachineMapper.updateByPrimaryKeySelective(old);

		SendMessageBean msg = new SendMessageBean();
		msg.setEventType(1);
		msg.setSubEventType(4);
		msg.setMachineId(machine.getMachineCode());
		msg.setData(machineCode);
		String m = "用户{0}，在erp系统中修改机器编号，修改前{1}，修改后{2}";
		String mm = MessageFormat.format(m, mUser.getName(), machine.getMachineCode(), machineCode);
		LogUtil.logger(LogType.UPDATE_MACHINECODE.getCode(), machineCode, mm);
		return sendMsg(machine.getMachineCode(), msg);
	}

	@Override
	public Result<Inno72Machine> findMachineInfoById(String machineId) {
		Inno72Machine machine = inno72MachineMapper.findMachineInfoById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		return Results.success(machine);
	}

	@Override
	public Result<String> cutDesktop(String machineId, Integer status) {
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (machine == null) {
			return Results.failure("机器id不存在");
		}
		String key = "com.inno72.monitorapp/.services.DetectionService";
		if (machine.getMachineStatus() == 9) {
			key = "com.inno72.monitorapp.tmall/.services.DetectionService";
		}
		if (status == 1) {
			Map<String, Object> map = new HashMap<>();
			map.put("machineCode", machine.getMachineCode());
			Map<String, Object> pmap = new HashMap<>();
			pmap.put("type", 1);
			pmap.put("data", "am stopservice -n " + key);
			map.put("msg", pmap);
			sendMsgStr(machine.getMachineCode(), JSON.toJSONString(map));
			pmap.put("data", "input keyevent 3");
			map.put("msg", pmap);
			return sendMsgStr(machine.getMachineCode(), JSON.toJSONString(map));
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("machineCode", machine.getMachineCode());
			Map<String, Object> pmap = new HashMap<>();
			pmap.put("type", 1);
			pmap.put("data", "am startservice -n " + key);
			map.put("msg", pmap);
			return sendMsgStr(machine.getMachineCode(), JSON.toJSONString(map));
		}
	}

	@Override
	public Result<List<PointLog>> machinePointLog(String machineCode, String startTime, String endTime) {

		if (StringUtil.isEmpty(machineCode)) {
			return Results.failure("机器编码不存在!");
		}

		Query query = new Query();

		query.addCriteria(Criteria.where("machineCode").is(machineCode));

		boolean isStartTime = StringUtil.isEmpty(startTime);
		boolean isEndTime = StringUtil.isEmpty(endTime);
		// 都没有值 倒序取1000条
		if (!isStartTime && isEndTime) {

			query.with(new Sort(Sort.Direction.ASC, "pointTime"));
			query.addCriteria(Criteria.where("pointTime").gt(startTime));

		} else if (isStartTime && !isEndTime) {

			query.with(new Sort(Sort.Direction.DESC, "pointTime"));
			query.addCriteria(Criteria.where("pointTime").lt(endTime));
			query.limit(200);

		} else if (isStartTime && isEndTime) {

			query.with(new Sort(Sort.Direction.DESC, "pointTime"));
			query.limit(200);

		} else if (!isStartTime && !isEndTime) {
			try {
				LocalDate startLocalDate = LocalDateUtil.transfer(startTime);
				LocalDate endLocalDate = LocalDateUtil.transfer(endTime);
				if (startLocalDate.isAfter(endLocalDate)) {
					return Results.failure("结束时间不能比开始时间小！");
				}
			} catch (Exception e) {
				return Results.failure("导出传入日期格式错误! (ex: yyyy-MM-dd)");
			}

			query.with(new Sort(Sort.Direction.ASC, "pointTime"));
			query.addCriteria(Criteria.where("pointTime").lt(endTime + " 23:59:59").gt(startTime));

		}

		List<PointLog> pointLogs = mongoTpl.find(query, PointLog.class, "PointLog");
		if (isStartTime) {
			if (pointLogs.size() > 1) {
				Collections.reverse(pointLogs);
			}
		}

		return Results.success(pointLogs);
	}

	private Result<String> sendMsgStr(String machineCode, String json) {
		String url = machineBackendProperties.get("sendAppMsgStrUrl");
		try {
			String result = HttpClient.post(url, json);
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
		return Results.success();
	}

	@Override
	public Result<String> updateTemperature(String machineId, Integer temperature) {
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (machine == null) {
			return Results.failure("机器id不存在");
		}
		SendMessageBean msg = new SendMessageBean();
		msg.setEventType(1);
		msg.setSubEventType(5);
		msg.setMachineId(machine.getMachineCode());
		msg.setData(temperature);
		return sendMsg(machine.getMachineCode(), msg);
	}

	@Override
	public Result<String> findTemperature(String machineId) {
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (machine == null) {
			return Results.failure("机器id不存在");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machine.getMachineCode()));
		List<MachineStatus> statusList = mongoTpl.find(query, MachineStatus.class, "MachineStatus");
		if (statusList == null || statusList.isEmpty() || StringUtil.isBlank(statusList.get(0).getTemperature())) {
			return Results.success("-1");
		}
		return Results.success(statusList.get(0).getTemperature());
	}

	@Override
	public Result<String> grabLog(String machineId, Integer logType, String startTime, String endTime) {
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (machine == null) {
			return Results.failure("机器id不存在");
		}
		Map<String, Object> param = new HashMap<>();
		param.put("pushType", 2);
		Map<String, Object> msgInfo = new HashMap<>();

		Map<String, Object> info = new HashMap<>();
		info.put("logType", logType);
		info.put("startTime", startTime);
		info.put("endTime", endTime);

		List<Map<String, Object>> infos = new ArrayList<>();
		infos.add(info);
		msgInfo.put("packInfo", infos);
		param.put("msgInfo", msgInfo);

		Map<String, String> params = new HashMap<>();
		params.put("msg", JSON.toJSONString(param));

		if (machine.getMachineStatus() == 9) {
			msgUtil.sendPush("push_android_tm_transmission_common", params, machine.getMachineCode(),
					"machine-backend--grabLog", "获取日志", "获取日志");
			return Results.success();
		}
		msgUtil.sendPush("push_android_transmission_common", params, machine.getMachineCode(),
				"machine-backend--grabLog", "获取日志", "获取日志");
		return Results.success();
	}

	@Override
	public Result<List<Inno72AppLog>> getLogs(String machineId) {
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (machine == null) {
			return Results.failure("机器id不存在");
		}
		Condition condition = new Condition(Inno72AppLog.class);
		condition.createCriteria().andEqualTo("machineCode", machine.getMachineCode());
		condition.orderBy("reciveTime").desc();
		List<Inno72AppLog> list = appLogService.findByCondition(condition);
		return Results.success(list);
	}

	private static Date getStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();
	}

	private static Date getEndTime() {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();

	}
}
