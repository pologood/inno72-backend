package com.inno72.project.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
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
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.project.vo.Inno72CouponVo;
import com.inno72.project.vo.Inno72MachineVo;
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
    

	@Override
	public Result<String> saveActPlan(Inno72ActivityPlanVo activityPlan) {
		
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
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
			String startTimeStr = activityPlan.getStartTimeStr();
			String endTimeStr = activityPlan.getEndTimeStr();
			
			
			planingsParam.put("startTime", activityPlan.getStartTime());
			planingsParam.put("endTime", activityPlan.getEndTime());
			List<String> planings =inno72ActivityPlanMapper.selectPlanedMachine(planingsParam);
			
			// 组合计划机器关系
			List<Inno72MachineVo> machines=activityPlan.getMachines();
			List<Inno72ActivityPlanMachine> insertPlanMachineList= new ArrayList<>();
			for (Inno72MachineVo inno72MachineVo : machines) {
				String machineId = inno72MachineVo.getMachineId();
				//查询机器在该计划时间内是否有排期（计划时间段交集）
				if ( planings.contains(machineId)) {
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
			//排期计划商品管理数据
			List<Inno72ActivityPlanGoods> insertPlanGoodList= new ArrayList<>();
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
					return Results.failure("添加规则有重复");
				}
				insertPlanGameResultList.add(planGameResult);
			}
			
			//保存优惠券
			List<Inno72CouponVo> coupons=activityPlan.getCoupons();
			List<Inno72Coupon> insertCouponList= new ArrayList<>();
			
			for (Inno72CouponVo inno72CouponVo : coupons) {
				//去重 同一 inno72CouponVo.getCode() 存一个
				
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
				
				insertCouponList.add(coupon);
				if (insertPlanGameResultList.contains(planGameResult)) {
					return Results.failure("添加规则有重复");
				}
				insertPlanGameResultList.add(planGameResult);
				
			}
			
			if ((null ==coupons || coupons.size()==0)&& goods.size()>0) {
				activityPlan.setPrizeType("100100");
			}
			if ((null ==goods || goods.size()==0) && coupons.size()>0) {
				activityPlan.setPrizeType("100200");
			}
			if (null ==goods && null ==coupons && goods.size()>0 && coupons.size()>0) {
				activityPlan.setPrizeType("100300");
			}
			//保存计划
			int n = inno72ActivityPlanMapper.insert(activityPlan);
			if (n <1) {
				return Results.failure("计划添加异常");
			}
			//批量保存计划机器
			int m =inno72ActivityPlanMachineMapper.insertActivityPlanMachineList(insertPlanMachineList);
			if (m <1) {
				return Results.failure("计划机器关联处理异常");
			}
			//批量保存优惠券信息
			int l =inno72CouponMapper.insertCouponList(insertCouponList);
			if (l <1) {
				return Results.failure("优惠券处理异常");
			}
			//批量保存计划商品信息
			int p =inno72ActivityPlanGoodsMapper.insertActivityPlanGoodsList(insertPlanGoodList);
			if (p <1) {
				return Results.failure("计划商品关联处理异常");
			}
			//批量保存计划游戏结果
			int q =inno72ActivityPlanGameResultMapper.insertActivityPlanGameResultList(insertPlanGameResultList);
			if (q <1) {
				return Results.failure("游戏结果规则处理异常");
			}
			
		} catch (Exception e) {
			return Results.failure("操作失败！");		
		}
		return Results.success();
	}

	@Override
	public Inno72ActivityPlanVo findById(String id) {
		return inno72ActivityPlanMapper.selectPlanDetail(id);
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
			
			//活动游戏结果 集合
			List<Inno72ActivityPlanGameResult> insertPlanGameResultList= new ArrayList<>();
			
			//组合 保存计划游戏结果
			List<Inno72ActivityPlanGameResult> goods=activityPlan.getGoods();
			//排期计划商品管理数据
			List<Inno72ActivityPlanGoods> insertPlanGoodList= new ArrayList<>();
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
			
			//保存优惠券
			List<Inno72CouponVo> coupons=activityPlan.getCoupons();
			List<Inno72Coupon> insertCouponList= new ArrayList<>();
			
			for (Inno72CouponVo inno72CouponVo : coupons) {
				//去重 同一 inno72CouponVo.getCode() 存一个
				
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
				
				insertCouponList.add(coupon);
				if (insertPlanGameResultList.contains(planGameResult)) {
					return Results.failure("更新规则有重复");
				}
				insertPlanGameResultList.add(planGameResult);
				
			}
			
			if ((null ==coupons || coupons.size()==0)&& goods.size()>0) {
				activityPlan.setPrizeType("100100");
			}
			if ((null ==goods || goods.size()==0) && coupons.size()>0) {
				activityPlan.setPrizeType("100200");
			}
			if (null ==goods && null ==coupons && goods.size()>0 && coupons.size()>0) {
				activityPlan.setPrizeType("100300");
			}
			
			//删除原有添加优惠券
			inno72CouponMapper.deleteByPlanId(activityPlan.getId());
			
			//删除原有商品关联结果
			inno72ActivityPlanGoodsMapper.deleteByPlanId(activityPlan.getId());
			
			//删除原有活动游戏结果
			inno72ActivityPlanGameResultMapper.deleteByPlanId(activityPlan.getId());
			
			
			//更新计划
			int n = inno72ActivityPlanMapper.updateByPrimaryKeySelective(activityPlan);
			if (n <1) {
				return Results.failure("计划更新异常");
			}
			
			//批量保存优惠券信息
			int l =inno72CouponMapper.insertCouponList(insertCouponList);
 
			if (l <1) {
				return Results.failure("优惠券处理异常");
			}
			//批量保存计划商品信息
			int p= inno72ActivityPlanGoodsMapper.insertActivityPlanGoodsList(insertPlanGoodList);
			if (p <1) {
				return Results.failure("计划商品关联处理异常");
			}
			//批量保存计划游戏结果
			int q =inno72ActivityPlanGameResultMapper.insertActivityPlanGameResultList(insertPlanGameResultList);
			if (q <1) {
				return Results.failure("游戏结果规则处理异常");
			}
			
		} catch (Exception e) {
			return Results.failure("操作失败！");	
		}
		
		return Results.success();
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
		Inno72ActivityPlan model = inno72ActivityPlanMapper.selectByPrimaryKey(id);
		model.setUpdateId(userId);
		model.setIsDelete(1);
		model.setUpdateTime(LocalDateTime.now());
		
		return Results.success("操作成功");
	}

	@Override
	public List<Inno72ActivityPlanVo> selectPlanList(String code ,String startTime,String endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(code)) {
			int num =getlikeCode(code);
			if (num<4) {
				num=3;
			}
			String likeCode = code.substring(0, num);
			params.put("code", likeCode);
			params.put("num", num);
		}
		return inno72ActivityPlanMapper.selectByPage(params);
	}
	
	@Override
	public List<Inno72AdminAreaVo> selectAreaMachineList(String code,String level) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		
		if (StringUtil.isEmpty(code)) {
			params.put("level", 1);
	   	}
		if (level.equals("1")) {
			params.put("num", 2);
		}else if (level.equals("2")) {
			params.put("num", 6);
		}else if (level.equals("3")) {
			params.put("num", 9);
		}
	   	return inno72ActivityPlanMapper.selectAreaMachineList(params);
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
