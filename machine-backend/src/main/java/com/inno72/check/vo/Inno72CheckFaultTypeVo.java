package com.inno72.check.vo;


import java.util.List;


import com.inno72.check.model.Inno72CheckFaultType;

public class Inno72CheckFaultTypeVo extends Inno72CheckFaultType{
	
	List<Inno72CheckFaultType> solutions ;

	public List<Inno72CheckFaultType> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Inno72CheckFaultType> solutions) {
		this.solutions = solutions;
	}
	
	
    
}