package com.inno72.project.service.impl;

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
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityMapper;
import com.inno72.project.mapper.Inno72ActivityShopsMapper;
import com.inno72.project.model.Inno72Activity;
import com.inno72.project.model.Inno72ActivityShops;
import com.inno72.project.model.Inno72Shops;
import com.inno72.project.service.ActivityService;
import com.inno72.project.vo.Inno72ActivityVo;
import com.inno72.redis.IRedisUtil;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class ActivityServiceImpl extends AbstractService<Inno72Activity> implements ActivityService {
	private static Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);
	@Resource
	private IRedisUtil redisUtil;
	@Resource
	private Inno72ActivityMapper inno72ActivityMapper;
	@Resource
	private Inno72ActivityShopsMapper inno72ActivityShopsMapper;

	@Override
	public Result<String> saveModel(Inno72ActivityVo model) {
		logger.info("--------------------活动新增-------------------");
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			model.setId(StringUtil.getUUID());
			model.setManagerId(userId);// 负责人
			model.setCreateId(userId);
			model.setUpdateId(userId);
			if (StringUtil.isBlank(model.getName())) {
				return Results.failure("请填写活动名称");
			}
			if (StringUtil.isBlank(model.getCode())) {
				return Results.failure("请填写活动编码");
			}

			if (0 == model.getIsDefault()) {// 常规活动选择商户，店铺
				if (StringUtil.isNotBlank(model.getCode())) {
					int n = inno72ActivityMapper.getCountByCode(model.getCode());
					if (n > 0) {
						return Results.failure("活动编码已存在，请确认！");
					}
				}
				if (null == model.getType()) {
					return Results.failure("请选择活动类型");
				}

				List<Inno72Shops> shops = model.getShops();
				if (null == shops || shops.size() == 0) {
					return Results.failure("请选择活动店铺");
				}
				// 处理活动店铺关系
				List<Inno72ActivityShops> insertActivityShopsList = new ArrayList<>();
				for (Inno72Shops inno72Shops : shops) {
					Inno72ActivityShops activityShops = new Inno72ActivityShops();
					activityShops.setId(StringUtil.getUUID());
					activityShops.setActivityId(model.getId());
					activityShops.setShopsId(inno72Shops.getId());

					if (insertActivityShopsList.contains(activityShops)) {
						logger.info("选择店铺有重复");
						return Results.failure("选择店铺有重复");
					}
					insertActivityShopsList.add(activityShops);
				}

				inno72ActivityShopsMapper.insertActivityShopsList(insertActivityShopsList);

			} else {
				Inno72Activity defaultAct = inno72ActivityMapper.selectDefaultActivity();
				if (null != defaultAct) {
					// 删除原来默认活动
					inno72ActivityMapper.deleteDefaultActivity();
					redisUtil.del(CommonConstants.REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY + "*");
				}
			}
			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("--------------------活动删除-------------------");
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

		int n = inno72ActivityMapper.selectIsUseing(id);
		if (n > 0) {
			return Results.failure("活动已排期，未结束，不能删除！");
		}
		Inno72Activity model = inno72ActivityMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);// 0正常,1结束
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());
		super.update(model);
		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModel(Inno72ActivityVo model) {
		logger.info("--------------------活动更新-------------------");
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			Inno72Activity m = inno72ActivityMapper.selectByPrimaryKey(model.getId());
			int n = inno72ActivityMapper.getCountByCode(model.getCode());
			if (n > 0 && !m.getCode().equals(model.getCode())) {
				return Results.failure("活动编码已存在，请确认！");
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());

			List<Inno72Shops> shops = model.getShops();
			if (null == shops || shops.size() == 0) {
				return Results.failure("请选择活动店铺");
			}

			// 处理活动店铺关系
			List<Inno72ActivityShops> insertActivityShopsList = new ArrayList<>();
			for (Inno72Shops inno72Shops : shops) {
				Inno72ActivityShops activityShops = new Inno72ActivityShops();
				activityShops.setId(StringUtil.getUUID());
				activityShops.setActivityId(model.getId());
				activityShops.setShopsId(inno72Shops.getId());
				if (insertActivityShopsList.contains(activityShops)) {
					logger.info("选择店铺有重复");
					return Results.failure("选择店铺有重复");
				}
				insertActivityShopsList.add(activityShops);
			}

			// 删除与原有店铺关系
			inno72ActivityShopsMapper.deleteByActivityId(model.getId());

			// 新增店铺关系
			inno72ActivityShopsMapper.insertActivityShopsList(insertActivityShopsList);

			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<Inno72ActivityVo> findByPage(String type, String keyword) {
		logger.info("---------------------活动分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("type", type);

		return inno72ActivityMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Activity> getList() {
		logger.info("---------------------获取活动列表-------------------");
		return inno72ActivityMapper.selectUnPlanList();
	}

	@Override
	public Inno72ActivityVo selectById(String id) {
		Inno72ActivityVo activity = inno72ActivityMapper.selectById(id);
		List<Inno72Shops> shops = activity.getShops();
		String shopNames = "";
		if (null != shops && shops.size() > 0) {
			for (Inno72Shops inno72Shops : shops) {
				shopNames = shopNames + " " + inno72Shops.getShopName();
			}
			activity.setShopName(shopNames);
		}
		return inno72ActivityMapper.selectById(id);
	}

	@Override
	public Inno72Activity selectDefaultActivity() {

		return inno72ActivityMapper.selectDefaultActivity();
	}

}
