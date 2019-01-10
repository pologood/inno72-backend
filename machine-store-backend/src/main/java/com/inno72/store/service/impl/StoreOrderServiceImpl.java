package com.inno72.store.service.impl;

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

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.store.mapper.Inno72StoreMapper;
import com.inno72.store.mapper.Inno72StoreOrderMapper;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StoreOrderService;
import com.inno72.store.vo.StoreOrderVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreOrderServiceImpl extends AbstractService<Inno72StoreOrder> implements StoreOrderService {
	private static Logger logger = LoggerFactory.getLogger(StoreOrderServiceImpl.class);

	@Resource
	private Inno72StoreOrderMapper inno72StoreOrderMapper;
	@Resource
	private Inno72StoreMapper inno72StoreMapper;

	/**
	 * 创建保存出库单
	 */
	@Override
	public Result<Object> saveModel(Inno72StoreOrder model) {

		try {
			SessionData session = SessionUtil.sessionData.get();
			Inno72Storekeeper mUser = Optional.ofNullable(session).map(SessionData::getStorekeeper).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			model.setId(StringUtil.getUUID());
			model.setCreater(mUser.getName());
			model.setUpdater(mUser.getName());
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			// 库单类型：0入库，1出库
			model.setOrderType(1);
			model.setOrderNum("C" + DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S2));
			if (StringUtil.isBlank(model.getMerchant())) {
				logger.info("请选择活动商家");
				return Results.failure("请选择活动商家");
			}
			if (StringUtil.isBlank(model.getGoods())) {
				logger.info("请选择商品");
				return Results.failure("请选择商品");
			}
			if (null == model.getNumber()) {
				logger.info("请填写商品数量");
				return Results.failure("请填写商品数量");
			}
			if (null == model.getCapacity()) {
				logger.info("请填写出库容量");
				return Results.failure("请填写出库容量");
			}
			// 发货方:0商家，1巡检，2仓库
			model.setSendType(2);
			if (null == model.getSendId()) {
				logger.info("请选择发货仓库");
				return Results.failure("请选择发货仓库");
			} else {
				Inno72Store store = inno72StoreMapper.selectByPrimaryKey(model.getSendId());
				if (null == store) {
					return Results.failure("发货仓不存在");
				}
				model.setSender(store.getName());
			}

			if (null == model.getReceiveType()) {
				logger.info("请选择收货方");
				return Results.failure("请选择收货方");
			}
			// 收货方类型：0商家，1巡检，2仓库
			if (model.getReceiveType() == 1) {
				if (null == model.getActivity()) {
					logger.info("请选择参与活动");
					return Results.failure("请选择参与活动");
				}
				if (null == model.getReceiveId()) {
					logger.info("请选择收货人员");
					return Results.failure("请选择收货人员");
				}
			}
			if (model.getReceiveType() == 2) {
				if (null == model.getReceiveId()) {
					logger.info("请选择收货仓库");
					return Results.failure("请选择收货仓库");
				}
				Inno72Store store = inno72StoreMapper.selectByPrimaryKey(model.getReceiveId());
				if (null == store) {
					return Results.failure("收货仓不存在");
				}
				model.setSender(store.getName());
			}
			// 减库存

			// 加容量

			inno72StoreOrderMapper.insertSelective(model);

			return Results.warn("操作成功", 0, model.getId());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
	}

	/**
	 * 签收入库单
	 */
	@Override
	public Result<Object> receiverConfirm(StoreOrderVo storeOrderVo) {
		logger.info("签收物流单接口参数:{}", JSON.toJSON(storeOrderVo));
		SessionData session = SessionUtil.sessionData.get();
		Inno72Storekeeper mUser = Optional.ofNullable(session).map(SessionData::getStorekeeper).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}

		List<Map<String, Object>> expressList = storeOrderVo.getExpressList();
		if (null == expressList || expressList.size() == 0) {
			logger.info("物流单未选择");
			return Results.failure("物流单未选择");
		}

		return Results.success();
	}

	/**
	 * 查询出库单
	 */
	@Override
	public List<Map<String, Object>> findSendOrderByPage(String date, String keyword) {

		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		if (StringUtil.isNotBlank(date)) {
			params.put("beginTime", date.trim() + " 00:00:00");
			params.put("endTime", date.trim() + " 23:59:59");
		}
		return inno72StoreOrderMapper.selectSendOrderByPage(params);
	}

	/**
	 * 查询入库单
	 */
	@Override
	public List<Map<String, Object>> findReceiveOrderByPage(String date, String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		return inno72StoreOrderMapper.selectReceiveOrderByPage(params);
	}

	@Override
	public StoreOrderVo findDetailById(String id) {
		logger.info("---------------------库单详情-------------------");
		StoreOrderVo vo = inno72StoreOrderMapper.selectOrderById(id);

		return vo;
	}

}
