package com.inno72.test.alipay;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_alipay_area")
public class Inno72AlipayArea {
	@Id
	private String code;

	@Column(name = "parent_code")
	private String parentCode;

	private String name;

	private String province;

	private String city;

	private String district;

	private Integer level;

	/**
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return parent_code
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
}