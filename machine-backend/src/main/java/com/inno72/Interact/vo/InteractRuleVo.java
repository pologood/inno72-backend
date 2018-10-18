package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

public class InteractRuleVo {

	/**
	 * 同一用户参与活动次数
	 */
	private String id;

	/**
	 * 同一用户参与活动次数
	 */
	private Integer times;

	/**
	 * 同一用户每一天参与活动次数
	 */
	private Integer dayTimes;

	/**
	 * 同一用户获得商品次数
	 */
	private Integer number;

	/**
	 * 同一用户每天获得商品次数
	 */
	private Integer dayNumber;

	private List<Map<String, Object>> goodsRule;

	private Integer type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getDayTimes() {
		return dayTimes;
	}

	public void setDayTimes(Integer dayTimes) {
		this.dayTimes = dayTimes;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	public List<Map<String, Object>> getGoodsRule() {
		return goodsRule;
	}

	public void setGoodsRule(List<Map<String, Object>> goodsRule) {
		this.goodsRule = goodsRule;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}