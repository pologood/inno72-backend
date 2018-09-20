package com.inno72.Interact.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractGoodsMapper;
import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.service.InteractGoodsService;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72CouponMapper;
import com.inno72.project.mapper.Inno72GoodsMapper;
import com.inno72.project.model.Inno72Coupon;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractGoodsServiceImpl extends AbstractService<Inno72InteractGoods> implements InteractGoodsService {
	private static Logger logger = LoggerFactory.getLogger(InteractGoodsServiceImpl.class);
	@Resource
	private Inno72InteractGoodsMapper inno72InteractGoodsMapper;
	@Resource
	private Inno72CouponMapper inno72CouponMapper;
	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;

	@Override
	public Result<String> save(InteractGoodsVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);

			// 数据校验
			Inno72InteractGoods interactGoods = new Inno72InteractGoods();
			interactGoods.setId(StringUtil.getUUID());
			interactGoods.setInteractId(model.getInteractId());
			interactGoods.setType(model.getType());
			if (null == model.getType()) {
				logger.info("请选择商品类型");
				return Results.failure("请选择商品类型");
			}
			if (0 == model.getType()) {
				if (StringUtil.isBlank(model.getSellerId())) {
					logger.info("请选择商家");
					return Results.failure("请选择商家");
				}
				if (StringUtil.isBlank(model.getShopId())) {
					logger.info("请选择店铺");
					return Results.failure("请选择店铺");
				}
				if (StringUtil.isBlank(model.getName())) {
					logger.info("请填写商品名称");
					return Results.failure("请填写商品名称");
				}
				if (StringUtil.isBlank(model.getCode())) {
					logger.info("请填写商品编码");
					return Results.failure("请填写商品编码");
				}
				if (null == model.getImg()) {
					logger.info("请上传图片");
					return Results.failure("请上传图片");
				}
				if (null == model.getNumber()) {
					logger.info("请填写商品数量");
					return Results.failure("请填写商品数量");
				}
				if (null == model.getNumber()) {
					logger.info("请输入商品价格");
					return Results.failure("请输入商品价格");
				}
				model.setId(StringUtil.getUUID());
				model.setCreateId(mUserId);
				model.setUpdateId(mUserId);
				model.setCreateTime(LocalDateTime.now());
				model.setUpdateTime(LocalDateTime.now());

				inno72GoodsMapper.insert(model);
				interactGoods.setGoodsId(model.getId());
				interactGoods.setUserDayNumber(model.getUserDayNumber());
			} else {
				if (StringUtil.isBlank(model.getName())) {
					logger.info("请填写商品名称");
					return Results.failure("请填写商品名称");
				}
				if (StringUtil.isBlank(model.getCode())) {
					logger.info("请填写商品编码");
					return Results.failure("请填写商品编码");
				}
				if (StringUtil.isBlank(model.getShopId())) {
					logger.info("请选择店铺");
					return Results.failure("请选择店铺");
				}
				Inno72Coupon coupon = new Inno72Coupon();
				coupon.setId(StringUtil.getUUID());
				coupon.setName(model.getName());
				coupon.setCode(model.getCode());
				coupon.setActivityPlanId(model.getInteractId());
				coupon.setShopsId(model.getShopId());
				coupon.setCreateId(mUserId);
				coupon.setUpdateId(mUserId);
				inno72CouponMapper.insert(coupon);
				interactGoods.setGoodsId(coupon.getId());
			}

			inno72InteractGoodsMapper.insert(interactGoods);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(InteractGoodsVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			// 数据校验
			Inno72InteractGoods interactGoods = new Inno72InteractGoods();
			interactGoods.setGoodsId(model.getId());
			interactGoods = inno72InteractGoodsMapper.selectOne(interactGoods);

			if (0 == interactGoods.getType()) {
				if (StringUtil.isBlank(model.getSellerId())) {
					logger.info("请选择商家");
					return Results.failure("请选择商家");
				}
				if (StringUtil.isBlank(model.getShopId())) {
					logger.info("请选择店铺");
					return Results.failure("请选择店铺");
				}
				if (StringUtil.isBlank(model.getName())) {
					logger.info("请填写商品名称");
					return Results.failure("请填写商品名称");
				}
				if (StringUtil.isBlank(model.getCode())) {
					logger.info("请填写商品编码");
					return Results.failure("请填写商品编码");
				}
				if (null == model.getImg()) {
					logger.info("请上传图片");
					return Results.failure("请上传图片");
				}
				if (null == model.getNumber()) {
					logger.info("请填写商品数量");
					return Results.failure("请填写商品数量");
				}
				if (null == model.getNumber()) {
					logger.info("请输入商品价格");
					return Results.failure("请输入商品价格");
				}
				model.setUpdateId(mUserId);
				model.setUpdateTime(LocalDateTime.now());

				inno72GoodsMapper.updateByPrimaryKey(model);
				interactGoods.setUserDayNumber(model.getUserDayNumber());
			} else {
				if (StringUtil.isBlank(model.getName())) {
					logger.info("请填写商品名称");
					return Results.failure("请填写商品名称");
				}
				if (StringUtil.isBlank(model.getCode())) {
					logger.info("请填写商品编码");
					return Results.failure("请填写商品编码");
				}
				if (StringUtil.isBlank(model.getShopId())) {
					logger.info("请选择店铺");
					return Results.failure("请选择店铺");
				}
				Inno72Coupon coupon = new Inno72Coupon();
				coupon.setId(model.getId());
				coupon.setName(model.getName());
				coupon.setCode(model.getCode());
				coupon.setShopsId(model.getShopId());
				coupon.setUpdateId(mUserId);
				inno72CouponMapper.updateByPrimaryKey(coupon);
			}

			inno72InteractGoodsMapper.updateByPrimaryKey(interactGoods);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public InteractGoodsVo findGoodsById(String id) {
		return inno72InteractGoodsMapper.selectInteractGoodsById(id);
	}

}
