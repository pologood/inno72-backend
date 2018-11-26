package com.inno72.Interact.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.Interact.mapper.Inno72InteractGoodsMapper;
import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.mapper.Inno72InteractMerchantMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.service.InteractMerchantService;
import com.inno72.Interact.vo.Inno72NeedExportStore;
import com.inno72.Interact.vo.InteractGoodsVo;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.Interact.vo.MerchantVo;
import com.inno72.common.AbstractService;
import com.inno72.common.ExportExcel;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72MerchantMapper;
import com.inno72.project.mapper.Inno72ShopsMapper;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.model.Inno72Shops;
import com.inno72.system.model.Inno72User;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class InteractMerchantServiceImpl extends AbstractService<Inno72InteractMerchant>
		implements InteractMerchantService {

	private static Logger logger = LoggerFactory.getLogger(InteractMerchantServiceImpl.class);
	@Resource
	private Inno72InteractMapper inno72InteractMapper;
	@Resource
	private Inno72InteractMerchantMapper inno72InteractMerchantMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;
	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;

	@Resource
	private Inno72InteractGoodsMapper inno72InteractGoodsMapper;

	// 表格
	public static final String[] USERCHARGE = { "省份名(prov_name)", "市名称(city_name)", "区域名称(area_name)",
			"街道名称(street_name)", "品牌名称(brand_name)", "商家编码(out_id)", "店名(store_name)", "分店名(sub_store_name)",
			"展示名称(display)", "地址(address)", "联系电话(contact)", "咨询电话(hotline)", "图片地址(pic_addr)", "支付宝账号(alipay_account)",
			"支付宝实名(alipay_real_name)", "营业时间(bustime)", "营业时间描述(bustime_desc)", "门店旺旺(wangwang)", "门店邮箱(email)",
			"核销账号(write_off_account)", "法人姓名(legal_person_name)", "法人身份证号(legal_cert_no)", "营业主体名称(license_name)",
			"营业主体类型(license_type)", "营业执照编号(license_code)" };
	public static final String[] USERCOLUMN = { "province", "city", "", "", "", "", "shopName", "", "", "locale",
			"phone", "", "img", "", "", "date", "", "", "", "", "", "", "", "", "" };

	@Override
	public Result<Object> save(InteractMerchantVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			List<MerchantVo> merchants = model.getMerchants();
			if (merchants.size() == 0) {
				logger.info("请添加商户");
				return Results.failure("请添加商户");
			}
			List<Inno72InteractMerchant> insertList = new ArrayList<>();
			for (MerchantVo merchant : merchants) {
				Inno72InteractMerchant interactMerchant = new Inno72InteractMerchant();
				interactMerchant.setId(StringUtil.getUUID());
				interactMerchant.setInteractId(model.getInteractId());
				interactMerchant.setMerchantId(merchant.getId());
				interactMerchant.setIsFocus(merchant.getIsFocus());

				insertList.add(interactMerchant);
			}
			// 判断是否已添加
			Inno72InteractMerchant having = new Inno72InteractMerchant();
			having.setInteractId(model.getInteractId());
			List<Inno72InteractMerchant> havingList = inno72InteractMerchantMapper.select(having);
			boolean fg = havingList.removeAll(insertList);
			if (fg) {
				logger.info("添加商户中部分已添加过");
				return Results.failure("添加商户中部分已添加过");
			}
			inno72InteractMerchantMapper.insertInteractMerchantList(insertList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(String interactId, String merchantId, Integer isFocus) {
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}

			Inno72InteractMerchant interactMerchant = new Inno72InteractMerchant();
			interactMerchant.setMerchantId(merchantId);
			interactMerchant.setInteractId(interactId);

			interactMerchant.setIsFocus(isFocus);
			inno72InteractMerchantMapper.updateByPrimaryKeySelective(interactMerchant);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<MerchantVo> getList(String interactId) {
		logger.info("---------------------获取活动下商户列表-------------------");
		return inno72InteractMerchantMapper.selectMerchantByInteractId(interactId);

	}

	@Override
	public List<Map<String, Object>> getMerchantUserList(String keyword) {
		logger.info("---------------------获取客户列表-------------------");
		return inno72InteractMerchantMapper.getMerchantUserList(keyword);

	}

	@Override
	public List<Map<String, Object>> checkMerchant(String interactId, String merchantAccountId) {
		logger.info("---------------------获取客户下商户列表-------------------");
		Inno72Interact interact = inno72InteractMapper.selectByPrimaryKey(interactId);
		Map<String, Object> pm = new HashMap<>();
		pm.put("merchantAccountId", merchantAccountId);
		pm.put("channel", interact.getChannel());
		return inno72InteractMerchantMapper.getMerchantList(pm);

	}

	@Override
	public Inno72Merchant findMerchantsById(String id) {
		return inno72MerchantMapper.selectByPrimaryKey(id);
	}

	@Override
	public Result<String> deleteById(String interactId, String merchantId) {
		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			// 先判断是否微信渠道（微信渠道判断是否存在商品，非微信渠道判断是否存在店铺）
			Inno72Merchant merchant = new Inno72Merchant();
			merchant.setId(merchantId);
			merchant = inno72MerchantMapper.selectByPrimaryKey(merchantId);
			if (merchant.getChannelId().endsWith("002002")) {
				// 查询店铺下是否存在商品(微信公众号商户下店铺商户ID与店铺ID相等)
				Map<String, Object> pm = new HashMap<>();
				pm.put("interactId", interactId);
				pm.put("shopsId", merchantId);
				List<InteractGoodsVo> goods = inno72InteractGoodsMapper.selectGoods(pm);
				if (goods != null && goods.size() > 0) {
					logger.info("商户下存在商品，不能删除");
					return Results.failure("商户下存在商品，不能删除");
				}
				// 删除公众号下虚拟店铺
				/*
				 * Inno72Shops inno72Shops = new Inno72Shops();
				 * inno72Shops.setId(merchantId);
				 * inno72Shops.setSellerId(merchantId);
				 * inno72Shops.setIsDelete(1); inno72Shops.setUpdateId(mUserId);
				 * inno72ShopsMapper.updateByPrimaryKeySelective(inno72Shops);
				 */
			} else {
				// 查询店铺下是否存在店铺
				Inno72Shops inno72Shops = new Inno72Shops();
				inno72Shops.setIsDelete(0);
				inno72Shops.setSellerId(merchantId);
				Condition condition = new Condition(Inno72Shops.class);
				condition.createCriteria().andEqualTo(inno72Shops);
				List<Inno72Shops> shops = inno72ShopsMapper.selectByCondition(condition);
				if (shops != null && shops.size() > 0) {
					logger.info("商户下存在店铺，不能删除");
					return Results.failure("商户下存在店铺，不能删除");
				}
			}
			// 中间表
			Inno72InteractMerchant interactMerchant = new Inno72InteractMerchant();
			interactMerchant.setMerchantId(merchantId);
			interactMerchant.setInteractId(interactId);
			inno72InteractMerchantMapper.delete(interactMerchant);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public void findMachineSellerId(String activityId, String activityType, HttpServletResponse response) {

		Map<String, Object> pm = new HashMap<>();
		pm.put("activityId", activityId);
		pm.put("activityType", activityType);
		// 派样
		List<Inno72NeedExportStore> list = inno72InteractMerchantMapper.findMachineSellerId(pm);
		ExportExcel<Inno72NeedExportStore> ee = new ExportExcel<Inno72NeedExportStore>();
		// 导出excel
		ee.setResponseHeader(USERCHARGE, USERCOLUMN, list, response, "门店导出");
	}

}
