package com.inno72.order.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_refund_log")
public class Inno72RefundLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 退款订单ID
	 */
	@Column(name = "refund_id")
	private String refundId;

	/**
	 * 退款单号
	 */
	@Column(name = "refund_num")
	private String refundNum;

	/**
	 * 操作人
	 */
	@Column(name = "refund_user")
	private String refundUser;

	/**
	 * 操作人
	 */
	@Column(name = "log")
	private String log;

	/**
	 * 时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取退款订单ID
	 *
	 * @return refund_id - 退款订单ID
	 */
	public String getRefundId() {
		return refundId;
	}

	/**
	 * 设置退款订单ID
	 *
	 * @param refundId
	 *            退款订单ID
	 */
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	/**
	 * 获取退款单号
	 *
	 * @return refund_num - 退款单号
	 */
	public String getRefundNum() {
		return refundNum;
	}

	/**
	 * 设置退款单号
	 *
	 * @param refundNum
	 *            退款单号
	 */
	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}

	/**
	 * 获取操作人
	 *
	 * @return refund_user - 操作人
	 */
	public String getRefundUser() {
		return refundUser;
	}

	/**
	 * 设置操作人
	 *
	 * @param refundUser
	 *            操作人
	 */
	public void setRefundUser(String refundUser) {
		this.refundUser = refundUser;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * 获取时间
	 *
	 * @return create_time - 时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置时间
	 *
	 * @param createTime
	 *            时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}