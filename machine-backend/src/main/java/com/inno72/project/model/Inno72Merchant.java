package com.inno72.project.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_merchant")
public class Inno72Merchant {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 商户主键
	 */
	@NotNull(message = "请选择商户")
	@Column(name = "merchant_account_id")
	private String merchantAccountId;
	/**
	 * 商户主键
	 */
	@NotNull(message = "请选择商户")
	@Column(name = "merchant_account_name")
	private String merchantAccountName;

	/**
	 * 商户号
	 */
	@NotBlank(message = "请填写商家编码")
	@Column(name = "merchant_code")
	private String merchantCode;

	/**
	 * 商户名称
	 */
	@Column(name = "brand_name")
	private String brandName;

	/**
	 * 原始标示
	 */
	@Column(name = "origin_flag")
	private String originFlag;

	/**
	 * 商户名称
	 */
	@NotBlank(message = "请填写商家名称")
	@Column(name = "merchant_name")
	private String merchantName;

	/**
	 * 商户所属渠道
	 */
	@NotBlank(message = "请选择所属渠道")
	@Column(name = "channel_id")
	private String channelId;

	/**
	 * 商户所属渠道
	 */
	@NotBlank(message = "请选择所属渠道")
	@Column(name = "channel_name")
	private String channelName;

	/**
	 * 新零售sessionKey
	 */
	@Column(name = "sell_session_key")
	private String sellSessionKey;

	/**
	 * 微信公众号二维码
	 */
	@Column(name = "wechat_qrcode_url")
	private String wechatQrcodeUrl;

	/**
	 * 商户可用状态0:可用，1:不可用
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * @return Id
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
	 * 获取商户号
	 *
	 * @return merchant_code - 商户号
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * 设置商户号
	 *
	 * @param merchantCode
	 *            商户号
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * 获取商户名称
	 *
	 * @return merchant_name - 商户名称
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * 设置商户名称
	 *
	 * @param merchantName
	 *            商户名称
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOriginFlag() {
		return originFlag;
	}

	public void setOriginFlag(String originFlag) {
		this.originFlag = originFlag;
	}

	/**
	 * 获取商户所属渠道
	 *
	 * @return channel_id - 商户所属渠道
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * 设置商户所属渠道
	 *
	 * @param channelId
	 *            商户所属渠道
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSellSessionKey() {
		return sellSessionKey;
	}

	public void setSellSessionKey(String sellSessionKey) {
		this.sellSessionKey = sellSessionKey;
	}

	/**
	 * 获取商户可用状态0:可用，1:不可用
	 *
	 * @return is_delete - 商户可用状态0:可用，1:不可用
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置商户可用状态0:可用，1:不可用
	 *
	 * @param isDelete
	 *            商户可用状态0:可用，1:不可用
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取创建人
	 *
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId
	 *            创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getMerchantAccountId() {
		return merchantAccountId;
	}

	public void setMerchantAccountId(String merchantAccountId) {
		this.merchantAccountId = merchantAccountId;
	}

	public String getMerchantAccountName() {
		return merchantAccountName;
	}

	public void setMerchantAccountName(String merchantAccountName) {
		this.merchantAccountName = merchantAccountName;
	}

	public String getWechatQrcodeUrl() {
		return wechatQrcodeUrl;
	}

	public void setWechatQrcodeUrl(String wechatQrcodeUrl) {
		this.wechatQrcodeUrl = wechatQrcodeUrl;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}