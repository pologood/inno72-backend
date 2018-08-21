package com.inno72.machine.vo;

import java.util.List;

import com.inno72.machine.model.Inno72AppScreenShot;

public class MachineStatusVo {
	private MachineStatus machineStatus;
	private SystemStatus systemStatus;

	private List<Inno72AppScreenShot> imgs;

	public MachineStatus getMachineStatus() {
		return machineStatus;
	}

	public void setMachineStatus(MachineStatus machineStatus) {
		this.machineStatus = machineStatus;
	}

	public SystemStatus getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(SystemStatus systemStatus) {
		this.systemStatus = systemStatus;
	}

	public List<Inno72AppScreenShot> getImgs() {
		return imgs;
	}

	public void setImgs(List<Inno72AppScreenShot> imgs) {
		this.imgs = imgs;
	}

}
