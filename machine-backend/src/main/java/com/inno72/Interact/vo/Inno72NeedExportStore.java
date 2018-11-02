package com.inno72.Interact.vo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Inno72NeedExportStore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String activityId;

	/**
	 * 0普通活动，1派样
	 */
	private Integer activityType;

	private String machineCode;

	private String sellerId;

	private String province;

	private String city;

	private String locale;

	private String phone;

	private String shopName;

	private String img = "https://img.alicdn.com/top/i1/TB1Xb0QXjfguuRjy1zewu20KFXa.png";

	private String date = "09:00-22:00";

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
	 * @return activity_id
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * 获取0普通活动，1派样
	 *
	 * @return activity_type - 0普通活动，1派样
	 */
	public Integer getActivityType() {
		return activityType;
	}

	/**
	 * 设置0普通活动，1派样
	 *
	 * @param activityType
	 *            0普通活动，1派样
	 */
	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	/**
	 * @return machine_code
	 */
	public String getMachineCode() {
		return machineCode;
	}

	/**
	 * @param machineCode
	 */
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	/**
	 * @return seller_id
	 */
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * @param sellerId
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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
	 * @return locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return create_time
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}