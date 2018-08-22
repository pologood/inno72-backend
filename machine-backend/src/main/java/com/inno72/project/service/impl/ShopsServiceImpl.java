package com.inno72.project.service.impl;

import java.time.LocalDateTime;
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
import com.inno72.project.mapper.Inno72ShopsMapper;
import com.inno72.project.model.Inno72Shops;
import com.inno72.project.service.ShopsService;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
@Service
@Transactional
public class ShopsServiceImpl extends AbstractService<Inno72Shops> implements ShopsService {
	private static Logger logger = LoggerFactory.getLogger(ShopsServiceImpl.class);

	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;

	@Override
	public Result<String> saveModel(Inno72Shops model) {
		logger.info("--------------------活动新增-------------------");
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			int n = inno72ShopsMapper.getCount(model.getShopCode());
			if (n > 0) {
				return Results.failure("店铺编码已存在，请确认！");
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setId(StringUtil.getUUID());
			model.setCreateId(userId);
			model.setUpdateId(userId);
			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

	@Override
	public Result<String> delById(String id) {
		logger.info("--------------------店铺删除-------------------");
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

		int n = inno72ShopsMapper.selectIsUseing(id);
		if (n > 0) {
			return Results.failure("店铺使用中，不能删除！");
		}
		Inno72Shops model = inno72ShopsMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);// 0正常,1结束
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());

		super.update(model);
		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModel(Inno72Shops model) {
		logger.info("--------------------店铺更新-------------------");
		try {
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isNotBlank(model.getShopCode())) {
				Inno72Shops m = inno72ShopsMapper.selectByPrimaryKey(model.getId());
				int n = inno72ShopsMapper.getCount(model.getShopCode());
				if (n > 0 && !m.getShopCode().equals(model.getShopCode())) {
					return Results.failure("店铺编码已存在，请确认！");
				}
			}
			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setUpdateId(userId);
			model.setUpdateTime(LocalDateTime.now());
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

	@Override
	public List<Inno72Shops> findByPage(String code, String keyword) {
		logger.info("---------------------店铺分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("code", code);
		params.put("keyword", keyword);
		return inno72ShopsMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Shops> getList(Inno72Shops model) {
		logger.info("---------------------获取店铺列表-------------------");
		model.setIsDelete(0);
		Condition condition = new Condition(Inno72Shops.class);
		condition.createCriteria().andEqualTo(model);
		return super.findByCondition(condition);
	}

	@Override
	public List<Inno72Shops> selectActivityShops(String activityId, String keyword) {
		logger.info("---------------------获取活动下店铺列表-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("activityId", activityId);
		params.put("keyword", keyword);
		return inno72ShopsMapper.selectActivityShops(params);
	}

	@Override
	public List<Inno72Shops> selectMerchantShops(String keyword) {
		logger.info("---------------------获取商户店铺列表-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		return inno72ShopsMapper.selectMerchantShops(params);
	}

}
