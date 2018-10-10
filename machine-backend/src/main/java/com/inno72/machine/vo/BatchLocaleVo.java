package com.inno72.machine.vo;

import java.util.List;

public class BatchLocaleVo {

	private List<String> ids;

	/**
	 * 监控：0开启，1不开启
	 */
	private Integer monitor;

	/**
	 * 监控开始时间
	 */
	private String monitorStart;

	/**
	 * 监控结束时间
	 */
	private String monitorEnd;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public Integer getMonitor() {
		return monitor;
	}

	public void setMonitor(Integer monitor) {
		this.monitor = monitor;
	}

	public String getMonitorStart() {
		return monitorStart;
	}

	public void setMonitorStart(String monitorStart) {
		this.monitorStart = monitorStart;
	}

	public String getMonitorEnd() {
		return monitorEnd;
	}

	public void setMonitorEnd(String monitorEnd) {
		this.monitorEnd = monitorEnd;
	}

}