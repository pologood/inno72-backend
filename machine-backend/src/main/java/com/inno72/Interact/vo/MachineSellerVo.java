package com.inno72.Interact.vo;

import java.io.Serializable;

/**
 *
 */
public class MachineSellerVo implements Serializable {
	private static final long serialVersionUID = 175479512480858039L;
	private String machineCode;
	private String sellerId;

	private String province;

	private String city;

	private String district;

	private String area;

	private String shopName;

	private String img = "https://img.alicdn.com/top/i1/TB1Xb0QXjfguuRjy1zewu20KFXa.png";

	private String date = "09:00-22:00";

	private String telNum;

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getDate() {
		return date;
	}

	public String getTelNum() {
		return this.getTel();
	}

	private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153"
			.split(",");

	private String getTel() {
		int index = getNum(0, telFirst.length - 1);
		String first = telFirst[index];
		String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
		String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
		return first + second + third;
	}

	public int getNum(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}

}
