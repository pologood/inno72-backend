package com.inno72.project.vo;

public class Inno72MachineVo {

	private String machineId;

	private String machineCode;

	private String state;

	private String province;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Inno72MachineVo) {
			Inno72MachineVo result = (Inno72MachineVo) obj;
			if (this.machineId.equals(result.getMachineId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}