package com.inno72.machine.vo;

public class MachineExceptionVo {
	private String id;
	private String machineCode;
	private String local;
	// 离线列表字段
	private String offlineTime;
	// 异常列表字段
	private Integer machineDoorStatus;
	private Integer dropGoodsSwitch;
	private Integer temperature;
	private Integer screenIntensity;
	private String goodsChannelStatus;
	private Integer voice;
	private String updateTime;

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

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime;
	}

	public Integer getMachineDoorStatus() {
		return machineDoorStatus;
	}

	public void setMachineDoorStatus(Integer machineDoorStatus) {
		this.machineDoorStatus = machineDoorStatus;
	}

	public Integer getDropGoodsSwitch() {
		return dropGoodsSwitch;
	}

	public void setDropGoodsSwitch(Integer dropGoodsSwitch) {
		this.dropGoodsSwitch = dropGoodsSwitch;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getScreenIntensity() {
		return screenIntensity;
	}

	public void setScreenIntensity(Integer screenIntensity) {
		this.screenIntensity = screenIntensity;
	}

	public String getGoodsChannelStatus() {
		return goodsChannelStatus;
	}

	public void setGoodsChannelStatus(String goodsChannelStatus) {
		this.goodsChannelStatus = goodsChannelStatus;
	}

	public Integer getVoice() {
		return voice;
	}

	public void setVoice(Integer voice) {
		this.voice = voice;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
