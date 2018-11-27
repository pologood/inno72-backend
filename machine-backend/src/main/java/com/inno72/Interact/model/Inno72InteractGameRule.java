package com.inno72.Interact.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_interact_game_rule")
public class Inno72InteractGameRule {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 互动ID
	 */
	@Column(name = "interact_id")
	private String interactId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 同一用户每天获得数量
	 */
	@Column(name = "rule_code")
	private Integer ruleCode;

	/**
	 * 商品关联的优惠券
	 */
	@Column(name = "rule_remark")
	private String ruleRemark;

	/**
	 * 类型:0商品，1优惠券
	 */
	private Integer type;

	/**
	 * 获取ID
	 *
	 * @return id - ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 *
	 * @param id
	 *            ID
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
	 * 获取商品ID
	 *
	 * @return goods_id - 商品ID
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * 设置商品ID
	 *
	 * @param goodsId
	 *            商品ID
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * 获取同一用户每天获得数量
	 *
	 * @return rule_code - 同一用户每天获得数量
	 */
	public Integer getRuleCode() {
		return ruleCode;
	}

	/**
	 * 设置同一用户每天获得数量
	 *
	 * @param ruleCode
	 *            同一用户每天获得数量
	 */
	public void setRuleCode(Integer ruleCode) {
		this.ruleCode = ruleCode;
	}

	/**
	 * 获取商品关联的优惠券
	 *
	 * @return rule_remark - 商品关联的优惠券
	 */
	public String getRuleRemark() {
		return ruleRemark;
	}

	/**
	 * 设置商品关联的优惠券
	 *
	 * @param ruleRemark
	 *            商品关联的优惠券
	 */
	public void setRuleRemark(String ruleRemark) {
		this.ruleRemark = ruleRemark;
	}

	/**
	 * 获取类型:0商品，1优惠券
	 *
	 * @return type - 类型:0商品，1优惠券
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置类型:0商品，1优惠券
	 *
	 * @param type
	 *            类型:0商品，1优惠券
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Inno72InteractGameRule) {
			Inno72InteractGameRule result = (Inno72InteractGameRule) obj;
			if (this.ruleCode.equals(result.getRuleCode()) && this.goodsId.equals(result.getGoodsId())) {
				return true;
			}
		}
		return false;
	}
}