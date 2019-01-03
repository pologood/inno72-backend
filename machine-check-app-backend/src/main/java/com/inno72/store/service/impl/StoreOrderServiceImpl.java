package com.inno72.store.service.impl;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public Result<String> saveOrder(StoreOrderVo storeOrderVo) {
		Inno72CheckUser user = UserUtil.getUser();
		Inno72StoreOrder inno72StoreOrder = new Inno72StoreOrder();
		LocalDateTime now = LocalDateTime.now();
		String orderId = StringUtil.getUUID();
		inno72StoreOrder.setId(orderId);
		String orderNum = "JC"+DateUtil.toTimeStr(now,DateUtil.DF_FULL_S2)+StringUtil.createVerificationCode(4);
		inno72StoreOrder.setOrderNum(orderNum);
		inno72StoreOrder.setOrderType(0);
		inno72StoreOrder.setGoods(storeOrderVo.getGoods());
		inno72StoreOrder.setSender(user.getName());
		inno72StoreOrder.setSendId(user.getId());
		inno72StoreOrder.setSendType(1);
		inno72StoreOrder.setReceiver(storeOrderVo.getReceiver());
		inno72StoreOrder.setReceiveId(storeOrderVo.getReceiveId());
		inno72StoreOrder.setReceiveType(2);
		inno72StoreOrder.setNumber(storeOrderVo.getNumber());
		inno72StoreOrder.setStatus(0);
		inno72StoreOrder.setCreater(user.getName());
		inno72StoreOrder.setCreateTime(now);
		inno72StoreOrder.setUpdater(user.getName());
		inno72StoreOrder.setUpdateTime(now);
		inno72StoreOrderMapper.insertSelective(inno72StoreOrder);
		Inno72StoreExpress inno72StoreExpress = new Inno72StoreExpress();
		inno72StoreExpress.setId(StringUtil.getUUID());
		inno72StoreExpress.setOrderId(orderId);
		inno72StoreExpress.setExpressNum(storeOrderVo.getExpressNum());
		inno72StoreExpress.setExpressCompany(storeOrderVo.getExpressCompany());
		inno72StoreExpress.setNumber(storeOrderVo.getNumber());
		inno72StoreExpress.setCreater(user.getName());
		inno72StoreExpress.setCreateTime(now);
		inno72StoreExpress.setUpdater(user.getName());
		inno72StoreExpress.setUpdateTime(now);
		inno72StoreExpressMapper.insertSelective(inno72StoreExpress);
		return ResultGenerator.genSuccessResult();
	}
}
