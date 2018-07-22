package com.inno72.project.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityPlanGameResultMapper;
import com.inno72.project.mapper.Inno72ActivityPlanGoodsMapper;
import com.inno72.project.mapper.Inno72ActivityPlanMachineMapper;
import com.inno72.project.mapper.Inno72ActivityPlanMapper;
import com.inno72.project.mapper.Inno72CouponMapper;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.model.Inno72ActivityPlanGameResult;
import com.inno72.project.model.Inno72ActivityPlanGoods;
import com.inno72.project.model.Inno72ActivityPlanMachine;
import com.inno72.project.model.Inno72Coupon;
import com.inno72.project.service.ActivityPlanService;
import com.inno72.project.vo.*;
import com.inno72.system.model.Inno72User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/11.
 */
@Service
@Transactional
public class ActivityPlanServiceImpl extends AbstractService<Inno72ActivityPlan> implements ActivityPlanService {
	private static Logger logger = LoggerFactory.getLogger(ActivityPlanServiceImpl.class);
	
    @Resource
    private Inno72ActivityPlanMapper inno72ActivityPlanMapper;
    @Resource
    private Inno72ActivityPlanMachineMapper inno72ActivityPlanMachineMapper;
    @Resource
    private Inno72CouponMapper inno72CouponMapper;
    @Resource
    private Inno72ActivityPlanGoodsMapper inno72ActivityPlanGoodsMapper;
    
