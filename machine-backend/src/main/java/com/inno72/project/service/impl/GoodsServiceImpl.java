package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.common.UploadUtil;
import com.inno72.project.mapper.Inno72GoodsMapper;
import com.inno72.project.mapper.Inno72MerchantMapper;
import com.inno72.project.mapper.Inno72ShopsMapper;
import com.inno72.project.model.Inno72Goods;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.model.Inno72Shops;
import com.inno72.project.service.GoodsService;
import com.inno72.project.vo.Inno72GoodsVo;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@Service
@Transactional
public class GoodsServiceImpl extends AbstractService<Inno72Goods> implements GoodsService {
	private static Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

	@Resource
	private Inno72GoodsMapper inno72GoodsMapper;
	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;

	Pattern pattern = Pattern.compile("^([+]?\\d{0,6})(\\.\\d{0,2})?");
	Pattern patternNumbe = Pattern.compile("^[0-9]{8}$");

	@Override
	public Result<String> saveModel(Inno72Goods model) {
		logger.info("----------------商品添加--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			// 微信类型操作默认店铺
			Inno72Merchant m = inno72MerchantMapper.selectByPrimaryKey(model.getSellerId());
			if (m.getChannelCode().equals(CommonConstants.WECHATCODE)) {
				Result<Object> sr = this.wxChannlShops(model.getSellerId(), mUser.getId());
				if (sr.getCode() == 0) {
					model.setShopId(sr.getData().toString());
				}
			}

			if (StringUtil.isBlank(model.getShopId())) {
				logger.info("请选择店铺");
				return Results.failure("请选择店铺");
			}

			// 商品ID：如果选择的店铺渠道是天猫，不限制输入长度，但需要校验唯一性；如果选择的店铺渠道是点七二或微信，则需要输入8位正整数，且校验唯一性
			if (m.getChannelCode().equals(CommonConstants.WECHATCODE)
					|| m.getChannelCode().equals(CommonConstants.INNO72CODE)) {
				Matcher match = patternNumbe.matcher(model.getCode());
				if (!match.matches()) {
					return Results.failure("商品ID请输入8位正整数");
				}
			}
			int n = inno72GoodsMapper.getCount(model.getCode());
			if (n > 0) {
				logger.info("商品ID");
				return Results.failure("商品ID已存在");
			}
			if (StringUtil.isNotBlank(model.getName())) {
				Inno72Goods gn = new Inno72Goods();
				gn.setName(model.getName());
				gn.setIsDelete(0);
				List<Inno72Goods> gnList = inno72GoodsMapper.select(gn);
				if (null != gnList && gnList.size() > 0) {
					logger.info("商品名称已存在");
					return Results.failure("商品名称已存在");
				}
			}

			if (null != model.getPrice()) {
				Matcher match = pattern.matcher(model.getPrice().toString());
				if (!match.matches()) {
					return Results.failure("商品价格最大整数6位，小数点后两位");
				}
			}

			String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			String id = StringUtil.getUUID();
			model.setId(id);
			model.setCreateId(userId);
			model.setUpdateId(userId);

			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}

		return Results.success("操作成功");
	}

