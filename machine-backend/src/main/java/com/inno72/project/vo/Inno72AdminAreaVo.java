package com.inno72.project.vo;

import java.util.List;

import com.inno72.model.Inno72AdminArea;

public class Inno72AdminAreaVo extends Inno72AdminArea{
	
	private String canUseNum;
	
	private String totalNum;
	
    private  List<Inno72MachineVo>  machines ;

	public String getCanUseNum() {
		return canUseNum;
	}

	public void setCanUseNum(String canUseNum) {
		this.canUseNum = canUseNum;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public List<Inno72MachineVo> getMachines() {
		return machines;
	}

	public void setMachines(List<Inno72MachineVo> machines) {
		this.machines = machines;
	}

    

   
}