package com.inno72.check.vo;

import java.util.List;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.project.vo.Inno72MachineVo;

public class Inno72CheckUserVo extends Inno72CheckUser{
	
	 private List<Inno72MachineVo> machines;

	public List<Inno72MachineVo> getMachines() {
		return machines;
	}

	public void setMachines(List<Inno72MachineVo> machines) {
		this.machines = machines;
	}
    
	 
	 
	 
}