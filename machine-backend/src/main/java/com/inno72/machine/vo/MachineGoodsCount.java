package com.inno72.machine.vo;

public class MachineGoodsCount {
	private String id;

	/**
	 * 机器ID
	 */
	private String machineCode;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 地区
	 */
	private String localDesc;

	/**
	 * 补货前数量
	 */
	private Integer beforeNum;

	/**
	 * 补货数量
	 */
	private Integer afterNum;

	/**
	 * 剩余数量
	 */
	private Integer num;

	private String date;

	/**
	 * 获取uuid
	 *
	 * @return id - uuid
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置uuid
	 *
	 * @param id
	 *            uuid
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getLocalDesc() {
		return localDesc;
	}

	public void setLocalDesc(String localDesc) {
		this.localDesc = localDesc;
	}

	public Integer getBeforeNum() {
		return beforeNum;
	}

	public void setBeforeNum(Integer beforeNum) {
		this.beforeNum = beforeNum;
	}

	public Integer getAfterNum() {
		return afterNum;
	}

	public void setAfterNum(Integer afterNum) {
		this.afterNum = afterNum;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}