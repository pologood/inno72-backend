package com.inno72.project.vo;

import java.util.List;

import javax.persistence.*;

import com.inno72.model.Inno72AdminArea;

public class Inno72AdminAreaVo extends Inno72AdminArea{
    private  List<Inno72MachineVo>  machines ;

	public List<Inno72MachineVo> getMachines() {
		return machines;
	}

	public void setMachines(List<Inno72MachineVo> machines) {
		this.machines = machines;
	}

    

   
}