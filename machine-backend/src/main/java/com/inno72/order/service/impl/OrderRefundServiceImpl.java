package com.inno72.order.service.impl;

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

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.order.mapper.Inno72OrderRefundMapper;
import com.inno72.order.model.Inno72OrderRefund;
import com.inno72.order.service.OrderRefundService;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/12/17.
 */
@Service
@Transactional
public class OrderRefundServiceImpl extends AbstractService<Inno72OrderRefund> implements OrderRefundService {
	private static Logger logger = LoggerFactory.getLogger(OrderRefundServiceImpl.class);

	@Resource
	private Inno72OrderRefundMapper inno72OrderRefundMapper;

	@Override
	public List<Map<String, Object>> getRefundList(String channel, String time, String status, String auditStatus,
			String keyword) {
		Map<String, Object> params = new HashMap<String, Object>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);
		params.put("channel", channel);
		params.put("status", status);
		params.put("auditStatus", auditStatus);

		if (StringUtil.isNotBlank(time)) {
			params.put("beginTime", time.trim() + " 00:00:00");
			params.put("endTime", time.trim() + " 23:59:59");
		}

		List<Map<String, Object>> orderList = inno72OrderRefundMapper.selectByPage(params);
		return orderList;
	}

	@Override
	public Map<String, Object> selectRefundDetail(String id) {

		Map<String, Object> orderList = inno72OrderRefundMapper.selectRefundDetail(id);
		return orderList;
	}

	@Override
	public Result<String> refundAudit(String id, String auditStatus, String auditReason) {
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}
		if (StringUtil.isBlank(id)) {
			logger.info("审核记录？");
			return Results.failure("审核记录？");
		}
		if (StringUtil.isBlank(auditStatus)) {
			logger.info("审核是否通过？");
			return Results.failure("审核是否通过？");
		}
		Inno72OrderRefund orderRefund = inno72OrderRefundMapper.selectByPrimaryKey(id);
		if (null == orderRefund) {
			logger.info("订单不存在");
			return Results.failure("订单不存在");
		}
		// 审核状态：1通过（调用退款接口），2不通过（发送微信消息模版）
		if (auditStatus.equals("1")) {

			orderRefund.setAuditStatus(Integer.parseInt(auditStatus));
			orderRefund.setAuditTime(LocalDateTime.now());
			orderRefund.setAuditUser(mUser.getName());
			inno72OrderRefundMapper.updateByPrimaryKeySelective(orderRefund);
			// 调用退款接口

		} else if (auditStatus.equals("2")) {
			orderRefund.setAuditStatus(Integer.parseInt(auditStatus));
			orderRefund.setAuditTime(LocalDateTime.now());
			orderRefund.setAuditUser(mUser.getName());
			orderRefund.setAuditReason(auditReason);
			orderRefund.setUpdateTime(LocalDateTime.now());
			inno72OrderRefundMapper.updateByPrimaryKeySelective(orderRefund);
			// 发送微信模版消息

		} else {
			logger.info("参数错误");
			return Results.failure("参数错误");
		}
		return Results.success("操作成功");
	}

	@Override
	public Result<String> updateModle(Inno72OrderRefund model, String type) {

		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			logger.info("登陆用户为空");
			return Results.failure("未找到用户登录信息");
		}

		if (StringUtil.isBlank(type)) {
			logger.info("参数错误");
			return Results.failure("参数错误");
		}

		return Results.success("操作成功");
	}

}
