package com.inno72.check.vo;

import java.util.List;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.project.vo.Inno72MachineVo;

public class Inno72CheckUserVo extends Inno72CheckUser{
	
	 private String localeName;
	 
	 private String machineCode;
	
	 private List<Inno72MachineVo> machines;

	public List<Inno72MachineVo> getMachines() {
		return machines;
	}

	public void setMachines(List<Inno72MachineVo> machines) {
		this.machines = machines;
	}

	public String getLocaleName() {
		return localeName;
	}

	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
    
	
	
	 
	 
	 
}