package com.inno72.order.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
import com.inno72.common.Encrypt;
import com.inno72.common.MachineBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.common.json.JsonUtil;
import com.inno72.log.util.FastJsonUtils;
import com.inno72.order.mapper.Inno72OrderRefundMapper;
import com.inno72.order.model.Inno72OrderRefund;
import com.inno72.order.service.OrderRefundService;
import com.inno72.plugin.http.HttpClient;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/12/17.
 */
@Service
@Transactional
public class OrderRefundServiceImpl extends AbstractService<Inno72OrderRefund> implements OrderRefundService {
	private static Logger logger = LoggerFactory.getLogger(OrderRefundServiceImpl.class);

	@Resource
	private MachineBackendProperties machineBackendProperties;

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
			// 调用退款接口
			try {
				String status = payRefund(orderRefund);
				if (status.equals("11")) {
					orderRefund.setStatus(1);
				} else if (status.equals("12")) {
					orderRefund.setStatus(2);
				} else if (status.equals("13")) {
					orderRefund.setStatus(3);
				}
			} catch (Exception e) {
				logger.info("退款接口调用失败:" + e.getMessage());
				return Results.failure("退款接口调用失败！");
			}

		} else if (auditStatus.equals("2")) {
			orderRefund.setAuditStatus(Integer.parseInt(auditStatus));
			orderRefund.setStatus(3);
			orderRefund.setAuditTime(LocalDateTime.now());
			orderRefund.setAuditUser(mUser.getName());
			orderRefund.setAuditReason(auditReason);
			orderRefund.setUpdateTime(LocalDateTime.now());

			// 发送微信模版消息

		} else {
			logger.info("参数错误");
			return Results.failure("参数错误");
		}
		inno72OrderRefundMapper.updateByPrimaryKeySelective(orderRefund);
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
		Inno72OrderRefund base = inno72OrderRefundMapper.selectByPrimaryKey(model.getId());
		if (type.equals("1")) {
			if (StringUtil.isBlank(model.getRemark())) {
				logger.info("备注不能为空！");
				return Results.failure("备注不能为空！");
			}
			base.setRemark(model.getRemark());
		} else if (type.equals("2")) {
			base.setStatus(2);
			base.setRefundTime(LocalDateTime.now());
			base.setRemark(base.getRemark() + "(线下退款)");
		} else if (type.equals("3")) {
			try {
				String status = payRefund(base);
				if (status.equals("11")) {
					base.setStatus(1);
				} else if (status.equals("12")) {
					base.setStatus(2);
				} else if (status.equals("13")) {
					base.setStatus(3);
				}
			} catch (Exception e) {
				logger.info("退款接口调用失败:" + e.getMessage());
				return Results.failure("退款接口调用失败！");
			}
		} else {
			logger.info("参数错误");
			return Results.failure("参数错误");
		}
		base.setUpdateTime(LocalDateTime.now());
		inno72OrderRefundMapper.updateByPrimaryKeySelective(base);

		return Results.success("操作成功");
	}

	public String payRefund(Inno72OrderRefund orderRefund) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("notifyUrl", machineBackendProperties.get("notifyUrl"));
		param.put("outTradeNo", orderRefund.getOrderId());
		param.put("outRefundNo", orderRefund.getId());
		BigDecimal temp = new BigDecimal(100);
		param.put("amount", orderRefund.getAmount().multiply(temp).longValue() + "");
		param.put("reason", orderRefund.getReason());
		String sign = genSign(param);
		param.put("sign", sign);
		String result = doInvoke(param);
		// 状态 11 退款申请中 12 退款成功 13 退款失败
		String status = FastJsonUtils.getString(result, "status");
		return status;
	}

	private String doInvoke(Map<String, String> param) {
		logger.info("pay invoke param = {}", JsonUtil.toJson(param));
		String respJson = HttpClient.form(machineBackendProperties.get("payRefundServiceUrl"), param, null);
		logger.info("pay invoke response = {}", respJson);
		return respJson;
	}

	private String genSign(Map<String, String> param) {
		String paramStr = createLinkString(param);
		String secureKey = machineBackendProperties.get("secureKey");
		String sign = paramStr + "&" + secureKey;
		sign = Encrypt.md5(sign);
		return sign;
	}

	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	@Override
	public Integer refundOrderCount(int countType) {
		return inno72OrderRefundMapper.selectRefundOrderCount(countType);
	}

}
