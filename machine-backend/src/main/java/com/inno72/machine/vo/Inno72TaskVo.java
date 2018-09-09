package com.inno72.machine.vo;

import java.util.List;

import com.inno72.machine.model.Inno72Task;

public class Inno72TaskVo extends Inno72Task {

	private int taskAll;

	private int taskSuss;

	private String doTimeStr;

	private String channelCodeStr;

	private String appName;

	private String creater;

	private String remark;

	private String result;

	private List<Inno72TaskMachineVo> machineList;

	public int getTaskAll() {
		return taskAll;
	}

	public void setTaskAll(int taskAll) {
		this.taskAll = taskAll;
	}

	public int getTaskSuss() {
		return taskSuss;
	}

	public void setTaskSuss(int taskSuss) {
		this.taskSuss = taskSuss;
	}

	public List<Inno72TaskMachineVo> getMachineList() {
		return machineList;
	}

	public void setMachineList(List<Inno72TaskMachineVo> machineList) {
		this.machineList = machineList;
	}

	public String getChannelCodeStr() {
		return channelCodeStr;
	}

	public void setChannelCodeStr(String channelCodeStr) {
		this.channelCodeStr = channelCodeStr;
	}

	public String getDoTimeStr() {
		return doTimeStr;
	}

	public void setDoTimeStr(String doTimeStr) {
		this.doTimeStr = doTimeStr;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}