package com.inno72.Interact.service.impl;

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

import com.inno72.Interact.mapper.Inno72InteractGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractShopsMapper;
import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.service.InteractShopsService;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.Interact.vo.InteractShopsVo;
import com.inno72.Interact.vo.ShopsVo;
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
	public List<Map<String, Object>> checkShops(String sellerId) {
		logger.info("---------------------获取要添加店铺列表-------------------");
		return inno72InteractShopsMapper.getShopsList(sellerId);
	}

	@Override
	public Result<String> save(InteractShopsVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			List<ShopsVo> shops = model.getShops();
			if (shops.size() == 0) {
				logger.info("请添加店铺");
				return Results.failure("请添加店铺");
			}
			List<Inno72InteractShops> insertList = new ArrayList<>();
			for (ShopsVo shopsVo : shops) {
				Inno72InteractShops interactShops = new Inno72InteractShops();
				interactShops.setId(StringUtil.getUUID());
				interactShops.setInteractId(model.getInteractId());
				interactShops.setShopsId(shopsVo.getId());
				// interactShops.setIsVip(model.getIsVip());

				insertList.add(interactShops);
			}
			// 判断是否已添加
			Inno72InteractShops having = new Inno72InteractShops();
			having.setInteractId(model.getInteractId());
			List<Inno72InteractShops> havingList = inno72InteractShopsMapper.select(having);
			boolean fg = havingList.removeAll(insertList);
			if (fg) {
				logger.info("添加店铺中部分已添加过");
				return Results.failure("添加店铺中部分已添加过");
			}

			inno72InteractShopsMapper.insertInteractShopsList(insertList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(String interactId, String shopsId, Integer isVip) {
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			Inno72InteractShops interactShops = new Inno72InteractShops();
			interactShops.setShopsId(shopsId);
			interactShops.setInteractId(interactId);

			interactShops.setIsVip(isVip);
			inno72InteractShopsMapper.updateByPrimaryKeySelective(interactShops);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<ShopsVo> getList(String interactId, String merchantId) {
		logger.info("---------------------获取商户下店铺列表-------------------");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sellerId", merchantId);
		params.put("interactId", interactId);

		List<ShopsVo> inno72ShopsList = inno72InteractShopsMapper.selectMerchantShops(params);

		return inno72ShopsList;

	}

	@Override
	public ShopsVo findShopsById(String id) {
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
