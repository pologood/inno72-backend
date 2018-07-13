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
			// 查询已有排期机器
			Map<String, Object> planingsParam = new HashMap<String, Object>();
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
			//保存计划
			int n = inno72ActivityPlanMapper.insert(activityPlan);
			if (n <1) {
				return Results.failure("计划添加异常");
			}
			//批量保存计划机器
			int m =inno72ActivityPlanMachineMapper.insertList(insertPlanMachineList);
			if (m <1) {
				return Results.failure("计划机器关联处理异常");
			}
			//批量保存优惠券信息
			int l =inno72CouponMapper.insertList(insertCouponList);
			if (l <1) {
				return Results.failure("优惠券处理异常");
			}
			//批量保存计划商品信息
			inno72ActivityPlanGoodsMapper.insertList(insertPlanGoodList);
			int p =inno72CouponMapper.insertList(insertCouponList);
			if (p <1) {
				return Results.failure("计划商品关联处理异常");
			}
			//批量保存计划游戏结果
			inno72ActivityPlanGameResultMapper.insertList(insertPlanGameResultList);
			int q =inno72CouponMapper.insertList(insertCouponList);
			if (q <1) {
				return Results.failure("游戏结果规则处理异常");
			}
			
		} catch (Exception e) {
			return Results.failure("操作失败！");		
		}
		return Results.success();
	}

	@Override
	public List<Inno72ActivityPlanVo> selectByPage(Object condition) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		
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
	
	
	
	
	

}
