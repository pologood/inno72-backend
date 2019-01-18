package com.inno72.store.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
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
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.store.mapper.Inno72CheckGoodsDetailMapper;
import com.inno72.store.mapper.Inno72CheckGoodsNumMapper;
import com.inno72.store.mapper.Inno72StoreExpressMapper;
import com.inno72.store.mapper.Inno72StoreGoodsDetailMapper;
import com.inno72.store.mapper.Inno72StoreGoodsMapper;
import com.inno72.store.mapper.Inno72StoreMapper;
import com.inno72.store.mapper.Inno72StoreOrderMapper;
import com.inno72.store.model.Inno72CheckGoodsDetail;
import com.inno72.store.model.Inno72CheckGoodsNum;
import com.inno72.store.model.Inno72Store;
import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.model.Inno72StoreGoods;
import com.inno72.store.model.Inno72StoreGoodsDetail;
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

	@Resource
	private Inno72StoreGoodsMapper inno72StoreGoodsMapper;
	@Resource
	private Inno72StoreExpressMapper inno72StoreExpressMapper;

	@Resource
	private Inno72CheckGoodsNumMapper inno72CheckGoodsNumMapper;

	@Resource
	private Inno72CheckGoodsDetailMapper inno72CheckGoodsDetailMapper;

	@Resource
	private Inno72StoreGoodsDetailMapper inno72StoreGoodsDetailMapper;

	/**
	 * 创建保存出库单
	 */
	@Override
	public Result<Object> saveModel(Inno72StoreOrder model) {

		try {
			Inno72Storekeeper mUser = UserUtil.getKepper();
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
			Inno72Store store = null;
			if (model.getReceiveType() == 2) {
				if (null == model.getReceiveId()) {
					logger.info("请选择收货仓库");
					return Results.failure("请选择收货仓库");
				}
				store = inno72StoreMapper.selectByPrimaryKey(model.getReceiveId());
				if (null == store) {
					return Results.failure("收货仓不存在");
				}
				model.setSender(store.getName());
			}
			Inno72CheckGoodsNum checkGoodsNum = new Inno72CheckGoodsNum();
			if (model.getReceiveType() == 1) {
				if (null == model.getActivity()) {
					logger.info("请选择参与活动");
					return Results.failure("请选择参与活动");
				}
				if (null == model.getReceiveId()) {
					logger.info("请选择收货人员");
					return Results.failure("请选择收货人员");
				}
				checkGoodsNum.setActivityId(model.getActivity());
				checkGoodsNum.setCheckUserId(model.getReceiveId());
				checkGoodsNum.setGoodsId(model.getGoods());
				checkGoodsNum = inno72CheckGoodsNumMapper.selectOne(checkGoodsNum);

				if (null == checkGoodsNum) {
					checkGoodsNum = new Inno72CheckGoodsNum();
					checkGoodsNum.setId(StringUtil.getUUID());
					checkGoodsNum.setActivityId(model.getActivity());
					checkGoodsNum.setCheckUserId(model.getReceiveId());
					checkGoodsNum.setGoodsId(model.getGoods());

					checkGoodsNum.setReceiveTotalCount(model.getNumber());
					checkGoodsNum.setDifferTotalCount(model.getNumber());
					checkGoodsNum.setSupplyTotalCount(0);
					inno72CheckGoodsNumMapper.insert(checkGoodsNum);
				} else {
					checkGoodsNum.setReceiveTotalCount(checkGoodsNum.getReceiveTotalCount() + model.getNumber());
					checkGoodsNum.setDifferTotalCount(
							checkGoodsNum.getReceiveTotalCount() - checkGoodsNum.getSupplyTotalCount());

					inno72CheckGoodsNumMapper.updateByPrimaryKey(checkGoodsNum);
				}
				Inno72CheckGoodsDetail checkGoodsDetail = new Inno72CheckGoodsDetail();
				checkGoodsDetail.setId(StringUtil.getUUID());
				checkGoodsDetail.setCreateTime(new Date());
				checkGoodsDetail.setReceiveCount(model.getNumber());
				checkGoodsDetail.setDifferCount(model.getNumber());
				checkGoodsDetail.setGoodsNumId(checkGoodsNum.getId());
				inno72CheckGoodsDetailMapper.insert(checkGoodsDetail);

			}

			// 仓库增加相应容量
			if (null != store) {
				store.setCapacity(store.getCapacity() + model.getCapacity());
			}
			Inno72StoreGoods storeGoods = new Inno72StoreGoods();
			storeGoods.setGoodsId(model.getGoods());
			storeGoods.setStoreId(model.getSendId());
			storeGoods = inno72StoreGoodsMapper.selectOne(storeGoods);
			// 商品库存减少，容量增加
			storeGoods.setNumber(storeGoods.getNumber() - model.getNumber());
			storeGoods.setCapacity(storeGoods.getCapacity() + model.getCapacity());
			inno72StoreGoodsMapper.updateByPrimaryKeySelective(storeGoods);
			// 保存出库单
			inno72StoreOrderMapper.insertSelective(model);

			this.storeGoodsDetail(model, storeGoods.getNumber(), storeGoods.getCapacity(), storeGoods.getId());

			return Results.warn("操作成功", 0, null);

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
		// logger.info("签收物流单接口参数:{}", JSON.toJSON(storeOrderVo));
		Inno72Storekeeper mUser = UserUtil.getKepper();
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}

		List<Inno72StoreExpress> expressList = storeOrderVo.getExpressList();
		if (null == expressList || expressList.size() == 0) {
			logger.info("物流单未选择");
			return Results.failure("物流单未选择");
		}
		// 获取商品库存
		Inno72StoreOrder storeOrder = inno72StoreOrderMapper.selectByPrimaryKey(storeOrderVo.getId());

		Inno72Store store = new Inno72Store();
		store.setId(storeOrder.getReceiveId());
		store = inno72StoreMapper.selectOne(store);

		// 签收总数量
		int totalNumber = 0;
		int totalCapacity = 0;

		for (Inno72StoreExpress express : expressList) {
			totalNumber += express.getReceiveNumber();
			totalCapacity += express.getReceiveCapacity();
			express.setReceiveTime(LocalDateTime.now());
			express.setReceiver(mUser.getName());
			express.setUpdater(mUser.getName());
			express.setUpdateTime(LocalDateTime.now());
			// 更新物流单
			inno72StoreExpressMapper.updateByPrimaryKeySelective(express);
		}
		// 更新库存数量
		Inno72StoreGoods storeGoods = new Inno72StoreGoods();
		storeGoods.setGoodsId(storeOrder.getGoods());
		storeGoods.setStoreId(storeOrder.getReceiveId());
		storeGoods = inno72StoreGoodsMapper.selectOne(storeGoods);
		if (null == storeGoods) {
			storeGoods = new Inno72StoreGoods();
			storeGoods.setId(StringUtil.getUUID());
			storeGoods.setGoodsId(storeOrder.getGoods());
			storeGoods.setStoreId(storeOrder.getReceiveId());
			storeGoods.setNumber(totalNumber);
			storeGoods.setCapacity(totalCapacity);
			storeGoods.setUpdateId(mUser.getId());
			storeGoods.setUpdateTime(LocalDateTime.now());
			inno72StoreGoodsMapper.insert(storeGoods);
		} else {
			storeGoods.setNumber(storeGoods.getNumber() + totalNumber);
			storeGoods.setCapacity(storeGoods.getCapacity() + totalCapacity);
			inno72StoreGoodsMapper.updateByPrimaryKeySelective(storeGoods);
		}

		// 更新仓库容量
		store.setCapacity(store.getCapacity() - totalCapacity);
		inno72StoreMapper.updateByPrimaryKeySelective(store);

		// 更新入库单：状态，实收数量，实收所占容量，
		Inno72StoreExpress ee = new Inno72StoreExpress();
		ee.setOrderId(storeOrderVo.getId());
		int all = inno72StoreExpressMapper.selectCount(ee);
		ee.setStatus(1);
		int con = inno72StoreExpressMapper.selectCount(ee);
		if (all == con) {
			storeOrder.setStatus(1);
			storeOrder.setReceiveTime(LocalDateTime.now());
		}
		storeOrder.setReceiveNumber(
				null == storeOrder.getReceiveNumber() ? totalNumber : (storeOrder.getReceiveNumber() + totalNumber));
		storeOrder.setReceiveCapacity(null == storeOrder.getReceiveCapacity() ? totalCapacity
				: (storeOrder.getReceiveCapacity() + totalCapacity));

		inno72StoreOrderMapper.updateByPrimaryKeySelective(storeOrder);
		this.storeGoodsDetail(storeOrder, totalNumber, totalCapacity, storeGoods.getId());
		return Results.success();
	}

	/**
	 * 记录商品出入库记录
	 */
	public void storeGoodsDetail(Inno72StoreOrder storeOrder, Integer number, Integer capacity, String storeGoodsId) {
		StringBuffer detail = new StringBuffer();
		Inno72StoreGoodsDetail storeGoodsDetail = new Inno72StoreGoodsDetail();
		if (storeOrder.getOrderType() == 0) {
			detail.append("由");
			detail.append(storeOrder.getSender());
			detail.append("发来商品");
			detail.append(number.toString());
			detail.append("件  入库完成");
			storeGoodsDetail.setType(0);
		} else {
			detail.append("由仓库");
			detail.append(storeOrder.getSender());
			detail.append("发给 ");
			detail.append(storeOrder.getReceiver());
			detail.append(" 商品 ");
			detail.append(number.toString());
			detail.append("件  出库完成");
			storeGoodsDetail.setType(1);
		}

		storeGoodsDetail.setId(StringUtil.getUUID());
		storeGoodsDetail.setStoreGoodsId(storeGoodsId);
		storeGoodsDetail.setNumber(number);
		storeGoodsDetail.setCapacity(capacity);
		storeGoodsDetail.setUpdateTime(LocalDateTime.now());
		storeGoodsDetail.setDetail(detail.toString());
		logger.info("记录商品出入库记录:{}", JSON.toJSON(storeGoodsDetail));
		inno72StoreGoodsDetailMapper.insert(storeGoodsDetail);

	}

	/**
	 * 查询出库单
	 */
	@Override
	public List<Map<String, Object>> findSendOrderByPage(String date, String keyword) {

		Inno72Storekeeper mUser = UserUtil.getKepper();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", mUser.getId());

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
		Inno72Storekeeper mUser = UserUtil.getKepper();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", mUser.getId());

		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		if (StringUtil.isNotBlank(date)) {
			params.put("beginTime", date.trim() + " 00:00:00");
			params.put("endTime", date.trim() + " 23:59:59");
		}
		return inno72StoreOrderMapper.selectReceiveOrderByPage(params);
	}

	@Override
	public StoreOrderVo findDetailById(String id) {
		logger.info("---------------------库单详情-------------------");
		StoreOrderVo vo = inno72StoreOrderMapper.selectOrderById(id);

		return vo;
	}

	@Override
	public List<Map<String, Object>> getGoodsList(String merchantId, String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();

		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("sellerId", merchantId);

		return inno72StoreOrderMapper.getGoodsList(params);
	}

	@Override
	public List<Map<String, Object>> getMerchantList(String keyword) {

		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);

		return inno72StoreOrderMapper.getMerchantList(params);
	}

	@Override
	public List<Map<String, Object>> getActivityList(String keyword) {

		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);

		return inno72StoreOrderMapper.getActivityList(params);
	}

	@Override
	public List<Map<String, Object>> getCheckUserList(String keyword) {

		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);

		return inno72StoreOrderMapper.getCheckUserList(params);
	}

	@Override
	public Result<Map<String, Object>> getHomePageInfo() {
		String userId = UserUtil.getKepper().getId();
		int pendingStorageCount = inno72StoreOrderMapper.selectPendingStorageCount(userId);
		Map<String, Object> map = new HashMap<>();
		map.put("pendingStorageCount", pendingStorageCount);
		return ResultGenerator.genSuccessResult(map);
	}

}
