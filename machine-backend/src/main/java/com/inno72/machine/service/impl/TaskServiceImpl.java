package com.inno72.machine.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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
import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
import com.inno72.common.LogType;
import com.inno72.common.LogUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.machine.mapper.Inno72AppMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.mapper.Inno72TaskMachineMapper;
import com.inno72.machine.mapper.Inno72TaskMapper;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.model.Inno72Task;
import com.inno72.machine.service.AppService;
import com.inno72.machine.service.TaskService;
import com.inno72.machine.vo.AppVersion;
import com.inno72.machine.vo.Inno72TaskMachineVo;
import com.inno72.machine.vo.Inno72TaskVo;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.project.vo.Inno72MachineVo;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/08/24.
 */
@Service
@Transactional
public class TaskServiceImpl extends AbstractService<Inno72Task> implements TaskService {
	private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Resource
	private Inno72TaskMapper inno72TaskMapper;

	@Resource
	private Inno72AppMapper inno72AppMapper;

	@Resource
	private Inno72TaskMachineMapper inno72TaskMachineMapper;
	@Resource
	private Inno72MachineMapper inno72MachineMapper;
	@Autowired
	private AppService appService;

	@Autowired
	private MongoOperations mongoTpl;

	// yyyy-MM-dd HH:mm
	private String timeRegex = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9])$";

	@Override
	public Result<String> saveTsak(Inno72TaskVo task) {
		logger.info("新建任务参数:{}", JSON.toJSONString(task));
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		try {
			// 1 app升级,2 app卸载，3合并货道 4 拆分货道
			int type = task.getType();
			if (1 == type) {
				if (StringUtil.isBlank(task.getAppId())) {
					logger.info("请选择APP");
					return Results.failure("请选择APP");
				}
				Inno72App app = inno72AppMapper.selectByPrimaryKey(task.getAppId());
				Query query = new Query();
				query.addCriteria(Criteria.where("appPackageName").is(app.getAppPackageName()));
				AppVersion appVersion = mongoTpl.findOne(query, AppVersion.class);

				task.setApp(app.getAppPackageName());
				task.setAppUrl(appVersion.getDownloadUrl());
				task.setAppVersion(appVersion.getAppVersion());

			}
			if (2 == type) {
				if (StringUtil.isBlank(task.getAppId())) {
					logger.info("请选择APP");
					return Results.failure("请选择APP");
				}
				Inno72App app = inno72AppMapper.selectByPrimaryKey(task.getAppId());
				task.setApp(app.getAppPackageName());
			}

			if (null == task.getDoType()) {
				return Results.failure("请选择执行方式");
			}
			if (StringUtil.isNotBlank(task.getDoTimeStr())) {
				boolean b1 = Pattern.matches(timeRegex, task.getDoTimeStr());
				if (!b1) {
					return Results.failure("执行时间应格式化到分");
				}
				task.setDoTime(DateUtil.toDateTime(task.getDoTimeStr() + ":00", DateUtil.DF_FULL_S1));
			}

			List<Inno72TaskMachineVo> machineList = task.getMachineList();
			if (null == machineList || machineList.size() < 0) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}
			if (3 == type || 4 == type) {
				if (StringUtil.isBlank(task.getChannelCode())) {
					logger.info("请选要操作的货道");
					return Results.failure("请选要操作的货道");
				}
			}

			task.setStatus(0);
			task.setId(StringUtil.getUUID());
			task.setCode("T" + DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S2));
			List<Inno72TaskMachineVo> insertTaskMachinList = new ArrayList<>();
			for (Inno72TaskMachineVo taskMachine : machineList) {
				if (insertTaskMachinList.contains(taskMachine)) {
					logger.info("选择机器有重复");
					return Results.failure("选择机器有重复");
				}
				Inno72TaskMachineVo insertTaskMachine = new Inno72TaskMachineVo();
				insertTaskMachine.setId(StringUtil.getUUID());
				insertTaskMachine.setTaskId(task.getId());
				insertTaskMachine.setMachineId(taskMachine.getMachineId());
				// 查询机器Code
				// Inno72Machine machine =
				// inno72MachineMapper.selectByPrimaryKey(taskMachine.getMachineId());
				insertTaskMachine.setMachineCode(taskMachine.getMachineCode());

				if ((3 == type || 4 == type) && insertTaskMachinList.size() > 0) {
					// 判断是否同一批次
					String lastMachineCode = insertTaskMachinList.get(insertTaskMachinList.size() - 1).getMachineCode();
					if (!insertTaskMachine.getMachineCode().substring(0, 2).equals(lastMachineCode.substring(0, 2))) {
						logger.info("操作货道不是同一批次");
						return Results.failure("操作货道不是同一批次");
					}
				}

				if (type == 1) {
					Inno72App app = appService.findBy("appPackageName", task.getApp());
					String m = "用户{0}，在erp系统中升级{1}，升级版本为{2}";
					String mm = MessageFormat.format(m, mUser.getName(), app.getAppName(), task.getAppVersion());
					LogUtil.logger(LogType.TASK_INSTALL.getCode(), taskMachine.getMachineCode(), mm);
				} else if (type == 2) {
					Inno72App app = appService.findBy("appPackageName", task.getApp());
					String m = "用户{0}，在erp系统中卸载{1}";
					String mm = MessageFormat.format(m, mUser.getName(), app.getAppName());
					LogUtil.logger(LogType.TASK_UNISTALL.getCode(), taskMachine.getMachineCode(), mm);
				}

				insertTaskMachinList.add(insertTaskMachine);
			}
			task.setCreateId(mUser.getId());
			task.setCreateTime(LocalDateTime.now());
			inno72TaskMachineMapper.insertTaskMachineList(insertTaskMachinList);
			super.save(task);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateTsak(Inno72TaskVo task) {
		logger.info("更新任务参数:{}", JSON.toJSONString(task));
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		try {

			Inno72Task model = inno72TaskMapper.selectByPrimaryKey(task.getId());
			// 判断是否可以编辑
			if (2 == model.getStatus() || 3 == model.getStatus()) {
				logger.info("该任务记录不能修改");
				return Results.failure("该任务记录不能修改");
			}
			// 1 app升级,2 app卸载，3合并货道 4 拆分货道
			int type = task.getType();
			if (1 == type) {
				if (StringUtil.isBlank(task.getAppId())) {
					logger.info("请选择APP");
					return Results.failure("请选择APP");
				}
				if (StringUtil.isBlank(task.getAppVersion())) {
					logger.info("请填写升级版本");
					return Results.failure("请填写升级版本");
				}
				if (StringUtil.isBlank(task.getAppUrl())) {
					logger.info("请填写升级链接");
					return Results.failure("请填写升级链接");
				}
				Inno72App app = inno72AppMapper.selectByPrimaryKey(task.getAppId());
				task.setApp(app.getAppPackageName());
			}
			if (2 == type) {
				if (StringUtil.isBlank(task.getAppId())) {
					logger.info("请选择APP");
					return Results.failure("请选择APP");
				}
				Inno72App app = inno72AppMapper.selectByPrimaryKey(task.getAppId());
				task.setApp(app.getAppPackageName());
			}

			if (null == task.getDoType()) {
				return Results.failure("请选择执行方式");
			}

			if (StringUtil.isNotBlank(task.getDoTimeStr())) {
				boolean b1 = Pattern.matches(timeRegex, task.getDoTimeStr());
				if (!b1) {
					return Results.failure("执行时间应格式化到分");
				}
				task.setDoTime((DateUtil.toDateTime(task.getDoTimeStr() + ":00", DateUtil.DF_FULL_S1)));
			} else {
				task.setDoTime(null);
			}

			List<Inno72TaskMachineVo> machineList = task.getMachineList();
			if (null == machineList || machineList.size() < 0) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}
			if (3 == type || 4 == type) {
				if (StringUtil.isBlank(task.getChannelCode())) {
					logger.info("请选要操作的货道");
					return Results.failure("请选要操作的货道");
				}
			}

			List<Inno72TaskMachineVo> insertTaskMachinList = new ArrayList<>();
			for (Inno72TaskMachineVo taskMachine : machineList) {
				if (insertTaskMachinList.contains(taskMachine)) {
					logger.info("选择机器有重复");
					return Results.failure("选择机器有重复");
				}
				Inno72TaskMachineVo insertTaskMachine = new Inno72TaskMachineVo();
				insertTaskMachine.setId(StringUtil.getUUID());
				insertTaskMachine.setTaskId(task.getId());
				insertTaskMachine.setMachineId(taskMachine.getMachineId());
				// 查询机器Code
				// Inno72Machine machine =
				// inno72MachineMapper.selectByPrimaryKey(taskMachine.getMachineId());
				insertTaskMachine.setMachineCode(taskMachine.getMachineCode());

				if ((3 == type || 4 == type) && insertTaskMachinList.size() > 0) {
					// 判断是否同一批次
					String lastMachineCode = insertTaskMachinList.get(insertTaskMachinList.size() - 1).getMachineCode();
					if (!insertTaskMachine.getMachineCode().substring(0, 2).equals(lastMachineCode.substring(0, 2))) {
						logger.info("操作货道不是同一批次");
						return Results.failure("操作货道不是同一批次");
					}
				}

				insertTaskMachinList.add(insertTaskMachine);
			}
			model.setUpdateId(mUser.getId());
			model.setUpdateTime(LocalDateTime.now());
			inno72TaskMachineMapper.deleteByTaskId(task.getId());
			inno72TaskMachineMapper.insertTaskMachineList(insertTaskMachinList);
			super.update(task);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateStatus(String id, Integer status, Integer doType) {

		logger.info("---------------------点位任务-------------------");
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		Inno72Task model = inno72TaskMapper.selectByPrimaryKey(id);
		// 判断是否可以删除
		if (null == status) {
			logger.info("参数有误");
			return Results.failure("请确认执行状态");
		}
		if (null == doType) {
			logger.info("请选择执行方式");
			return Results.failure("请选择执行方式");
		} else {
			model.setDoType(doType);
		}
		if (3 == status) {
			if (2 != model.getStatus()) {
				logger.info("该任务，不能继续执行");
				return Results.failure("该任务，不能继续执行");
			}
		}

		model.setStatus(status);
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());

		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> delById(String id) {

		logger.info("---------------------点位任务-------------------");
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		Inno72Task model = inno72TaskMapper.selectByPrimaryKey(id);
		// 判断是否可以删除
		if (2 == model.getStatus() || 3 == model.getStatus()) {
			logger.info("该任务记录不能删除");
			return Results.failure("该任务记录不能删除");
		}
		model.setStatus(9);
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());

		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Inno72TaskVo findDetail(String id) {
		Inno72TaskVo task = inno72TaskMapper.selectTaskDetail(id);

		List<Inno72TaskMachineVo> machines = task.getMachineList();
		if (null != machines && machines.size() > 0) {
			List<String> pList = new ArrayList<>();
			machines.forEach(machine -> {
				String province = machine.getProvince();
				if (!pList.contains(province)) {
					pList.add(province);
				}
			});
			String r = "已选择" + machines.size() + "台机器，分别位于:" + pList.toString();
			task.setRemark(r);
		}
		if (StringUtil.isNotBlank(task.getChannelCode())) {
			String[] channelArray = task.getChannelCode().split(",");
			StringBuffer channelCodeStr = new StringBuffer();
			for (String channel : channelArray) {
				channelCodeStr.append("货道:").append(channel).append(" ");
			}
			task.setChannelCodeStr(channelCodeStr.toString());
		}
		return task;
	}

	@Override
	public List<Inno72AdminAreaVo> selectAreaMachineList(String code, String level, String machineCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		params.put("machineCode", machineCode);

		if (StringUtil.isNotBlank(level)) {
			if (level.equals("1")) {
				params.put("num", 2);
			} else if (level.equals("2")) {
				params.put("num", 4);
			} else if (level.equals("3")) {
				params.put("num", 6);
			} else if (level.equals("4")) {
				params.put("num", 9);
			}
			params.put("level", level);
		} else {
			int num = StringUtil.getAreaCodeNum(code);

			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}

		List<Inno72AdminAreaVo> list = new ArrayList<>();
		if (StringUtil.isEmpty(level)) {
			list = inno72TaskMapper.selectMachineList(params);
		} else {
			list = inno72TaskMapper.selectAreaMachineList(params);
			for (Inno72AdminAreaVo inno72AdminAreaVo : list) {
				List<Inno72MachineVo> machines = inno72AdminAreaVo.getMachines();
				inno72AdminAreaVo.setTotalNum(machines.size() + "");
			}
		}

		return list;
	}

	@Override
	public List<Inno72TaskVo> findByPage(String type, String status) {
		logger.info("---------------------机器任务分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("type", type);
		params.put("status", status);

		List<Inno72TaskVo> list = inno72TaskMapper.selectByPage(params);
		return list;
	}

	@Override
	public List<Map<String, Object>> selectAppList() {
		logger.info("---------------------机器任务获取app-------------------");
		Map<String, Object> params = new HashMap<String, Object>();

		return inno72TaskMapper.selectAppList(params);
	}

}