	// 微信渠道添加虚拟店铺
	public Result<Object> wxChannlShops(String merchantId, String mUserId) {
		try {
			// 已添加 已添加返回ID
			Inno72Shops params = new Inno72Shops();
			params.setSellerId(merchantId);
			params.setIsDelete(0);
			List<Inno72Shops> shopsList = inno72ShopsMapper.select(params);
			if (shopsList.size() == 0) {
				Inno72Shops shops = new Inno72Shops();
				shops.setShopName("微店");
				shops.setShopCode("VD");
				shops.setId(merchantId);
				shops.setSellerId(merchantId);
				shops.setIsDelete(0);
				shops.setCreateId(mUserId);
				shops.setUpdateId(mUserId);
				shops.setCreateTime(LocalDateTime.now());
				shops.setUpdateTime(LocalDateTime.now());

				inno72ShopsMapper.insertSelective(shops);
			}
			return Results.warn("微店ID", 0, merchantId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

	@Override
	public Result<String> updateModel(Inno72Goods model) {
		logger.info("----------------商品修改--------------");
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			// 活动添加不可修改
			int gu1 = inno72GoodsMapper.selectIsUseing1(model.getId());
			if (gu1 > 0) {
				return Results.failure("商品活动排期中，请移除后修改！");
			}

			int gu2 = inno72GoodsMapper.selectIsUseing2(model.getId());
			if (gu2 > 0) {
				return Results.failure("商品派样活动使用中，请移除后修改！");
			}

			// 微信类型操作默认店铺
			Inno72Merchant m = inno72MerchantMapper.selectByPrimaryKey(model.getSellerId());
			if (m.getChannelCode().equals(CommonConstants.WECHATCODE)) {
				Result<Object> sr = this.wxChannlShops(model.getSellerId(), mUser.getId());
				if (sr.getCode() == 0) {
					model.setShopId(sr.getData().toString());
				}
			}
			if (StringUtil.isBlank(model.getShopId())) {
				logger.info("请选择店铺");
				return Results.failure("请选择店铺");
			}
			// 商品ID：如果选择的店铺渠道是天猫，不限制输入长度，但需要校验唯一性；如果选择的店铺渠道是点七二或微信，则需要输入8位正整数，且校验唯一性
			if (m.getChannelCode().equals(CommonConstants.WECHATCODE)
					|| m.getChannelCode().equals(CommonConstants.INNO72CODE)) {
				Matcher match = patternNumbe.matcher(model.getCode());
				if (!match.matches()) {
					return Results.failure("商品ID请输入8位正整数");
				}
			}
			if (StringUtil.isNotBlank(model.getCode())) {
				int n = inno72GoodsMapper.getCount(model.getCode());
				Inno72Goods g = inno72GoodsMapper.selectByPrimaryKey(model.getId());
				if (n > 0 && !g.getCode().equals(model.getCode())) {
					logger.info("商品ID");
					return Results.failure("商品ID已存在");
				}
			}
			if (StringUtil.isNotBlank(model.getName())) {
				Inno72Goods gn = new Inno72Goods();
				gn.setName(model.getName());
				gn.setIsDelete(0);
				List<Inno72Goods> gnList = inno72GoodsMapper.select(gn);
				gnList.remove(model);
				if (null != gnList && gnList.size() > 0) {
					logger.info("商品名称已存在");
					return Results.failure("商品名称已存在");
				}
			}
			if (null != model.getPrice()) {
				Matcher match = pattern.matcher(model.getPrice().toString());
				if (!match.matches()) {
					return Results.failure("商品价格最大整数6位，小数点后两位");
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
	public Result<String> delById(String id) {
		logger.info("----------------商品删除--------------");
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		int n = inno72GoodsMapper.selectIsUseing(id);
		int m = inno72GoodsMapper.selectIsUseing1(id);
		if (n > 0 || m > 0) {
			return Results.failure("商品使用中，不能删除！");
		}
		String userId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
		Inno72Goods model = inno72GoodsMapper.selectByPrimaryKey(id);
		model.setIsDelete(1);// 0正常，1下架
		model.setUpdateId(userId);
		model.setUpdateTime(LocalDateTime.now());
		super.update(model);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Inno72GoodsVo findId(String id) {
		Inno72GoodsVo good = inno72GoodsMapper.selectById(id);
		if (StringUtil.isNotBlank(good.getImg())) {
			good.setImg(CommonConstants.ALI_OSS + good.getImg());
		}
		if (StringUtil.isNotBlank(good.getBanner())) {
			good.setBanner(CommonConstants.ALI_OSS + good.getBanner());
		}
		return good;
	}

	@Override
	public List<Inno72Goods> findByPage(String code, String keyword) {
		// TODO 商户分页列表查询
		logger.info("---------------------商户分页列表查询-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("code", code);
		List<Inno72Goods> list = inno72GoodsMapper.selectByPage(params);
		for (Inno72Goods inno72Goods : list) {
			if (StringUtil.isNotBlank(inno72Goods.getImg())) {
				inno72Goods.setImg(CommonConstants.ALI_OSS + inno72Goods.getImg());
			}
			if (StringUtil.isNotBlank(inno72Goods.getBanner())) {
				inno72Goods.setBanner(CommonConstants.ALI_OSS + inno72Goods.getBanner());
			}
		}
		return inno72GoodsMapper.selectByPage(params);
	}

	@Override
	public List<Inno72Goods> getList(Inno72Goods model) {
		// TODO 获取商品列表
		logger.info("---------------------获取商品列表-------------------");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("shopId", model.getShopId());
		return inno72GoodsMapper.selectByShop(params);
	}

	@Override
	public Result<String> uploadImage(MultipartFile file) {
		if (file.getSize() > 0) {
			// 调用上传图片
			return UploadUtil.uploadImage(file, "goods");
		}
		logger.info("[out-uploadImg]-空");
		return Results.success("");

	}

	@Override
	public Result<String> isExist(String code, String sellerId, String Id, int type) {
		// 商品ID：如果选择的店铺渠道是天猫，不限制输入长度，但需要校验唯一性；如果选择的店铺渠道是点七二或微信，则需要输入8位正整数，且校验唯一性
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("code", code);
		params.put("id", Id);
		if (0 == type) {// 商品ID
			Inno72Merchant m = inno72MerchantMapper.selectByPrimaryKey(sellerId);
			if (m.getChannelCode().equals(CommonConstants.WECHATCODE)
					|| m.getChannelCode().equals(CommonConstants.INNO72CODE)) {
				Matcher match = patternNumbe.matcher(code);
				if (!match.matches()) {
					return Results.warn("商品ID请输入8位正整数", 0, "false");
				}
			}
		}
		int n = inno72GoodsMapper.selectIsExist(params);
		if (n > 0) {
			return Results.warn("已存在", 0, "false");
		} else {
			return Results.success();
		}

	}

}
