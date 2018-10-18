package com.inno72.Interact.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

public class MachineVo {

	private String id;

	private String machineId;

	private String machineCode;

	private String localDesc;

	private Integer state;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime queryStartTime;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime queryEndTime;

	private List<MachineActivityVo> machineActivity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLocalDesc() {
		return localDesc;
	}

	public void setLocalDesc(String localDesc) {
		this.localDesc = localDesc;
	}

	public List<MachineActivityVo> getMachineActivity() {
		return machineActivity;
	}

	public LocalDateTime getQueryStartTime() {
		return queryStartTime;
	}

	public void setQueryStartTime(LocalDateTime queryStartTime) {
		this.queryStartTime = queryStartTime;
	}

	public LocalDateTime getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(LocalDateTime queryEndTime) {
		this.queryEndTime = queryEndTime;
	}

	public void setMachineActivity(List<MachineActivityVo> machineActivity) {
		this.machineActivity = machineActivity;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MachineVo) {
			MachineVo result = (MachineVo) obj;
			if (this.getMachineId().equals(result.getMachineId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}