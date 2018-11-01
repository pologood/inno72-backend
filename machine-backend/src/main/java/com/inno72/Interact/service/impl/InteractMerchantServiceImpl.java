package com.inno72.Interact.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.Interact.mapper.Inno72InteractMerchantMapper;
import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.service.InteractMerchantService;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.Interact.vo.MachineSellerVo;
import com.inno72.common.AbstractService;
import com.inno72.common.ExportExcel;
import com.inno72.common.MachineBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.plugin.http.HttpClient;
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
	private Inno72InteractMerchantMapper inno72InteractMerchantMapper;
	@Resource
	private Inno72MerchantMapper inno72MerchantMapper;
	@Resource
	private Inno72ShopsMapper inno72ShopsMapper;
	@Resource
	private MachineBackendProperties machineBackendProperties;

	// 表格
	public static final String[] USERCHARGE = { "省份名(prov_name)", "市名称(city_name)", "区域名称(area_name)",
			"街道名称(street_name)", "品牌名称(brand_name)", "商家编码(out_id)", "店名(store_name)", "分店名(sub_store_name)",
			"展示名称(display)", "地址(address)", "联系电话(contact)", "咨询电话(hotline)", "图片地址(pic_addr)", "支付宝账号(alipay_account)",
			"支付宝实名(alipay_real_name)", "营业时间(bustime)", "营业时间描述(bustime_desc)", "门店旺旺(wangwang)", "门店邮箱(email)",
			"核销账号(write_off_account)", "法人姓名(legal_person_name)", "法人身份证号(legal_cert_no)", "营业主体名称(license_name)",
			"营业主体类型(license_type)", "营业执照编号(license_code)" };
	public static final String[] USERCOLUMN = { "province", "city", "", "", "", "", "shopName", "", "", "area",
			"telNum", "", "img", "", "", "", "date", "", "", "", "", "", "", "", "" };

	@Override
	public Result<String> save(InteractMerchantVo model) {

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
			if (StringUtil.isBlank(model.getChannelId())) {
				logger.info("请选择渠道");
				return Results.failure("请选择渠道");
			}
			if (StringUtil.isBlank(model.getMerchantCode())) {
				logger.info("请填写商家编号");
				return Results.failure("请填写商家编号");
			}
			if (StringUtil.isBlank(model.getMerchantName())) {
				logger.info("请填写商家名称");
				return Results.failure("请填写商家名称");
			}

			Inno72InteractMerchant interactMerchant = new Inno72InteractMerchant();
			interactMerchant.setId(StringUtil.getUUID());
			interactMerchant.setInteractId(model.getInteractId());
			interactMerchant.setMerchantId(model.getId());

			inno72InteractMerchantMapper.insert(interactMerchant);
			inno72MerchantMapper.insert(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> update(InteractMerchantVo model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setUpdateId(mUserId);
			model.setUpdateTime(LocalDateTime.now());
			// 数据校验
			if (StringUtil.isBlank(model.getChannelId())) {
				logger.info("请选择渠道");
				return Results.failure("请选择渠道");
			}
			if (StringUtil.isBlank(model.getMerchantCode())) {
				logger.info("请填写商家编号");
				return Results.failure("请填写商家编号");
			}
			if (StringUtil.isBlank(model.getMerchantName())) {
				logger.info("请填写商家名称");
				return Results.failure("请填写商家名称");
			}

			inno72MerchantMapper.updateByPrimaryKeySelective(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success("操作成功");
	}

	@Override
	public List<InteractMerchantVo> getList(String interactId) {
		logger.info("---------------------获取活动下商户列表-------------------");
		return inno72InteractMerchantMapper.selectMerchantByInteractId(interactId);

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

			Inno72Merchant merchant = new Inno72Merchant();
			merchant.setId(merchantId);
			merchant.setIsDelete(1);
			merchant.setUpdateId(mUserId);
			merchant.setUpdateTime(LocalDateTime.now());
			inno72MerchantMapper.updateByPrimaryKeySelective(merchant);

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
	public void findMachineSellerId(String activityId, Integer activityType, HttpServletResponse response) {

		List<MachineSellerVo> list = null;
		if (!StringUtil.isEmpty(activityId)) {
			if (1 == activityType) {
				// 派样
				list = inno72InteractMerchantMapper.findMachineSellerId1(activityId);
			} else {
				// 互动
				list = inno72InteractMerchantMapper.findMachineSellerId2(activityId);
			}
		}

		for (MachineSellerVo machineSellerVo : list) {
			try {
				logger.info("查询门店开始:");
				System.out.println(machineSellerVo.getShopName());
				String gameServiceUrl = machineBackendProperties.getProps().get("gameServiceUrl")
						+ "newretail/findStores?storeName={0}";
				String url = MessageFormat.format(gameServiceUrl, machineSellerVo.getShopName());
				String result = HttpClient.get(url);
				logger.info("查询门店结束" + result);
				JSONObject resultJson = JSON.parseObject(result);
				if (StringUtil.isNotBlank(resultJson.getString("data"))) {
					list.remove(machineSellerVo);
				}
			} catch (Exception e) {
				logger.error("查找门店异常{}", e.getMessage());
				throw e;
			}
		}

		int size = list.size();
		if (list != null && size > 0) {
			ExportExcel<MachineSellerVo> ee = new ExportExcel<MachineSellerVo>();
			// 导出excel
			ee.setResponseHeader(USERCHARGE, USERCOLUMN, list, response, "门店导出");
		}

	}

}
