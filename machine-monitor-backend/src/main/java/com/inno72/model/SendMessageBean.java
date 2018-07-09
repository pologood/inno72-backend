package com.inno72.model;

public class SendMessageBean {

	private Integer eventType;
	private Integer subEventType;
	private String machineId;
	private Object data;

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Integer getSubEventType() {
		return subEventType;
	}

	public void setSubEventType(Integer subEventType) {
		this.subEventType = subEventType;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
