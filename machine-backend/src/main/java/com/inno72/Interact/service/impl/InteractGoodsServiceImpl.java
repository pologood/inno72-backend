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
import com.inno72.Interact.mapper.Inno72InteractMachineGoodsMapper;
import com.inno72.Interact.model.Inno72InteractGoods;
import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.service.InteractGoodsService;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72CouponMapper;
import com.inno72.project.mapper.Inno72GoodsMapper;
import com.inno72.project.model.Inno72Coupon;
import com.inno72.project.model.Inno72Goods;
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
	@Resource
	private Inno72InteractMachineGoodsMapper inno72InteractMachineGoodsMapper;

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

				model.setId(StringUtil.getUUID());
				model.setUseType(1);
				model.setIsDelete(0);
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
				if (null == model.getIsAlone()) {
					logger.info("请选择是否关联商品");
					return Results.failure("请选择是否关联商品");
				}
				List<Map<String, String>> goodsList = model.getGoodsList();
				if (model.getIsAlone() == 0) {
					if (null == model.getImg()) {
						logger.info("请上传图片");
						return Results.failure("请上传图片");
					}
					if (null == model.getBanner()) {
						logger.info("请上传宣传图片");
						return Results.failure("请上传宣传图片");
					}
				} else {

					if (model.getIsAlone() == 1) {
						if (null == goodsList || goodsList.size() < 1) {
							logger.info("选择要关联的商品");
							return Results.failure("选择要关联的商品");
						}
					}
				}

				Inno72Coupon coupon = new Inno72Coupon();
				coupon.setId(StringUtil.getUUID());
				coupon.setIsDelete(0);
				coupon.setName(model.getName());
				coupon.setCode(model.getCode());
				coupon.setImg(model.getImg());
				coupon.setBanner(model.getBanner());
				coupon.setActivityPlanId(model.getInteractId());
				coupon.setShopsId(model.getShopId());
				coupon.setCreateId(mUserId);
				coupon.setUpdateId(mUserId);
				inno72CouponMapper.insert(coupon);

				if (null != goodsList && goodsList.size() > 0) {
					for (Map<String, String> map : goodsList) {
						Inno72InteractGoods gg = new Inno72InteractGoods();
						gg.setGoodsId(map.get("goodsId"));
						gg = inno72InteractGoodsMapper.selectOne(gg);
						gg.setCoupon(coupon.getId());
						inno72InteractGoodsMapper.updateByPrimaryKeySelective(gg);
					}
				}

				interactGoods.setGoodsId(coupon.getId());
				interactGoods.setIsAlone(model.getIsAlone());
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

				model.setUpdateId(mUserId);
				model.setUpdateTime(LocalDateTime.now());

				inno72GoodsMapper.updateByPrimaryKeySelective(model);
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
				if (null == model.getIsAlone()) {
					logger.info("请选择是否关联商品");
					return Results.failure("请选择是否关联商品");
				}
				List<Map<String, String>> goodsList = model.getGoodsList();

				if (model.getIsAlone() == 1) {
					if (null == goodsList || goodsList.size() < 1) {
						logger.info("选择要关联的商品");
						return Results.failure("选择要关联的商品");
					}
				}
				Inno72Coupon coupon = new Inno72Coupon();
				coupon.setId(model.getId());
				coupon.setName(model.getName());
				coupon.setCode(model.getCode());
				coupon.setImg(model.getImg());
				coupon.setBanner(model.getBanner());
				coupon.setShopsId(model.getShopId());
				coupon.setUpdateId(mUserId);
				// 修改自身是否关联商品发放
				Inno72InteractGoods baseg = new Inno72InteractGoods();
				baseg.setGoodsId(model.getId());
				baseg = inno72InteractGoodsMapper.selectOne(baseg);
				baseg.setIsAlone(model.getIsAlone());
				inno72InteractGoodsMapper.updateByPrimaryKeySelective(baseg);

				// 清空优惠券原来关联的商品
				Inno72InteractGoods old = new Inno72InteractGoods();
				old.setCoupon(coupon.getId());
				List<Inno72InteractGoods> oldList = inno72InteractGoodsMapper.select(old);
				for (Inno72InteractGoods oldGoods : oldList) {
					oldGoods.setCoupon("");
					inno72InteractGoodsMapper.updateByPrimaryKeySelective(oldGoods);
				}
				// 更新优惠券新关联的商品
				if (null != goodsList && goodsList.size() > 0) {
					for (Map<String, String> map : goodsList) {
						Inno72InteractGoods gg = new Inno72InteractGoods();
						gg.setGoodsId(map.get("goodsId"));
						gg = inno72InteractGoodsMapper.selectOne(gg);
						gg.setCoupon(coupon.getId());
						inno72InteractGoodsMapper.updateByPrimaryKeySelective(gg);
					}
				}

				inno72CouponMapper.updateByPrimaryKeySelective(coupon);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public InteractGoodsVo findGoodsById(String id, Integer type) {
		InteractGoodsVo goods = null;
		if (type == 0) {
			goods = inno72InteractGoodsMapper.selectInteractGoodsById(id);
		} else if (type == 1) {
			goods = inno72InteractGoodsMapper.selectInteractCouponById(id);
		}
		if (StringUtil.isNotBlank(goods.getImg())) {
			goods.setImg(CommonConstants.ALI_OSS + goods.getImg());
		}
		if (StringUtil.isNotBlank(goods.getBanner())) {
			goods.setBanner(CommonConstants.ALI_OSS + goods.getBanner());
		}
		return goods;
	}

	@Override
	public List<InteractGoodsVo> getList(String interactId, String shopsId, Integer isAlone) {
		logger.info("---------------------获取活动下商品列表-------------------");

		Map<String, Object> pm = new HashMap<>();
		pm.put("interactId", interactId);
		pm.put("shopsId", shopsId);
		pm.put("isAlone", isAlone);
		return inno72InteractGoodsMapper.selectGoods(pm);

	}

	@Override
	public List<Map<String, Object>> couponGetList(String interactId, String shopsId) {
		logger.info("---------------------获取优惠券关联商品列表-------------------");

		Map<String, Object> pm = new HashMap<>();
		pm.put("interactId", interactId);
		pm.put("shopsId", shopsId);
		return inno72InteractGoodsMapper.selectCouponGetList(pm);

	}

	@Override
	public Result<String> deleteById(String interactId, String goodsId) {
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			// 查看商品是否被关联
			Inno72InteractMachineGoods machineGoods = new Inno72InteractMachineGoods();
			machineGoods.setGoodsId(goodsId);
			int n = inno72InteractMachineGoodsMapper.selectCount(machineGoods);

			if (n > 0) {
				logger.info("请商品已关联机器不能删除");
				return Results.failure("请商品已关联机器不能删除");
			}

			Inno72InteractGoods interactGoods = new Inno72InteractGoods();
			interactGoods.setInteractId(interactId);
			interactGoods.setGoodsId(goodsId);
			interactGoods = inno72InteractGoodsMapper.selectOne(interactGoods);
			if (0 == interactGoods.getType()) {
				Inno72Goods goods = new Inno72Goods();
				goods.setId(goodsId);
				goods.setIsDelete(1);
				goods.setUpdateId(mUserId);
				goods.setUpdateTime(LocalDateTime.now());
				inno72GoodsMapper.updateByPrimaryKeySelective(goods);
			} else {
				Inno72Coupon coupon = new Inno72Coupon();
				coupon.setId(goodsId);
				coupon.setIsDelete(1);
				coupon.setUpdateId(mUserId);
				inno72CouponMapper.updateByPrimaryKeySelective(coupon);
			}

			inno72InteractGoodsMapper.delete(interactGoods);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}
}
