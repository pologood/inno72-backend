package com.inno72.machine.vo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

public class MachineListVo {
	private String id;
	private String machineCode;
	private String localDesc;
	private Integer netStatus;
	private String activityName;
	private String channelStatus;
	private String goodsStatus;
	private String machineStatus;
	private String detail;
	private Integer machineType;
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime insideTime;

	private List<Map<String, Object>> planTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(Integer netStatus) {
		this.netStatus = netStatus;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getMachineStatus() {
		return machineStatus;
	}

	public void setMachineStatus(String machineStatus) {
		this.machineStatus = machineStatus;
	}

	public List<Map<String, Object>> getPlanTime() {
		return planTime;
	}

	public void setPlanTime(List<Map<String, Object>> planTime) {
		this.planTime = planTime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getMachineType() {
		return machineType;
	}

	public void setMachineType(Integer machineType) {
		this.machineType = machineType;
	}

	public LocalDateTime getInsideTime() {
		return insideTime;
	}

	public void setInsideTime(LocalDateTime insideTime) {
		this.insideTime = insideTime;
	}

}