    @Resource
    private Inno72ActivityPlanGameResultMapper inno72ActivityPlanGameResultMapper;
    //yyyy-MM-dd HH:mm
    private String timeRegex = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9])$";


    

	@Override
	public Result<String> saveActPlan(Inno72ActivityPlanVo activityPlan) {
		
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		if (StringUtil.isBlank(activityPlan.getStartTimeStr())||StringUtil.isBlank(activityPlan.getEndTimeStr())) {
			return Results.failure("请选择计划时间");
		}
		boolean b1 = Pattern.matches(timeRegex, activityPlan.getStartTimeStr());
		boolean b2 = Pattern.matches(timeRegex, activityPlan.getEndTimeStr());
		if (!b1 || !b2) {
			return Results.failure("计划时间应格式化到分");
		}
		if (StringUtil.isBlank(activityPlan.getActivityId())) {
			return Results.failure("请选择活动");
		}
		if (StringUtil.isBlank(activityPlan.getGameId())) {
			return Results.failure("请选择游戏");
		}
		if (StringUtil.isBlank(activityPlan.getUserMaxTimes())) {
			return Results.failure("请填写获得最大商品次数");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		try {
			//保存计划信息
			String activityPlanId = StringUtil.getUUID();
			activityPlan.setId(activityPlanId);
			activityPlan.setCreateId(userId);
			activityPlan.setUpdateId(userId);
			activityPlan.setIsDelete(0);
			
			// 查询已有排期机器
			Map<String, Object> planingsParam = new HashMap<String, Object>();
			String startTimeStr = activityPlan.getStartTimeStr()+":00";
			String endTimeStr = activityPlan.getEndTimeStr()+":59";
			planingsParam.put("startTime", startTimeStr);
			planingsParam.put("endTime", endTimeStr);
			activityPlan.setStartTime(DateUtil.toDateTime(startTimeStr, DateUtil.DF_FULL_S1));
			activityPlan.setEndTime(DateUtil.toDateTime(endTimeStr, DateUtil.DF_FULL_S1));
			
			List<String> planings =inno72ActivityPlanMapper.selectPlanedMachine(planingsParam);
			
			// 组合计划机器关系
			List<Inno72MachineVo> machines=activityPlan.getMachines();
			if (null ==machines || machines.size()==0) {
				return Results.failure("未选择机器");
			}
			List<Inno72ActivityPlanMachine> insertPlanMachineList= new ArrayList<>();
			for (Inno72MachineVo inno72MachineVo : machines) {
				String machineId = inno72MachineVo.getMachineId();
				//查询机器在该计划时间内是否有排期（计划时间段交集）
				if ( planings.contains(machineId)) {
					logger.info("机器中有包含已排期机器");
					return Results.failure("机器中有包含已排期机器");
				}
				
				Inno72ActivityPlanMachine planMachine = new Inno72ActivityPlanMachine();
				String planMachineId = StringUtil.getUUID();
				planMachine.setId(planMachineId);
				planMachine.setActivityPlanId(activityPlanId);
				planMachine.setMachineId(machineId);
				
				insertPlanMachineList.add(planMachine);
			}
			//活动游戏结果 集合
			List<Inno72ActivityPlanGameResult> insertPlanGameResultList= new ArrayList<>();
			
			//组合 保存计划游戏结果
			List<Inno72ActivityPlanGameResult> goods=activityPlan.getGoods();
			List<Inno72CouponVo> coupons=activityPlan.getCoupons();
			if ((null == goods||goods.size()==0)&& (null == coupons||coupons.size()==0)) {
				logger.info("商品优惠券不能同时不填");
				return Results.failure("商品优惠券不能同时不填");
			}
			
			//排期计划商品管理数据
			List<Inno72ActivityPlanGoods> insertPlanGoodList= new ArrayList<>();
			if (null != goods) {
				for (Inno72ActivityPlanGameResult inno72ActivityPlanGameResult : goods) {
					//活动计划商品关联数据
					Inno72ActivityPlanGoods planGood = new Inno72ActivityPlanGoods();
					String planGoodId = StringUtil.getUUID();
					planGood.setId(planGoodId);
					planGood.setActivityPlanId(activityPlanId);
					planGood.setGoodsId(inno72ActivityPlanGameResult.getPrizeId());
					
					//活动游戏结果数据
					Inno72ActivityPlanGameResult  planGameResult = new Inno72ActivityPlanGameResult();
					String planGameResultId = StringUtil.getUUID();
					planGameResult.setId(planGameResultId);
					planGameResult.setActivityPlanId(activityPlanId);
					planGameResult.setPrizeId(planGoodId);
					planGameResult.setPrizeType("1");
					planGameResult.setResultCode(inno72ActivityPlanGameResult.getResultCode());
					planGameResult.setResultRemark(inno72ActivityPlanGameResult.getResultRemark());
					
					insertPlanGoodList.add(planGood);
					if (insertPlanGameResultList.contains(planGameResult)) {
						logger.info("添加规则有重复");
						return Results.failure("添加规则有重复");
					}
					insertPlanGameResultList.add(planGameResult);
				}
			}
			
			//保存优惠券
			List<Inno72Coupon> insertCouponList= new ArrayList<>();
			if(null != coupons){
				for (Inno72CouponVo inno72CouponVo : coupons) {
					//优惠券数据
					Inno72Coupon coupon = new Inno72Coupon();
					String couponId = StringUtil.getUUID();
					coupon.setId(couponId);
					coupon.setCode(inno72CouponVo.getCode());
					coupon.setName(inno72CouponVo.getName());
					coupon.setActivityPlanId(activityPlanId);
					coupon.setCreateId(userId);
					coupon.setUpdateId(userId);
					//活动游戏结果数据
					Inno72ActivityPlanGameResult  planGameResult = new Inno72ActivityPlanGameResult();
					String planGameResultId = StringUtil.getUUID();
					planGameResult.setId(planGameResultId);
					planGameResult.setActivityPlanId(activityPlanId);
					planGameResult.setPrizeId(couponId);
					planGameResult.setPrizeType("2");
					planGameResult.setResultCode(inno72CouponVo.getResultCode());
					planGameResult.setResultRemark(inno72CouponVo.getResultRemark());
					
					if (!insertCouponList.contains(coupon)) {
						insertCouponList.add(coupon);
						if (insertPlanGameResultList.contains(planGameResult)) {
							logger.info("添加规则有重复");
							return Results.failure("添加规则有重复");
						}
						insertPlanGameResultList.add(planGameResult);
					}else {
						int num = insertCouponList.indexOf(coupon);
						Inno72Coupon old = insertCouponList.get(num);
						planGameResult.setPrizeId(old.getId());
						if (insertPlanGameResultList.contains(planGameResult)) {
							logger.info("添加规则有重复");
							return Results.failure("添加规则有重复");
						}
						insertPlanGameResultList.add(planGameResult);
					}
				}
			}
			
			if ((null ==coupons || coupons.size()==0)&& goods.size()>0) {
				activityPlan.setPrizeType("100100");
			}
			if ((null ==goods || goods.size()==0) && coupons.size()>0) {
				activityPlan.setPrizeType("100200");
			}
			if (null !=goods && null !=coupons && goods.size()>0 && coupons.size()>0) {
				activityPlan.setPrizeType("100300");
			}
			//保存计划
			logger.info("计划添加数据开始——————————————————————————————");
			
			inno72ActivityPlanMapper.insert(activityPlan);
			logger.info("计划添加完成");
			//批量保存计划机器
			int m =inno72ActivityPlanMachineMapper.insertActivityPlanMachineList(insertPlanMachineList);
			if (m >0) {
				logger.info("计划机器关联完成");
			}
			//批量保存优惠券信息
			if (insertCouponList.size()>0) {
				inno72CouponMapper.insertCouponList(insertCouponList);
				logger.info("优惠券完成");
			}
			//批量保存计划商品信息
			if (insertPlanGoodList.size()>0) {
				inno72ActivityPlanGoodsMapper.insertActivityPlanGoodsList(insertPlanGoodList);
				logger.info("计划商品关联完成");
			}
			//批量保存计划游戏结果
			int q =inno72ActivityPlanGameResultMapper.insertActivityPlanGameResultList(insertPlanGameResultList);
			if (q >0) {
				logger.info("游戏结果规则处理完成");
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败！");		
		}
		return Results.success();
	}

	@Override
	public Inno72ActivityPlanVo findById(String id) {
		return inno72ActivityPlanMapper.selectPlanDetail(id);
	}
	@Override
	public List<Map<String, Object>> selectPlanMachinDetailList(String planId) {
		
		return inno72ActivityPlanMachineMapper.selectPlanMachinDetailList(planId);
	}
	

	@Override
	public Result<String> updateModel(Inno72ActivityPlanVo activityPlan) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		try {
			activityPlan.setUpdateId(userId);
			activityPlan.setUpdateTime(LocalDateTime.now());
			//是否开始
			int n =inno72ActivityPlanMapper.selectPlanIsState(activityPlan.getId());
			if (StringUtil.isNotBlank(activityPlan.getStartTimeStr())) {
				String startTimeStr = activityPlan.getStartTimeStr()+":00";
				//未开始 更新开始时间
				if (n==0) {
					activityPlan.setStartTime(DateUtil.toDateTime(startTimeStr, DateUtil.DF_FULL_S1));
				}
			}
			if (StringUtil.isNotBlank(activityPlan.getEndTimeStr())) {
				LocalDateTime oldEndTime =inno72ActivityPlanMapper.selectByPrimaryKey(activityPlan).getEndTime();
				String endTimeStr = activityPlan.getEndTimeStr()+":59";
				LocalDateTime newEndTime = DateUtil.toDateTime(endTimeStr, DateUtil.DF_FULL_S1);
				if (n==0) {
					newEndTime.isBefore(oldEndTime);
					if (!newEndTime.isBefore(oldEndTime)) {
						return Results.failure("活动已开始结束时间只能向前修改");
					}
				}
				activityPlan.setEndTime(newEndTime);
			}
			
			//活动游戏结果 集合
			List<Inno72ActivityPlanGameResult> insertPlanGameResultList= new ArrayList<>();
			
			//组合 保存计划游戏结果
			List<Inno72ActivityPlanGameResult> goods=activityPlan.getGoods();
			List<Inno72CouponVo> coupons=activityPlan.getCoupons();
			if ((null == goods||goods.size()==0)&& (null == coupons||coupons.size()==0)) {
				return Results.failure("商品优惠券不能同时不填");
			}
			//排期计划商品管理数据
			List<Inno72ActivityPlanGoods> insertPlanGoodList= new ArrayList<>();
			if (null != goods) {
				for (Inno72ActivityPlanGameResult inno72ActivityPlanGameResult : goods) {
					//活动计划商品关联数据
					Inno72ActivityPlanGoods planGood = new Inno72ActivityPlanGoods();
					String planGoodId = StringUtil.getUUID();
					planGood.setId(planGoodId);
					planGood.setActivityPlanId(activityPlan.getId());
					planGood.setGoodsId(inno72ActivityPlanGameResult.getPrizeId());
					
					//活动游戏结果数据
					Inno72ActivityPlanGameResult  planGameResult = new Inno72ActivityPlanGameResult();
					String planGameResultId = StringUtil.getUUID();
					planGameResult.setId(planGameResultId);
					planGameResult.setActivityPlanId(activityPlan.getId());
					planGameResult.setPrizeId(planGoodId);
					planGameResult.setPrizeType("1");
					planGameResult.setResultCode(inno72ActivityPlanGameResult.getResultCode());
					planGameResult.setResultRemark(inno72ActivityPlanGameResult.getResultRemark());
					
					insertPlanGoodList.add(planGood);
					
					if (insertPlanGameResultList.contains(planGameResult)) {
						return Results.failure("添加规则有重复");
					}
					insertPlanGameResultList.add(planGameResult);
				}
			}
			
			//保存优惠券
			List<Inno72Coupon> insertCouponList= new ArrayList<>();
			if (null != coupons) {
				for (Inno72CouponVo inno72CouponVo : coupons) {
					//优惠券数据
					Inno72Coupon coupon = new Inno72Coupon();
					String couponId = StringUtil.getUUID();
					coupon.setId(couponId);
					coupon.setCode(inno72CouponVo.getCode());
					coupon.setName(inno72CouponVo.getName());
					coupon.setActivityPlanId(activityPlan.getId());
					coupon.setCreateId(userId);
					coupon.setUpdateId(userId);
					//活动游戏结果数据
					Inno72ActivityPlanGameResult  planGameResult = new Inno72ActivityPlanGameResult();
					String planGameResultId = StringUtil.getUUID();
					planGameResult.setId(planGameResultId);
					planGameResult.setActivityPlanId(activityPlan.getId());
					planGameResult.setPrizeId(couponId);
					planGameResult.setPrizeType("2");
					planGameResult.setResultCode(inno72CouponVo.getResultCode());
					planGameResult.setResultRemark(inno72CouponVo.getResultRemark());
					
					if (!insertCouponList.contains(coupon)) {
						insertCouponList.add(coupon);
						if (insertPlanGameResultList.contains(planGameResult)) {
							logger.info("添加规则有重复");
							return Results.failure("添加规则有重复");
						}
						insertPlanGameResultList.add(planGameResult);
					}else {
						int num = insertCouponList.indexOf(coupon);
						Inno72Coupon old = insertCouponList.get(num);
						planGameResult.setPrizeId(old.getId());
						if (insertPlanGameResultList.contains(planGameResult)) {
							logger.info("添加规则有重复");
							return Results.failure("添加规则有重复");
						}
						insertPlanGameResultList.add(planGameResult);
					}
				}
			}
			
			if ((null ==coupons || coupons.size()==0)&& goods.size()>0) {
				activityPlan.setPrizeType("100100");
			}
			if ((null ==goods || goods.size()==0) && coupons.size()>0) {
				activityPlan.setPrizeType("100200");
			}
			if (null !=goods && null !=coupons && goods.size()>0 && coupons.size()>0) {
				activityPlan.setPrizeType("100300");
			}
			
			//删除原有添加优惠券
			inno72CouponMapper.deleteByPlanId(activityPlan.getId());
			
			//删除原有商品关联结果
			inno72ActivityPlanGoodsMapper.deleteByPlanId(activityPlan.getId());
			
			//删除原有活动游戏结果
			inno72ActivityPlanGameResultMapper.deleteByPlanId(activityPlan.getId());
			//更新计划
			logger.info("计划更新数据开始——————————————————————————————");
			
			inno72ActivityPlanMapper.updateByPrimaryKeySelective(activityPlan);
			logger.info("计划更新完成");
			//批量保存优惠券信息
			if (insertCouponList.size()>0) {
				inno72CouponMapper.insertCouponList(insertCouponList);
				logger.info("优惠券完成");
			}
			//批量保存计划商品信息
			if (insertPlanGoodList.size()>0) {
				inno72ActivityPlanGoodsMapper.insertActivityPlanGoodsList(insertPlanGoodList);
				logger.info("计划商品关联完成");
			}
			//批量保存计划游戏结果
			int q =inno72ActivityPlanGameResultMapper.insertActivityPlanGameResultList(insertPlanGameResultList);
			if (q >0) {
				logger.info("游戏结果规则处理完成");
			}
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败！");	
		}
		
		return Results.success();
	}

	@Override
	public List<Inno72NoPlanInfoVo> selectNoPlanMachineList(String taskTime) {

		List<Inno72NoPlanInfoVo> noPlanedMachineList = inno72ActivityPlanMapper.selectNoPlanedMachine(taskTime);

		return noPlanedMachineList;
	}

	@Override
	public Result<String> delById(String id) {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		int n =inno72ActivityPlanMapper.selectPlanIsState(id);
		if (n>0) {
			return Results.failure("计划进行中不能删除");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		//删除机器关联关系
		inno72ActivityPlanMachineMapper.deleteByPlanId(id);
		Inno72ActivityPlan model = inno72ActivityPlanMapper.selectByPrimaryKey(id);
		model.setUpdateId(userId);
		model.setIsDelete(1);
		model.setUpdateTime(LocalDateTime.now());
		
		super.update(model);
		return Results.success("操作成功");
	}

	@Override
	public List<Inno72ActivityPlanVo> selectPlanList(String code ,String startTime,String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		if (StringUtil.isNotEmpty(code)) {
			int num =getlikeCode(code);
			if (num<4) {
				num=3;
			}
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		return inno72ActivityPlanMapper.selectPlanList(params);
	}
	
	@Override
	public List<Inno72AdminAreaVo> selectAreaMachineList(String code,String level,String startTime,String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		boolean b1 = Pattern.matches(timeRegex, startTime);
		boolean b2 = Pattern.matches(timeRegex, endTime);
		if (!b1 || !b2) {
			return null;
		}
		if (StringUtil.isNotBlank(startTime)) {
			params.put("startTime", startTime+":00");
		}
		if (StringUtil.isNotBlank(endTime)) {
			params.put("endTime", endTime+":59");
		}
		
		if (StringUtil.isEmpty(code)) {
			params.put("level", 1);
	   	}
		if (level.equals("1")) {
			params.put("num", 2);
		}else if (level.equals("2")) {
			params.put("num", 4);
		}else if (level.equals("3")) {
			params.put("num", 6);
		}else if (level.equals("4")) {
			params.put("num", 9);
		}
		List<Inno72AdminAreaVo> list = new ArrayList<>();
		if (level.equals("5")) {
			list = inno72ActivityPlanMapper.selectMachineList(params);
		}else{
			list = inno72ActivityPlanMapper.selectAreaMachineList(params);
			for (Inno72AdminAreaVo inno72AdminAreaVo : list) {
				int canUseNum = 0;
				List<Inno72MachineVo> machines=inno72AdminAreaVo.getMachines();
				List<Inno72MachineVo> temp = new ArrayList<>();
				for (Inno72MachineVo machineVo : machines) {
					if (StringUtil.isEmpty(machineVo.getState())) {
						canUseNum++;
						machineVo.setState("0");
					}else{
						temp.add(machineVo);
					}
				}
				inno72AdminAreaVo.setTotalNum(machines.size()+"");
				inno72AdminAreaVo.setCanUseNum(canUseNum+"");
				machines.removeAll(temp);
			}
		}
		
	   	return list;
	}
	
	public int getlikeCode(String s){
		for (int i = s.length()-1; i >=0; i--) {
			if (!"0".equals(String.valueOf(s.charAt(i)))) {
				return i+1;
			}
		}
		return 0;
	}
	

}
