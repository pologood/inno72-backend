package com.inno72.check.vo;

import java.util.List;

import com.inno72.check.model.Inno72CheckFault;

public class Inno72CheckFaultVo extends Inno72CheckFault {

	private String machineCode;

	private List<Inno72CheckFaultRemarkVo> answerList;

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public List<Inno72CheckFaultRemarkVo> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Inno72CheckFaultRemarkVo> answerList) {
		this.answerList = answerList;
	}

}