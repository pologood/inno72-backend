package com.inno72.system.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;

@Table(name = "inno72_user_function_area")
public class Inno72UserFunctionArea {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * 区域code
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 区域名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 省名称
	 */
	@Column(name = "province")
	private String province;

	/**
	 * 市名称
	 */
	@Column(name = "city")
	private String city;

	/**
	 * 区名称
	 */
	@Column(name = "district")
	private String district;

	/**
	 * 市名称
	 */
	@Column(name = "level")
	private Integer level;

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
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

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
	 * 获取用户ID
	 *
	 * @return user_id - 用户ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 *
	 * @param userId
	 *            用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取区域code
	 *
	 * @return area_code - 区域code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置区域code
	 *
	 * @param areaCode
	 *            区域code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
}