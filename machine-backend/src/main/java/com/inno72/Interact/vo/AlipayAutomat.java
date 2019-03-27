package com.inno72.Interact.vo;

import java.util.Map;

public class AlipayAutomat {

	private String terminal_id;

	private String product_user_id;

	private String merchant_user_id;

	private String machine_type;

	private String machine_cooperation_type;

	private String machine_delivery_date;

	private String machine_name;

	private Map<String, String> delivery_address;

	private Map<String, String> point_position;

	private String merchant_user_type;

	private Map<String, String> associate;

	private Map<String, String> scene;

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getProduct_user_id() {
		return product_user_id;
	}

	public void setProduct_user_id(String product_user_id) {
		this.product_user_id = product_user_id;
	}

	public String getMerchant_user_id() {
		return merchant_user_id;
	}

	public void setMerchant_user_id(String merchant_user_id) {
		this.merchant_user_id = merchant_user_id;
	}

	public String getMachine_type() {
		return machine_type;
	}

	public void setMachine_type(String machine_type) {
		this.machine_type = machine_type;
	}

	public String getMachine_cooperation_type() {
		return machine_cooperation_type;
	}

	public void setMachine_cooperation_type(String machine_cooperation_type) {
		this.machine_cooperation_type = machine_cooperation_type;
	}

	public String getMachine_delivery_date() {
		return machine_delivery_date;
	}

	public void setMachine_delivery_date(String machine_delivery_date) {
		this.machine_delivery_date = machine_delivery_date;
	}

	public String getMachine_name() {
		return machine_name;
	}

	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}

	public Map<String, String> getDelivery_address() {
		return delivery_address;
	}

	public void setDelivery_address(Map<String, String> delivery_address) {
		this.delivery_address = delivery_address;
	}

	public Map<String, String> getPoint_position() {
		return point_position;
	}

	public void setPoint_position(Map<String, String> point_position) {
		this.point_position = point_position;
	}

	public String getMerchant_user_type() {
		return merchant_user_type;
	}

	public void setMerchant_user_type(String merchant_user_type) {
		this.merchant_user_type = merchant_user_type;
	}

	public Map<String, String> getAssociate() {
		return associate;
	}

	public void setAssociate(Map<String, String> associate) {
		this.associate = associate;
	}

	public Map<String, String> getScene() {
		return scene;
	}

	public void setScene(Map<String, String> scene) {
		this.scene = scene;
	}

}