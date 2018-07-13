package com.inno72.machine.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class MachineAppStatus {

	private String machineId;
	private List<AppStatus> status;
	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createTime;

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public List<AppStatus> getStatus() {
		return status;
	}

	public void setStatus(List<AppStatus> status) {
		this.status = status;
	}

}
