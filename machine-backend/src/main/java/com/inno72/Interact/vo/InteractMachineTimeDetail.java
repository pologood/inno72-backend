package com.inno72.Interact.vo;

import java.util.List;
import java.util.Map;

public class InteractMachineTimeDetail {

	private String interactId;

	private String queryStartTime;

	private String queryEndTime;

	private String machineId;

	private String machineCode;

	private Integer state;

	private List<Map<String, String>> planTime;

	public String getInteractId() {
		return interactId;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
	}

	public String getQueryStartTime() {
		return queryStartTime;
	}

	public void setQueryStartTime(String queryStartTime) {
		this.queryStartTime = queryStartTime;
	}

	public String getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(String queryEndTime) {
		this.queryEndTime = queryEndTime;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<Map<String, String>> getPlanTime() {
		return planTime;
	}

	public void setPlanTime(List<Map<String, String>> planTime) {
		this.planTime = planTime;
	}

}