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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.MachineBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.AppService;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.service.SupplyChannelService;
import com.inno72.machine.vo.AppStatus;
import com.inno72.machine.vo.ChannelListVo;
import com.inno72.machine.vo.MachineAppStatus;
import com.inno72.machine.vo.MachineInstallAppBean;
import com.inno72.machine.vo.MachineNetInfo;
import com.inno72.machine.vo.MachineStartAppBean;
import com.inno72.machine.vo.MachineStatus;
import com.inno72.machine.vo.MachineStatusVo;
import com.inno72.machine.vo.SendMessageBean;
import com.inno72.machine.vo.SystemStatus;
import com.inno72.machine.vo.UpdateMachineChannelVo;
import com.inno72.plugin.http.HttpClient;
import com.inno72.system.model.Inno72User;
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
	@Autowired
	private SupplyChannelService supplyChannelService;
	@Autowired
	private MongoOperations mongoTpl;
	@Autowired
	private AppService appService;
	@Autowired
	private MachineBackendProperties machineBackendProperties;

	// @Override
	// public Result<String> initMachine(String deviceId, String channelJson) {
	// Inno72Machine initMachine = findBy("deviceId", deviceId);
	// if (initMachine != null) {
	// return Results.success(initMachine.getMachineCode());
	// }
	// String machineCode = StringUtil.getMachineCode();
	// LocalDateTime now = LocalDateTime.now();
	// Inno72Machine machine = new Inno72Machine();
	// String machineId = StringUtil.getUUID();
	// machine.setDeviceId(deviceId);
	// machine.setId(machineId);
	// machine.setMachineCode(machineCode);
	// machine.setMachineStatus(Inno72Machine.Machine_Status.INFACTORY.v());
	// machine.setUpdateId("machine-backend");
	// machine.setCreateId("machine-backend");
	// machine.setCreateTime(now);
	// machine.setUpdateTime(now);
	// machine.setNetStatus(0);
	// int result = inno72MachineMapper.insert(machine);
	// if (result != 1) {
	// return Results.failure("生成machineCode失败");
	// }
	// List<Inno72SupplyChannel> channels = JSON.parseArray(channelJson,
	// Inno72SupplyChannel.class);
	// Result<String> initResult = supplyChannelService.initChannel(machineId,
	// channels);
	// if (initResult.getCode() != Result.SUCCESS) {
	// return initResult;
	// }
	// return Results.success(machineCode);
	// }

	@Override
	public Result<String> updateNetStatus(String machineCode, Integer netStatus) {
		Inno72Machine machine = findBy("machineCode", machineCode);
		if (machine != null) {
			if (!machine.getNetStatus().equals(netStatus)) {
				machine.setNetStatus(netStatus);
				inno72MachineMapper.updateByPrimaryKeySelective(machine);
			}
		} else {
			return Results.failure("机器code传入错误");
		}
		return Results.success();
	}

	@Override
	public Result<List<Inno72Machine>> findMachines(String machineCode, String localCode) {
		Map<String, Object> param = new HashMap<>();
		if (StringUtil.isNotEmpty(localCode)) {
			int num = getlikeCode(localCode);
			if (num < 4) {
				num = 3;
			}
			String likeCode = localCode.substring(0, num);
			param.put("code", likeCode);
			param.put("num", num);
		}
		param.put("machineCode", machineCode);

		List<Inno72Machine> machines = inno72MachineMapper.selectMachinesByPage(param);
		return Results.success(machines);
	}

	public int getlikeCode(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				return i + 1;
			}
		}
		return 0;
	}

	@Override
	public Result<String> updateLocale(String id, String localeId, String address) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Inno72Machine machine = findById(id);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		machine.setLocaleId(localeId);
		machine.setAddress(address);
		machine.setUpdateId(mUser.getId());
		machine.setUpdateTime(LocalDateTime.now());
		int result = inno72MachineMapper.updateByPrimaryKeySelective(machine);
		if (result != 1) {
			return Results.failure("修改点位失败");
		}
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
		SessionData session = CommonConstants.SESSION_DATA;
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
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		Result<String> result = supplyChannelService.updateGoodsCount(channels);
		if (result.getCode() == Result.SUCCESS) {
			Inno72Machine machine = findBy("machineCode", result.getData());
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
				status.setAppType(app.getAppType());
				if (status.getVersionCode() == -1) {
					if (app != null) {
						status.setAppName(app.getAppName());
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
			List<String> names = new ArrayList<String>();
			List<Inno72App> appAll = appService.findAll();
			for (Inno72App app : appAll) {
				names.add(app.getAppPackageName());
			}
			msg.setData(names);
		}
		return sendMsg(machine.getMachineCode(), msg);
	}

	@Override
	public Result<String> cutApp(String machineId, String appPackageName) {
		Inno72Machine machine = findById(machineId);
		if (machine == null) {
			return Results.failure("机器id传入错误");
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("machineId").is(machine.getMachineCode()));
		List<MachineAppStatus> ms = mongoTpl.find(query, MachineAppStatus.class, "MachineAppStatus");
		if (ms != null && !ms.isEmpty()) {
			MachineAppStatus machineAppStatus = ms.get(0);
			SendMessageBean msg = new SendMessageBean();
			msg.setEventType(2);
			msg.setSubEventType(1);
			msg.setMachineId(machine.getMachineCode());
			List<MachineStartAppBean> sl = new ArrayList<>();
			for (AppStatus status : machineAppStatus.getStatus()) {
				MachineStartAppBean bean = new MachineStartAppBean();
				bean.setAppPackageName(status.getAppPackageName());
				Inno72App app = appService.findBy("appPackageName", status.getAppPackageName());
				bean.setAppType(app.getAppType());
				if (appPackageName.equals(status.getAppPackageName()) || app.getAppType() == 1) {
					bean.setStartStatus(1);
				} else {
					bean.setStartStatus(2);
				}
				sl.add(bean);
			}
			msg.setData(sl);
			return sendMsg(machine.getMachineCode(), msg);
		}
		return Results.failure("发送失败");
	}

	@Override
	public Result<String> installApp(String machineId, String appPackageName, String url, Integer versionCode) {
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

}
