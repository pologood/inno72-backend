package com.inno72.check.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_check_user")
public class Inno72CheckUser {
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 姓名
	 */
	@NotBlank(message = "请输入姓名")
	@Column(name = "name")
	private String name;

	/**
	 * 手机号
	 */
	@NotBlank(message = "请输入手机号")
	@Column(name = "phone")
	private String phone;

	/**
	 * 密码（预留）
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 身份证号
	 */
	@NotBlank(message = "请输入身份证号")
	@Column(name = "card_no")
	private String cardNo;

	/**
	 * 公司
	 */
	private String enterprise;

	/**
	 * 性别（1.男，2.女）
	 */
	private Integer sex;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 区域
	 */
	@Column(name = "area")
	private String area;

	/**
	 * 头像
	 */
	@Column(name = "head_image")
	private String headImage;

	/**
	 * 企业微信user_id
	 */
	@Column(name = "wechat_user_id")
	private String wechatUserId;

	/**
	 * 企业微信OpenId
	 */
	@Column(name = "open_id")
	private String openId;

	/**
	 * 人员状态
	 */
	@Column(name = "status")
	private int status;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 是否删除（0.正常，1.删除，默认为0）
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 备注描述
	 */
	@Column(name = "remark")
	private String remark;

	private String detail;

	/**
	 * 获取主键
	 *
	 * @return id - 主键
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置主键
	 *
	 * @param id
	 *            主键
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取姓名
	 *
	 * @return name - 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 *
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取手机号
	 *
	 * @return phone - 手机号
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号
	 *
	 * @param phone
	 *            手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取密码（预留）
	 *
	 * @return password - 密码（预留）
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码（预留）
	 *
	 * @param password
	 *            密码（预留）
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取身份证号
	 *
	 * @return card_no - 身份证号
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * 设置身份证号
	 *
	 * @param cardNo
	 *            身份证号
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * 获取公司
	 *
	 * @return enterprise - 公司
	 */
	public String getEnterprise() {
		return enterprise;
	}

	/**
	 * 设置公司
	 *
	 * @param enterprise
	 *            公司
	 */
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	/**
	 * 获取性别（1.男，2.女）
	 *
	 * @return sex - 性别（1.男，2.女）
	 */
	public Integer getSex() {
		return sex;
	}

	/**
	 * 设置性别（1.男，2.女）
	 *
	 * @param sex
	 *            性别（1.男，2.女）
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * 获取邮箱
	 *
	 * @return email - 邮箱
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置邮箱
	 *
	 * @param email
	 *            邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取头像
	 *
	 * @return head_image - 头像
	 */
	public String getHeadImage() {
		return headImage;
	}

	/**
	 * 设置头像
	 *
	 * @param headImage
	 *            头像
	 */
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getWechatUserId() {
		return wechatUserId;
	}

	public void setWechatUserId(String wechatUserId) {
		this.wechatUserId = wechatUserId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取是否删除（0.正常，1.删除，默认为0）
	 *
	 * @return is_delete - 是否删除（0.正常，1.删除，默认为0）
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置是否删除（0.正常，1.删除，默认为0）
	 *
	 * @param isDelete
	 *            是否删除（0.正常，1.删除，默认为0）
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}