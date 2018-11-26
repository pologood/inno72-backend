package com.inno72.project.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import lombok.Data;

@Table(name = "inno72_merchant_user")
@Data
public class Inno72MerchantUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT REPLACE(UUID(),'-','')")
	private String id;

	/**
	 * 商户表主键id
	 */
	@Column(name = "merchant_id")
	private String merchantId;

	/**
	 * 商户id
	 */
	@Column(name = "seller_id")
	private String sellerId;

	/**
	 * 登录名
	 */
	@Column(name = "login_name")
	private String loginName;

	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 商户名称
	 */
	@Column(name = "merchant_name")
	@NotNull(message = "请填写商户名称!")
	@Length(max = 50, message = "商户名称不成超过50个字!")
	private String merchantName;


	/**
	 * 行业
	 */
	@Column(name = "industry")
	private String industry;

	/**
	 * 行业
	 */
	@Column(name = "industry_code")
	private String industryCode;

	/**
	 * 验证手机号
	 */
	private String phone;

	/**
	 * 验证手机号
	 */
	@Column(name = "login_status")
	private String loginStatus;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Column(name = "creator")
	private String creator;

	/**
	 * 最后更新时间
	 */
	@Column(name = "last_update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime lastUpdateTime;

	/**
	 * 最后更新人
	 */
	@Column(name = "last_updator")
	private String lastUpdator;

}