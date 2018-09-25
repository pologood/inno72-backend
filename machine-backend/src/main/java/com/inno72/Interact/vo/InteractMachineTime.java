package com.inno72.Interact.vo;

import java.util.List;

public class InteractMachineTime {

	private String interactId;

	private List<MachineTime> machines;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public List<MachineTime> getMachines() {
		return machines;
	}

	public void setMachines(List<MachineTime> machines) {
		this.machines = machines;
	}

}