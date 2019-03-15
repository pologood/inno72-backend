package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

public class MachineEnterVo {

	private String machineId;

	private String machineCode;

	private String localDesc;

	private List<Map<String, Object>> enterList;

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

	public String getLocalDesc() {
		return localDesc;
	}

	public void setLocalDesc(String localDesc) {
		this.localDesc = localDesc;
	}

	public List<Map<String, Object>> getEnterList() {
		return enterList;
	}

	public void setEnterList(List<Map<String, Object>> enterList) {
		this.enterList = enterList;
	}

}