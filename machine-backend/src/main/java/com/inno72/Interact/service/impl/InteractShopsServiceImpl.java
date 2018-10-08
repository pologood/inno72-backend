package com.inno72.Interact.service.impl;

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

import com.inno72.Interact.mapper.Inno72InteractGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractShopsMapper;
import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.service.InteractShopsService;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.Interact.vo.InteractShopsVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ShopsMapper;
import com.inno72.project.model.Inno72Shops;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractShopsServiceImpl extends AbstractService<Inno72InteractShops> implements InteractShopsService {
	private static Logger logger = LoggerFactory.getLogger(InteractShopsServiceImpl.class);
	@Resource
	private Inno72InteractShopsMapper inno72InteractShopsMapper;
	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;
	@Resource
	private Inno72InteractGoodsMapper inno72InteractGoodsMapper;

	@Override
	public Result<String> save(InteractShopsVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setId(StringUtil.getUUID());
			model.setIsDelete(0);
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			// 数据校验
			if (StringUtil.isBlank(model.getSellerId())) {
				logger.info("请选择商家");
				return Results.failure("请选择商家");
			}
			if (StringUtil.isBlank(model.getShopCode())) {
				logger.info("请填写店铺编号");
				return Results.failure("请填写店铺编号");
			}
			if (StringUtil.isBlank(model.getShopName())) {
				logger.info("请填写店铺名称");
				return Results.failure("请填写店铺名称");
			}
			if (null == model.getIsVip()) {
				logger.info("选择是否入会");
				return Results.failure("选择是否入会");
			}

			Inno72InteractShops interactShops = new Inno72InteractShops();
			interactShops.setId(StringUtil.getUUID());
			interactShops.setInteractId(model.getInteractId());
			interactShops.setShopsId(model.getId());
			interactShops.setIsVip(model.getIsVip());

			inno72InteractShopsMapper.insert(interactShops);
			inno72ShopsMapper.insert(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(InteractShopsVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			// 数据校验
			if (StringUtil.isBlank(model.getSellerId())) {
				logger.info("请选择商家");
				return Results.failure("请选择商家");
			}
			if (StringUtil.isBlank(model.getShopCode())) {
				logger.info("请填写店铺编号");
				return Results.failure("请填写店铺编号");
			}
			if (StringUtil.isBlank(model.getShopName())) {
				logger.info("请填写店铺名称");
				return Results.failure("请填写店铺名称");
			}
			if (null == model.getIsVip()) {
				logger.info("选择是否入会");
				return Results.failure("选择是否入会");
			}

			inno72ShopsMapper.updateByPrimaryKeySelective(model);
			// 中间表 关联关系
			Inno72InteractShops interactShops = new Inno72InteractShops();
			interactShops.setShopsId(model.getId());
			Inno72InteractShops oldInteractShops = inno72InteractShopsMapper.selectOne(interactShops);

			oldInteractShops.setIsVip(model.getIsVip());
			inno72InteractShopsMapper.updateByPrimaryKeySelective(oldInteractShops);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<InteractShopsVo> getList(String merchantId) {
		logger.info("---------------------获取商户下店铺列表-------------------");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sellerId", merchantId);

		List<InteractShopsVo> inno72ShopsList = inno72InteractShopsMapper.selectMerchantShops(params);

		return inno72ShopsList;

	}

	@Override
	public InteractShopsVo findShopsById(String id) {
		return inno72InteractShopsMapper.selectInteractShopsById(id);
	}

	@Override
	public Result<String> deleteById(String interactId, String shopsId) {
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			// 查询店铺下是否存在商品
			Map<String, Object> pm = new HashMap<>();
			pm.put("interactId", interactId);
			pm.put("shopsId", shopsId);
			List<InteractGoodsVo> goods = inno72InteractGoodsMapper.selectGoods(pm);
			if (goods != null && goods.size() > 0) {
				logger.info("店铺下存在商品，不能删除");
				return Results.failure("店铺下存在商品，不能删除");
			}
			Inno72Shops shops = new Inno72Shops();
			shops.setId(shopsId);
			shops.setIsDelete(1);
			shops.setUpdateId(mUserId);
			shops.setUpdateTime(LocalDateTime.now());
			inno72ShopsMapper.updateByPrimaryKeySelective(shops);
			// 中间表 关联关系
			Inno72InteractShops interactShops = new Inno72InteractShops();
			interactShops.setShopsId(shopsId);
			interactShops.setInteractId(interactId);
			inno72InteractShopsMapper.delete(interactShops);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

}
