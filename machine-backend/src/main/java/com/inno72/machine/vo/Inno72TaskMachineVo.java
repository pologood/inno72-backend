package com.inno72.machine.vo;

import com.inno72.machine.model.Inno72TaskMachine;

public class Inno72TaskMachineVo extends Inno72TaskMachine {

	private String name;

	private Integer planed;

	private String province;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPlaned() {
		return planed;
	}

	public void setPlaned(Integer planed) {
		this.planed = planed;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}