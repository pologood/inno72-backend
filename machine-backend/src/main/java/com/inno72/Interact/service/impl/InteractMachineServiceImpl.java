package com.inno72.Interact.service.impl;

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

import com.inno72.Interact.mapper.Inno72InteractMachineMapper;
import com.inno72.Interact.mapper.Inno72InteractMachineTimeMapper;
import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.model.Inno72InteractMachineTime;
import com.inno72.Interact.service.InteractMachineService;
import com.inno72.Interact.vo.InteractMachineTime;
import com.inno72.Interact.vo.MachineTime;
import com.inno72.Interact.vo.MachineVo;
import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMachineServiceImpl extends AbstractService<Inno72InteractMachine>
		implements InteractMachineService {
	private static Logger logger = LoggerFactory.getLogger(InteractMachineServiceImpl.class);
	@Resource
	private Inno72InteractMachineMapper inno72InteractMachineMapper;

	@Resource
	private Inno72InteractMachineTimeMapper inno72InteractMachineTimeMapper;

	@Override
	public List<MachineVo> getList(String keyword, String startTime, String endTime) {
		logger.info("---------------------获取选择机器列表-------------------");

		Map<String, Object> pm = new HashMap<>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		pm.put("keyword", keyword);
		pm.put("startTime", startTime);
		pm.put("endTime", endTime);
		List<MachineVo> planMachines = inno72InteractMachineMapper.selectPlanMachines(pm);
		List<MachineVo> interactMachines = inno72InteractMachineMapper.selectInteractMachines(pm);

		for (MachineVo machine : interactMachines) {
			if (planMachines.contains(machine)) {
				int index = planMachines.indexOf(machine);
				if (planMachines.get(index).getState() == 1) {
					machine.setState(1);
				}
				machine.getMachineActivity().addAll(planMachines.get(index).getMachineActivity());
				planMachines.remove(index);
			}
		}
		interactMachines.addAll(planMachines);

		return interactMachines;
	}

	@Override
	public Result<String> save(InteractMachineTime model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String interactId = model.getInteractId();

			List<MachineTime> machines = model.getMachines();
			if (StringUtil.isBlank(model.getQueryStartTime()) || StringUtil.isBlank(model.getQueryStartTime())) {
				logger.info("缺少查询日期参数");
				return Results.failure("缺少查询日期参数");
			}
			if (null == machines || machines.size() < 1) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}
			List<Inno72InteractMachine> insetInteractMachineList = new ArrayList<>();
			List<Inno72InteractMachineTime> insetInteractMachineTimeList = new ArrayList<>();
			for (MachineTime machineTime : machines) {
				String machineId = machineTime.getMachineId();

				Inno72InteractMachine interactMachine = new Inno72InteractMachine();
				interactMachine.setId(StringUtil.getUUID());
				interactMachine.setInteractId(interactId);
				interactMachine.setMachineId(machineId);
				interactMachine.setMachineCode(machineTime.getMachineCode());
				interactMachine.setQueryStartTime(DateUtil.toDateTime(model.getQueryStartTime(), DateUtil.DF_FULL_S1));
				interactMachine.setQueryEndTime(DateUtil.toDateTime(model.getQueryEndTime(), DateUtil.DF_FULL_S1));
				interactMachine.setState(machineTime.getState());
				insetInteractMachineList.add(interactMachine);
				List<Map<String, String>> planTime = machineTime.getPlanTime();
				if (null == planTime || planTime.size() < 1) {
					logger.info("请确认机器时间");
					return Results.failure("请确认机器时间");
				}
				for (Map<String, String> map : planTime) {
					String startTime = map.get("startTime");
					String endTime = map.get("endTime");
					Inno72InteractMachineTime interactMachineTime = new Inno72InteractMachineTime();
					interactMachineTime.setId(StringUtil.getUUID());
					interactMachineTime.setStartTime(DateUtil.toDateTime(startTime, DateUtil.DF_FULL_S1));
					interactMachineTime.setEndTime(DateUtil.toDateTime(endTime, DateUtil.DF_FULL_S1));
					interactMachineTime.setInteractMachineId(interactMachine.getId());

					insetInteractMachineTimeList.add(interactMachineTime);
				}
			}
			inno72InteractMachineMapper.insertInteractMachineList(insetInteractMachineList);
			inno72InteractMachineTimeMapper.insertInteractMachineTimeList(insetInteractMachineTimeList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(InteractMachineTime model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String interactId = model.getInteractId();

			List<MachineTime> machines = model.getMachines();
			if (StringUtil.isBlank(model.getQueryStartTime()) || StringUtil.isBlank(model.getQueryStartTime())) {
				logger.info("缺少查询日期参数");
				return Results.failure("缺少查询日期参数");
			}
			if (null == machines || machines.size() < 1) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}
			List<Inno72InteractMachineTime> insetInteractMachineTimeList = new ArrayList<>();
			for (MachineTime machineTime : machines) {
				List<Map<String, String>> planTime = machineTime.getPlanTime();
				if (null == planTime || planTime.size() < 1) {
					logger.info("请确认机器时间");
					return Results.failure("请确认机器时间");
				}

				String machineId = machineTime.getMachineId();

				Inno72InteractMachine interactMachine = new Inno72InteractMachine();
				interactMachine.setInteractId(interactId);
				interactMachine.setMachineId(machineId);
				interactMachine = inno72InteractMachineMapper.selectOne(interactMachine);

				interactMachine.setQueryStartTime(DateUtil.toDateTime(model.getQueryStartTime(), DateUtil.DF_FULL_S1));
				interactMachine.setQueryEndTime(DateUtil.toDateTime(model.getQueryEndTime(), DateUtil.DF_FULL_S1));
				interactMachine.setState(machineTime.getState());

				for (Map<String, String> map : planTime) {
					String startTime = map.get("startTime");
					String endTime = map.get("endTime");
					Inno72InteractMachineTime interactMachineTime = new Inno72InteractMachineTime();
					interactMachineTime.setId(StringUtil.getUUID());
					interactMachineTime.setStartTime(DateUtil.toDateTime(startTime, DateUtil.DF_FULL_S1));
					interactMachineTime.setEndTime(DateUtil.toDateTime(endTime, DateUtil.DF_FULL_S1));
					interactMachineTime.setInteractMachineId(interactMachine.getId());

					insetInteractMachineTimeList.add(interactMachineTime);
				}
				inno72InteractMachineMapper.updateByPrimaryKeySelective(interactMachine);
				Inno72InteractMachineTime delTime = new Inno72InteractMachineTime();
				delTime.setInteractMachineId(interactMachine.getId());
				inno72InteractMachineTimeMapper.delete(delTime);
			}

			inno72InteractMachineTimeMapper.insertInteractMachineTimeList(insetInteractMachineTimeList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<MachineVo> getHavingMachines(String interactId, String keyword) {
		logger.info("---------------------获取已添加机器-------------------");

		Map<String, Object> pm = new HashMap<>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		pm.put("keyword", keyword);
		pm.put("interactId", interactId);
		List<MachineVo> interactMachines = inno72InteractMachineMapper.getHavingMachines(pm);

		return interactMachines;
	}

	@Override
	public MachineVo findById(String interactId, String machineId) {

		Map<String, Object> pm = new HashMap<>();
		pm.put("interactId", interactId);
		pm.put("machineId", machineId);

		MachineVo interactMachines = inno72InteractMachineMapper.selectMachineTimeDetail(pm);
		List<MachineVo> planMachines = inno72InteractMachineMapper.selectPlanMachines(pm);

		if (planMachines.contains(interactMachines)) {
			interactMachines.getMachineActivity().addAll(planMachines.get(0).getMachineActivity());
		}

		return interactMachines;
	}

}
