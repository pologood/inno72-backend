package com.inno72.Interact.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_interact_shops")
public class Inno72InteractShops {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 互动ID
	 */
	@Column(name = "interact_id")
	private String interactId;

	/**
	 * 店铺ID
	 */
	@Column(name = "shops_id")
	private String shopsId;

	/**
	 * 是否入会:0否，1是
	 */
	@Column(name = "is_vip")
	private Integer isVip;

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
	 * 获取互动ID
	 *
	 * @return interact_id - 互动ID
	 */
	public String getInteractId() {
		return interactId;
	}

	/**
	 * 设置互动ID
	 *
	 * @param interactId
	 *            互动ID
	 */
	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	/**
	 * 获取店铺ID
	 *
	 * @return shops_id - 店铺ID
	 */
	public String getShopsId() {
		return shopsId;
	}

	/**
	 * 设置店铺ID
	 *
	 * @param shopsId
	 *            店铺ID
	 */
	public void setShopsId(String shopsId) {
		this.shopsId = shopsId;
	}

	/**
	 * 获取是否入会:0否，1是
	 *
	 * @return is_vip - 是否入会:0否，1是
	 */
	public Integer getIsVip() {
		return isVip;
	}

	/**
	 * 设置是否入会:0否，1是
	 *
	 * @param isVip
	 *            是否入会:0否，1是
	 */
	public void setIsVip(Integer isVip) {
		this.isVip = isVip;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Inno72InteractShops) {
			Inno72InteractShops other = (Inno72InteractShops) obj;
			if (shopsId.equals(other.shopsId) && interactId.equals(other.interactId))
				return true;
		}
		return false;
	}
}