package com.inno72.Interact.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_interact_merchant")
public class Inno72InteractMerchant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 互动ID
	 */
	@Column(name = "interact_id")
	private String interactId;

	/**
	 * 商户ID
	 */
	@Column(name = "merchant_id")
	private String merchantId;

	/**
	 * 是否关注：0不关注，1关注，2强制关注
	 */
	@Column(name = "is_focus")
	private Integer isFocus;

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
	 * 获取商户ID
	 *
	 * @return merchant_id - 商户ID
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * 设置商户ID
	 *
	 * @param merchantId
	 *            商户ID
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(Integer isFocus) {
		this.isFocus = isFocus;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Inno72InteractMerchant) {
			Inno72InteractMerchant other = (Inno72InteractMerchant) obj;
			if (merchantId.equals(other.merchantId) && interactId.equals(other.interactId))
				return true;
		}
		return false;
	}

}