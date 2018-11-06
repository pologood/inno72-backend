package com.inno72.Interact.service.impl;

import java.time.Duration;
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

import com.inno72.Interact.mapper.Inno72InteractMachineGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractMachineMapper;
import com.inno72.Interact.mapper.Inno72InteractMachineTimeMapper;
import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.model.Inno72InteractMachineTime;
import com.inno72.Interact.service.InteractMachineService;
import com.inno72.Interact.vo.InteractMachineTime;
import com.inno72.Interact.vo.MachineActivityVo;
import com.inno72.Interact.vo.MachineTime;
import com.inno72.Interact.vo.MachineVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.redis.IRedisUtil;
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
	private IRedisUtil redisUtil;

	@Resource
	private Inno72InteractMapper inno72InteractMapper;
	@Resource
	private Inno72InteractMachineMapper inno72InteractMachineMapper;

	@Resource
	private Inno72InteractMachineTimeMapper inno72InteractMachineTimeMapper;
	@Resource
	private Inno72InteractMachineGoodsMapper inno72InteractMachineGoodsMapper;

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
				// planMachines.remove(index);
			}
		}
		// interactMachines.addAll(planMachines);

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
			Inno72Interact interact = inno72InteractMapper.selectByPrimaryKey(interactId);
			LocalDateTime runTime = interact.getRunTime();

			List<MachineTime> machines = model.getMachines();
			if (StringUtil.isBlank(model.getQueryStartTime()) || StringUtil.isBlank(model.getQueryStartTime())) {
				logger.info("缺少查询日期参数");
				return Results.failure("缺少查询日期参数");
			}
			if (null == machines || machines.size() < 1) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}
			Map<String, Object> selectParam = new HashMap<String, Object>();
			selectParam.put("id", interactId);
			List<String> selectMachines = inno72InteractMachineMapper.selectInteractUseredMachine(selectParam);

			List<String> useredMachine = new ArrayList<>();
			List<Inno72InteractMachine> insetInteractMachineList = new ArrayList<>();
			List<Inno72InteractMachineTime> insetInteractMachineTimeList = new ArrayList<>();

			for (MachineTime machineTime : machines) {
				String machineId = machineTime.getMachineId();
				if (selectMachines.contains(machineId)) {
					logger.info("此活动下重复选择机器");
					return Results.failure("此活动下重复选择机器，机器编号：" + machineTime.getMachineCode());
				}

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

				if ((null == planTime || planTime.size() < 1) && interactMachine.getState() == 0) {
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
				// 合并连续时间
				insetInteractMachineTimeList = this.mergeContinuTime(insetInteractMachineTimeList);
				// 选择长期
				if (interactMachine.getState() == 1) {
					Inno72InteractMachineTime interactMachineTime = new Inno72InteractMachineTime();
					interactMachineTime.setId(StringUtil.getUUID());
					interactMachineTime.setStartTime(interactMachine.getQueryEndTime().minusSeconds(-1));
					interactMachineTime.setEndTime(DateUtil.toDateTime("2028-12-30 23:59:59", DateUtil.DF_FULL_S1));
					interactMachineTime.setInteractMachineId(interactMachine.getId());

					insetInteractMachineTimeList.add(interactMachineTime);
				}
				// 校验时间内机器是否占用
				for (Inno72InteractMachineTime time : insetInteractMachineTimeList) {
					Map<String, Object> isUseredParam = new HashMap<String, Object>();
					isUseredParam.put("startTime", DateUtil.toTimeStr(time.getStartTime(), DateUtil.DF_FULL_S1));
					isUseredParam.put("endTime", DateUtil.toTimeStr(time.getEndTime(), DateUtil.DF_FULL_S1));
					List<String> usered1 = inno72InteractMachineMapper.selectInteractUseredMachine(isUseredParam);
					List<String> usered2 = inno72InteractMachineMapper.selectPlanUseredMachine(isUseredParam);
					useredMachine.addAll(usered1);
					useredMachine.addAll(usered2);

					if (null == runTime) {
						runTime = time.getStartTime();
					} else if (time.getStartTime().isBefore(runTime)) {
						runTime = time.getStartTime();
					}
				}
				if (useredMachine.contains(machineId)) {
					logger.info("所选时间内机器已占用");
					return Results.failure("所选时间内机器已占用，机器编号：" + machineTime.getMachineCode());
				}

			}

			interact.setRunTime(runTime);
			inno72InteractMapper.updateByPrimaryKeySelective(interact);
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
			Inno72Interact interact = inno72InteractMapper.selectByPrimaryKey(interactId);
			LocalDateTime runTime = interact.getRunTime();
			List<MachineTime> machines = model.getMachines();
			if (StringUtil.isBlank(model.getQueryStartTime()) || StringUtil.isBlank(model.getQueryStartTime())) {
				logger.info("缺少查询日期参数");
				return Results.failure("缺少查询日期参数");
			}
			if (null == machines || machines.size() < 1) {
				logger.info("请选择机器");
				return Results.failure("请选择机器");
			}

			List<String> useredMachine = new ArrayList<>();
			List<Inno72InteractMachineTime> insetInteractMachineTimeList = new ArrayList<>();
			for (MachineTime machineTime : machines) {
				List<Map<String, String>> planTime = machineTime.getPlanTime();
				if ((null == planTime || planTime.size() < 1) && machineTime.getState() == 0) {
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
					if (!interactMachineTime.getEndTime()
							.isEqual(DateUtil.toDateTime("2028-12-30 23:59:59", DateUtil.DF_FULL_S1))) {
						insetInteractMachineTimeList.add(interactMachineTime);
					}
				}
				// 合并连续时间
				insetInteractMachineTimeList = this.mergeContinuTime(insetInteractMachineTimeList);
				// 选择长期
				if (interactMachine.getState() == 1) {
					Inno72InteractMachineTime interactMachineTime = new Inno72InteractMachineTime();
					interactMachineTime.setId(StringUtil.getUUID());
					interactMachineTime.setStartTime(interactMachine.getQueryEndTime().minusSeconds(-1));
					interactMachineTime.setEndTime(DateUtil.toDateTime("2028-12-30 23:59:59", DateUtil.DF_FULL_S1));
					interactMachineTime.setInteractMachineId(interactMachine.getId());

					insetInteractMachineTimeList.add(interactMachineTime);

				}

				// 校验时间内机器是否占用
				for (Inno72InteractMachineTime time : insetInteractMachineTimeList) {
					Map<String, Object> isUseredParam = new HashMap<String, Object>();
					isUseredParam.put("startTime", DateUtil.toTimeStr(time.getStartTime(), DateUtil.DF_FULL_S1));
					isUseredParam.put("endTime", DateUtil.toTimeStr(time.getEndTime(), DateUtil.DF_FULL_S1));
					isUseredParam.put("noId", interactId);
					List<String> usered1 = inno72InteractMachineMapper.selectInteractUseredMachine(isUseredParam);
					List<String> usered2 = inno72InteractMachineMapper.selectPlanUseredMachine(isUseredParam);
					useredMachine.addAll(usered1);
					useredMachine.addAll(usered2);

					if (time.getStartTime().isBefore(runTime)) {
						runTime = time.getStartTime();
					}
				}
				if (useredMachine.contains(machineId)) {
					logger.info("所选时间内机器已占用");
					return Results.failure("所选时间内机器已占用，机器编号：" + machineTime.getMachineCode());
				}
				interact.setRunTime(runTime);
				inno72InteractMapper.updateByPrimaryKeySelective(interact);

				inno72InteractMachineMapper.updateByPrimaryKeySelective(interactMachine);
				Inno72InteractMachineTime delTime = new Inno72InteractMachineTime();
				delTime.setInteractMachineId(interactMachine.getId());
				inno72InteractMachineTimeMapper.delete(delTime);
			}

			// 更新删除机器端资源缓存
			logger.info("更新删除机器端资源缓存");
			redisUtil.deleteByPrex(CommonConstants.REDIS_ACTIVITY_PLAN_CACHE_KEY + interactId + "*");
			inno72InteractMachineTimeMapper.insertInteractMachineTimeList(insetInteractMachineTimeList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	public List<Inno72InteractMachineTime> mergeContinuTime(List<Inno72InteractMachineTime> timeList) {
		logger.info("---------------------机器活动连续时间合并-------------------");

		timeList.sort((Inno72InteractMachineTime t1, Inno72InteractMachineTime t2) -> t1.getStartTime()
				.compareTo(t2.getStartTime()));

		List<Inno72InteractMachineTime> removeList = new ArrayList<>();
		for (int i = 0; i < timeList.size() - 1; i++) {
			if (timeList.get(i).getEndTime().plusSeconds(1).isEqual(timeList.get(i + 1).getStartTime())) {
				timeList.get(i + 1).setStartTime(timeList.get(i).getStartTime());
				removeList.add(timeList.get(i));
			}
		}
		timeList.removeAll(removeList);
		return timeList;
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
		List<MachineActivityVo> aList = this.getContinuTime(interactId, interactMachines);
		interactMachines.setMachineActivity(aList);
		List<MachineVo> planMachines = inno72InteractMachineMapper.selectPlanMachines(pm);
		if (planMachines.contains(interactMachines)) {
			interactMachines.getMachineActivity().addAll(planMachines.get(0).getMachineActivity());
		}

		return interactMachines;
	}

	public List<MachineActivityVo> getContinuTime(String interactId, MachineVo machineVo) {
		logger.info("---------------------机器活动连续时间拆分-------------------");

		List<MachineActivityVo> aList = machineVo.getMachineActivity();
		List<MachineActivityVo> newList = new ArrayList<>();

		for (MachineActivityVo activity : aList) {

			LocalDateTime sTime = DateUtil.toDateTime(activity.getStartTime(), DateUtil.DF_FULL_S1);
			LocalDateTime eTime = DateUtil.toDateTime(activity.getEndTime(), DateUtil.DF_FULL_S1);
			long betweenDay = Duration.between(sTime, eTime).toDays();
			if (betweenDay != 0 && activity.getActivityId().equals(interactId)
					&& !eTime.isEqual(DateUtil.toDateTime("2028-12-30 23:59:59", DateUtil.DF_FULL_S1))) {
				for (int i = 0; i <= betweenDay; i++) {
					MachineActivityVo newAct = new MachineActivityVo();
					newAct.setActivityId(activity.getActivityId());
					newAct.setActivityName(activity.getActivityName());
					newAct.setStartTime(DateUtil.toTimeStr(sTime.plusDays(i), DateUtil.DF_FULL_S1));
					newAct.setEndTime(DateUtil.toTimeStr(sTime.plusDays(i + 1).plusSeconds(-1), DateUtil.DF_FULL_S1));
					newList.add(newAct);
				}

			} else {
				newList.add(activity);
			}

		}
		return newList;
	}

	@Override
	public Result<String> deleteById(String interactId, String machineId) {

		try {
			Inno72InteractMachine interactMachine = new Inno72InteractMachine();
			interactMachine.setInteractId(interactId);
			interactMachine.setMachineId(machineId);
			Inno72InteractMachine base = inno72InteractMachineMapper.selectOne(interactMachine);

			// 活动机器上绑定的商品
			Inno72InteractMachineGoods delGoods = new Inno72InteractMachineGoods();
			delGoods.setInteractMachineId(base.getId());
			// 活动机器时间
			Inno72InteractMachineTime delTime = new Inno72InteractMachineTime();
			delTime.setInteractMachineId(base.getId());

			// 更新删除机器端资源缓存
			logger.info("更新删除机器端资源缓存");
			redisUtil.deleteByPrex(
					CommonConstants.REDIS_ACTIVITY_PLAN_CACHE_KEY + interactId + ":" + base.getMachineCode());

			inno72InteractMachineMapper.delete(interactMachine);
			inno72InteractMachineGoodsMapper.delete(delGoods);
			inno72InteractMachineTimeMapper.delete(delTime);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

}
