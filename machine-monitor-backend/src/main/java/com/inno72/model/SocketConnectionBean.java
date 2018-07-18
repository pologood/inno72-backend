package com.inno72.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SocketConnectionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String machineCode;
	private String sessionId;
	private LocalDateTime connectTime;
	private LocalDateTime disConnectTime;
	private Integer status;// 1;正常连接 2；暂时断开

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public LocalDateTime getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(LocalDateTime connectTime) {
		this.connectTime = connectTime;
	}

	public LocalDateTime getDisConnectTime() {
		return disConnectTime;
	}

	public void setDisConnectTime(LocalDateTime disConnectTime) {
		this.disConnectTime = disConnectTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
