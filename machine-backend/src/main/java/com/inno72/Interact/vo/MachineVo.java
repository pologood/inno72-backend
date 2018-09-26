package com.inno72.Interact.vo;

import java.util.List;

public class MachineVo {

	private String id;

	private String machineId;

	private String machineCode;

	private String localDesc;

	private List<MachineActivityVo> machineActivity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public List<MachineActivityVo> getMachineActivity() {
		return machineActivity;
	}

	public void setMachineActivity(List<MachineActivityVo> machineActivity) {
		this.machineActivity = machineActivity;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MachineVo) {
			MachineVo result = (MachineVo) obj;
			if (this.getMachineId().equals(result.getMachineId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}