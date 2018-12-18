package com.inno72.order.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_order_refund")
public class Inno72OrderRefund {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 订单id
	 */
	@Column(name = "order_id")
	private String orderId;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * 退款金额
	 */
	private Short amount;

	/**
	 * 退款原因
	 */
	private String reason;

	/**
	 * 退款证明图片
	 */
	private String url;

	/**
	 * 状态：0 新退款订单，1退款中，
	 * 
	 * 状态：0 新退款订单，1退款中，2退款成功，3退款失败
	 */
	private Integer status;

	/**
	 * 审核状态：0待审核，1已通过，2未通过
	 */
	@Column(name = "audit_status")
	private Integer auditStatus;

	/**
	 * 审核时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "audit_time")
	private LocalDateTime auditTime;

	/**
	 * 审核人
	 */
	@Column(name = "audit_user")
	private String auditUser;

	/**
	 * 审核原因
	 */
	@Column(name = "audit_reason")
	private String auditReason;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 退款单号
	 */
	@Column(name = "refund_num")
	private String refundNum;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private LocalDateTime createTime;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private LocalDateTime updateTime;

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
	 * 获取订单id
	 *
	 * @return order_id - 订单id
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * 设置订单id
	 *
	 * @param orderId
	 *            订单id
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * 获取用户id
	 *
	 * @return user_id - 用户id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户id
	 *
	 * @param userId
	 *            用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取退款金额
	 *
	 * @return amount - 退款金额
	 */
	public Short getAmount() {
		return amount;
	}

	/**
	 * 设置退款金额
	 *
	 * @param amount
	 *            退款金额
	 */
	public void setAmount(Short amount) {
		this.amount = amount;
	}

	/**
	 * 获取退款原因
	 *
	 * @return reason - 退款原因
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * 设置退款原因
	 *
	 * @param reason
	 *            退款原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 获取退款证明图片
	 *
	 * @return url - 退款证明图片
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置退款证明图片
	 *
	 * @param url
	 *            退款证明图片
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取状态：0 新退款订单，1退款中，
	 * 
	 * 状态：0 新退款订单，1退款中，2退款成功，3退款失败
	 *
	 * @return status - 状态：0 新退款订单，1退款中，
	 * 
	 *         状态：0 新退款订单，1退款中，2退款成功，3退款失败
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置状态：0 新退款订单，1退款中，
	 * 
	 * 状态：0 新退款订单，1退款中，2退款成功，3退款失败
	 *
	 * @param status
	 *            状态：0 新退款订单，1退款中，
	 * 
	 *            状态：0 新退款订单，1退款中，2退款成功，3退款失败
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取审核状态：0待审核，1已通过，2未通过
	 *
	 * @return audit_status - 审核状态：0待审核，1已通过，2未通过
	 */
	public Integer getAuditStatus() {
		return auditStatus;
	}

	/**
	 * 设置审核状态：0待审核，1已通过，2未通过
	 *
	 * @param auditStatus
	 *            审核状态：0待审核，1已通过，2未通过
	 */
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public LocalDateTime getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(LocalDateTime auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	/**
	 * 获取备注
	 *
	 * @return remark - 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 *
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 * @return create_time
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return update_time
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
}