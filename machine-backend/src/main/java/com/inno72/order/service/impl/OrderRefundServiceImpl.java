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
import com.inno72.common.DateUtil;
import com.inno72.common.Encrypt;
import com.inno72.common.MachineBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.common.json.JsonUtil;
import com.inno72.log.util.FastJsonUtils;
import com.inno72.msg.MsgUtil;
import com.inno72.order.mapper.Inno72OrderRefundMapper;
import com.inno72.order.mapper.Inno72RefundLogMapper;
import com.inno72.order.model.Inno72OrderRefund;
import com.inno72.order.model.Inno72RefundLog;
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

	@Resource
	private Inno72RefundLogMapper inno72RefundLogMapper;

	@Resource
	private MsgUtil msgUtil;

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
			if (orderRefund.getAuditStatus() == 2) {
				logger.info("审核已通过");
				return Results.failure("审核已通过！");
			}
			if (orderRefund.getStatus() == 2) {
				logger.info("已成功退款，不能再次退款！");
				return Results.failure("已成功退款，不能再次退款！");
			}
			orderRefund.setAuditStatus(Integer.parseInt(auditStatus));
			orderRefund.setAuditTime(LocalDateTime.now());
			orderRefund.setAuditUser(mUser.getName());
			// 调用退款接口
			try {
				String result = payRefund(orderRefund, mUser);
				String code = FastJsonUtils.getString(result, "code");
				String msg = FastJsonUtils.getString(result, "msg");
				if (Result.SUCCESS == Integer.parseInt(code)) {
					String status = FastJsonUtils.getString(result, "status");
					if (status.equals("11")) {
						orderRefund.setStatus(1);
						orderRefund.setRefundTime(LocalDateTime.now());
						this.sendMsgInfo(orderRefund, "1", "成功");
					} else if (status.equals("12")) {
						orderRefund.setStatus(2);
					} else if (status.equals("13")) {
						orderRefund.setStatus(3);
						this.sendMsgInfo(orderRefund, "2", msg);
					}
				} else {
					orderRefund.setStatus(3);
					this.sendMsgInfo(orderRefund, "2", msg);
				}
				orderRefund.setRefundMsg(msg);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				logger.info("退款接口调用失败:" + e.toString());
				return Results.failure("退款调用失败");
			}

		} else if (auditStatus.equals("2")) {
			orderRefund.setAuditStatus(Integer.parseInt(auditStatus));
			orderRefund.setStatus(3);
			orderRefund.setAuditTime(LocalDateTime.now());
			orderRefund.setAuditUser(mUser.getName());
			orderRefund.setAuditReason(auditReason);
			orderRefund.setRefundMsg("审核未通过");
			orderRefund.setUpdateTime(LocalDateTime.now());

			// 发送短信/模版消息
			this.sendMsgInfo(orderRefund, "3", auditReason);

			// 记录日志
			this.refundLog(orderRefund, mUser, "审核未通过");
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
			if (base.getAuditStatus() != 2) {
				logger.info("审核未通过，不能线下退款");
				return Results.failure("审核未通过，不能线下退款！");
			}
			base.setStatus(2);
			base.setRefundTime(LocalDateTime.now());
			base.setRemark(base.getRemark() + "(线下退款)");
			base.setRefundMsg("线下退款");
			// 记录日志
			this.refundLog(base, mUser, "线下退款");
		} else if (type.equals("3")) {
			if (base.getAuditStatus() != 2) {
				logger.info("审核未通过，不能线下退款");
				return Results.failure("审核未通过，不能再次退款！");
			}
			if (base.getStatus() == 2) {
				logger.info("已成功退款，不能再次退款！");
				return Results.failure("已成功退款，不能再次退款！");
			}
			try {
				String result = payRefund(base, mUser);
				String code = FastJsonUtils.getString(result, "code");
				String msg = FastJsonUtils.getString(result, "msg");
				if (Result.SUCCESS == Integer.parseInt(code)) {
					String status = FastJsonUtils.getString(result, "status");
					if (status.equals("11")) {
						base.setStatus(1);
						base.setRefundTime(LocalDateTime.now());
						this.sendMsgInfo(base, "1", "成功");
					} else if (status.equals("12")) {
						base.setStatus(2);
					} else if (status.equals("13")) {
						base.setStatus(3);
						this.sendMsgInfo(base, "2", msg);
					}
				} else if (50023 == Integer.parseInt(code)) {

				} else {
					base.setStatus(3);
					this.sendMsgInfo(base, "2", msg);
				}
				base.setRefundMsg(msg);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				logger.info("退款接口调用失败:" + e.toString());
				return Results.failure("退款调用失败");
			}
		} else {
			logger.info("参数错误");
			return Results.failure("参数错误");
		}
		base.setUpdateTime(LocalDateTime.now());
		inno72OrderRefundMapper.updateByPrimaryKeySelective(base);

		return Results.success("操作成功");
	}

	public String payRefund(Inno72OrderRefund orderRefund, Inno72User mUser) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("notifyUrl", machineBackendProperties.get("notifyPayRefundUrl"));
		param.put("spId", "1001");
		param.put("outTradeNo", orderRefund.getOrderId());
		param.put("outRefundNo", orderRefund.getId());
		BigDecimal temp = new BigDecimal(100);
		param.put("amount", orderRefund.getAmount().multiply(temp).longValue() + "");
		param.put("reason", orderRefund.getReason());
		String sign = genSign(param);
		param.put("sign", sign);
		String result = doInvoke(param);
		refundLog(orderRefund, mUser, result);
		return result;
	}

	private String doInvoke(Map<String, String> param) {
		logger.info("payRefund invoke param = {}", JsonUtil.toJson(param));
		String respJson = HttpClient.form(machineBackendProperties.get("payRefundServiceUrl"), param, null);
		logger.info("payRefund invoke response = {}", respJson);
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

	private void refundLog(Inno72OrderRefund orderRefund, Inno72User mUser, String msg) {
		Inno72RefundLog log = new Inno72RefundLog();
		log.setId(StringUtil.getUUID());
		log.setRefundId(orderRefund.getId());
		log.setRefundNum(orderRefund.getRefundNum());
		log.setRefundUser(mUser.getName());
		log.setLog(msg);
		inno72RefundLogMapper.insert(log);
	}

	private String sendMsgInfo(Inno72OrderRefund orderRefund, String type, String msg) {
		// type 1，退款成功 2，失败 3，审核未通过
		// String active = System.getenv("spring_profiles_active");
		String appName = "machineBackend";
		String smsCode = "sms_order_refund";
		// String wechatCode = "";

		Map<String, String> params = new HashMap<>();
		if (type.endsWith("1")) {
			params.put("title", "费用已退回至您的支付账户");
			params.put("text", "退款方式：原路退回");
		} else if (type.endsWith("2")) {
			params.put("title", "您的退款失败");
			params.put("text", "失败原因：" + msg);
		} else if (type.endsWith("3")) {
			params.put("title", "您的退款审核被拒绝");
			if (StringUtil.isBlank(msg)) {
				params.put("text", "失败原因：已经补发商品");
			} else {
				params.put("text", "失败原因：" + msg);
			}

		}
		params.put("orderNo", orderRefund.getRefundNum());
		params.put("amount", orderRefund.getAmount() + "元");
		params.put("date", "时间：" + DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_ONLY_YMDHM_S1));

		msgUtil.sendSMS(smsCode, params, orderRefund.getPhone(), appName);
		// msgUtil.sendWechatTemplate(wechatCode, params, firstContent,
		// remarkContent, openid, null, appName);
		return "ok";
	}

	@Override
	public Integer refundOrderCount(int countType) {
		return inno72OrderRefundMapper.selectRefundOrderCount(countType);
	}

}
