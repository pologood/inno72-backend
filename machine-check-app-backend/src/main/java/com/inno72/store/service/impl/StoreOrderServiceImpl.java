package com.inno72.store.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.check.mapper.Inno72CheckGoodsDetailMapper;
import com.inno72.check.mapper.Inno72CheckGoodsNumMapper;
import com.inno72.check.model.Inno72CheckGoodsDetail;
import com.inno72.check.model.Inno72CheckGoodsNum;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.store.mapper.Inno72StoreExpressMapper;
import com.inno72.store.mapper.Inno72StoreOrderMapper;
import com.inno72.store.model.Inno72StoreExpress;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.service.StoreOrderService;
import com.inno72.store.vo.StoreOrderVo;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StoreOrderServiceImpl extends AbstractService<Inno72StoreOrder> implements StoreOrderService {
    @Resource
    private Inno72StoreOrderMapper inno72StoreOrderMapper;

    @Resource
    private Inno72StoreExpressMapper inno72StoreExpressMapper;

    @Resource
    private Inno72CheckGoodsNumMapper inno72CheckGoodsNumMapper;

    @Resource
    private Inno72CheckGoodsDetailMapper inno72CheckGoodsDetailMapper;

	@Override
	public Result<String> saveOrder(StoreOrderVo storeOrderVo) {
		String goodsId = storeOrderVo.getGoods();
		int number = storeOrderVo.getNumber();
		Inno72CheckUser user = UserUtil.getUser();
		Inno72StoreOrder inno72StoreOrder = new Inno72StoreOrder();
		LocalDateTime now = LocalDateTime.now();
		String orderId = StringUtil.getUUID();
		inno72StoreOrder.setId(orderId);
		String orderNum = "JC"+DateUtil.toTimeStr(now,DateUtil.DF_FULL_S2)+StringUtil.createVerificationCode(4);
		inno72StoreOrder.setOrderNum(orderNum);
		inno72StoreOrder.setOrderType(0);
		inno72StoreOrder.setGoods(goodsId);
		inno72StoreOrder.setSender(user.getName());
		inno72StoreOrder.setSendId(user.getId());
		inno72StoreOrder.setSendType(1);
		inno72StoreOrder.setReceiver(storeOrderVo.getReceiver());
		inno72StoreOrder.setReceiveId(storeOrderVo.getReceiveId());
		inno72StoreOrder.setReceiveType(2);
		inno72StoreOrder.setNumber(number);
		inno72StoreOrder.setStatus(0);
		inno72StoreOrder.setCreater(user.getName());
		inno72StoreOrder.setCreateTime(now);
		inno72StoreOrder.setUpdater(user.getName());
		inno72StoreOrder.setUpdateTime(now);
		inno72StoreOrder.setActivity(storeOrderVo.getActivity());
		inno72StoreOrderMapper.insertSelective(inno72StoreOrder);
		Inno72StoreExpress inno72StoreExpress = new Inno72StoreExpress();
		inno72StoreExpress.setId(StringUtil.getUUID());
		inno72StoreExpress.setOrderId(orderId);
		inno72StoreExpress.setExpressNum(storeOrderVo.getExpressNum());
		inno72StoreExpress.setExpressCompany(storeOrderVo.getExpressCompany());
		inno72StoreExpress.setNumber(number);
		inno72StoreExpress.setCreater(user.getName());
		inno72StoreExpress.setCreateTime(now);
		inno72StoreExpress.setUpdater(user.getName());
		inno72StoreExpress.setUpdateTime(now);
		inno72StoreExpressMapper.insertSelective(inno72StoreExpress);

		Map<String,Object> goodsMap = new HashMap<>();
		goodsMap.put("goodsId",goodsId);
		goodsMap.put("checkUserId",user.getId());
		goodsMap.put("activityId",storeOrderVo.getActivity());
		Inno72CheckGoodsNum goodsNum = inno72CheckGoodsNumMapper.selectByparam(goodsMap);
		if(goodsNum != null){
			int receiveTotalCount = goodsNum.getReceiveTotalCount();
			int supplyTotalCount = goodsNum.getSupplyTotalCount();
			receiveTotalCount = receiveTotalCount-number;
			int differTotalCount = receiveTotalCount-supplyTotalCount;
			goodsNum.setDifferTotalCount(differTotalCount);
			goodsNum.setReceiveTotalCount(receiveTotalCount);
			inno72CheckGoodsNumMapper.updateByPrimaryKeySelective(goodsNum);
		}else{
			int receiveTotalCount = -number;
			int supplyTotalCount = 0;
			int differTotalCount = receiveTotalCount;
			goodsNum = new Inno72CheckGoodsNum();
			goodsNum.setId(StringUtil.getUUID());
			goodsNum.setReceiveTotalCount(receiveTotalCount);
			goodsNum.setSupplyTotalCount(supplyTotalCount);
			goodsNum.setDifferTotalCount(differTotalCount);
			goodsNum.setGoodsId(goodsId);
			goodsNum.setCheckUserId(UserUtil.getUser().getId());
//			goodsNum.setActivityId(activityId);
			inno72CheckGoodsNumMapper.insertSelective(goodsNum);
		}
		Inno72CheckGoodsDetail goodsDetail = new Inno72CheckGoodsDetail();
		goodsDetail.setGoodsNumId(goodsNum.getId());
		goodsDetail.setId(StringUtil.getUUID());
		goodsDetail.setReceiveCount(-number);
		goodsDetail.setSupplyCount(0);
		goodsDetail.setDifferCount(-number);
		goodsDetail.setCreateTime(LocalDateTime.now());
		inno72CheckGoodsDetailMapper.insertSelective(goodsDetail);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72StoreOrder> findOrderByPage(StoreOrderVo storeOrderVo) {
		String keyword = storeOrderVo.getKeyword();
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isNotEmpty(keyword)){
			map.put("keyword",keyword);
		}
		String sendId = UserUtil.getUser().getId();
		map.put("sendId",sendId);
		List<Inno72StoreOrder> list = inno72StoreOrderMapper.selectOrderByPage(map);
		return list;
	}

	@Override
	public Result<String> updateOrder(StoreOrderVo storeOrderVo) {
		Inno72CheckUser user = UserUtil.getUser();
		Inno72StoreOrder inno72StoreOrder = new Inno72StoreOrder();
		LocalDateTime now = LocalDateTime.now();
		String orderId = storeOrderVo.getId();
		Inno72StoreOrder oldOrder = inno72StoreOrderMapper.selectByPrimaryKey(orderId);
		int oldCount = oldOrder.getNumber();
		int newCount = storeOrderVo.getNumber();
		inno72StoreOrder.setId(orderId);
		inno72StoreOrder.setGoods(storeOrderVo.getGoods());
		inno72StoreOrder.setReceiver(storeOrderVo.getReceiver());
		inno72StoreOrder.setReceiveId(storeOrderVo.getReceiveId());
		inno72StoreOrder.setNumber(storeOrderVo.getNumber());
		inno72StoreOrder.setUpdater(user.getName());
		inno72StoreOrder.setUpdateTime(now);
		inno72StoreOrder.setActivity(storeOrderVo.getActivity());
		inno72StoreOrderMapper.updateByPrimaryKeySelective(inno72StoreOrder);
		Inno72StoreExpress express = new Inno72StoreExpress();
		express.setOrderId(orderId);
		Inno72StoreExpress inno72StoreExpress = inno72StoreExpressMapper.selectOne(express);
		inno72StoreExpress.setExpressNum(storeOrderVo.getExpressNum());
		inno72StoreExpress.setExpressCompany(storeOrderVo.getExpressCompany());
		inno72StoreExpress.setNumber(storeOrderVo.getNumber());
		inno72StoreExpress.setUpdater(user.getName());
		inno72StoreExpress.setUpdateTime(now);
		inno72StoreExpressMapper.updateByPrimaryKeySelective(inno72StoreExpress);
		if(oldCount != newCount){
			Map<String,Object> goodsMap = new HashMap<>();
			goodsMap.put("goodsId",storeOrderVo.getGoods());
			goodsMap.put("checkUserId",user.getId());
			goodsMap.put("activityId",storeOrderVo.getActivity());
			Inno72CheckGoodsNum goodsNum = inno72CheckGoodsNumMapper.selectByparam(goodsMap);
			if(goodsNum != null){
				int receiveTotalCount = goodsNum.getReceiveTotalCount();
				int supplyTotalCount = goodsNum.getSupplyTotalCount();
				receiveTotalCount = receiveTotalCount+oldCount-newCount;
				int differTotalCount = receiveTotalCount-supplyTotalCount;
				goodsNum.setDifferTotalCount(differTotalCount);
				goodsNum.setReceiveTotalCount(receiveTotalCount);
				inno72CheckGoodsNumMapper.updateByPrimaryKeySelective(goodsNum);
			}
			Inno72CheckGoodsDetail goodsDetail = new Inno72CheckGoodsDetail();
			goodsDetail.setGoodsNumId(goodsNum.getId());
			goodsDetail.setId(StringUtil.getUUID());
			goodsDetail.setReceiveCount(oldCount-newCount);
			goodsDetail.setSupplyCount(0);
			goodsDetail.setDifferCount(oldCount-newCount);
			goodsDetail.setCreateTime(LocalDateTime.now());
			inno72CheckGoodsDetailMapper.insertSelective(goodsDetail);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Inno72StoreOrder findOrderById(String id) {
		Inno72StoreOrder storeOrder = inno72StoreOrderMapper.selectDetailById(id);
		return storeOrder;
	}

	@Override
	public Result<List<Inno72CheckGoodsNum>> findActivityList(StoreOrderVo storeOrderVo) {
		Map<String,Object> map = new HashMap<>();
		map.put("checkUserId",UserUtil.getUser().getId());
		String goodsId = storeOrderVo.getGoods();
		if(StringUtil.isNotEmpty(goodsId)){
			map.put("goodsId",goodsId);
		}
		List<Inno72CheckGoodsNum> list = inno72CheckGoodsNumMapper.selectActivityList(map);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> deleteModel(String id) {
		Inno72StoreOrder inno72StoreOrder = inno72StoreOrderMapper.selectByPrimaryKey(id);
		if(inno72StoreOrder != null){
			int isDelete = inno72StoreOrder.getIsDelete();
			if(isDelete == 1){
				return ResultGenerator.genSuccessResult();
			}
			int number = inno72StoreOrder.getNumber();
			String checkUserId = UserUtil.getUser().getId();
			String goodsId = inno72StoreOrder.getGoods();
			String activityId = inno72StoreOrder.getActivity();
			Map<String,Object> goodsMap = new HashMap<>();
			goodsMap.put("checkUserId",checkUserId);
			goodsMap.put("goodsId",goodsId);
			goodsMap.put("activityId",activityId);
			Inno72CheckGoodsNum goodsNum = inno72CheckGoodsNumMapper.selectByparam(goodsMap);
			if(goodsNum != null){
				int receiveTotalCount = goodsNum.getReceiveTotalCount();
				receiveTotalCount += number;
				int supplyTotalCount = goodsNum.getSupplyTotalCount();
				int differTotalCount = receiveTotalCount-supplyTotalCount;
				goodsNum.setReceiveTotalCount(receiveTotalCount);
				goodsNum.setDifferTotalCount(differTotalCount);
				inno72CheckGoodsNumMapper.updateByPrimaryKeySelective(goodsNum);
			}
			Inno72CheckGoodsDetail goodsDetail = new Inno72CheckGoodsDetail();
			goodsDetail.setGoodsNumId(goodsNum.getId());
			goodsDetail.setId(StringUtil.getUUID());
			goodsDetail.setReceiveCount(number);
			goodsDetail.setSupplyCount(0);
			goodsDetail.setDifferCount(number);
			goodsDetail.setCreateTime(LocalDateTime.now());
			inno72CheckGoodsDetailMapper.insertSelective(goodsDetail);
			inno72StoreOrder.setIsDelete(1);
			inno72StoreOrder.setStatus(2);
			inno72StoreOrderMapper.updateByPrimaryKeySelective(inno72StoreOrder);
		}
		return ResultGenerator.genSuccessResult();
	}
}
