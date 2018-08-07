package com.inno72.machine.vo;

public class MachinePortalVo {
	private Integer online; // 在线机器
	private Integer offline;// 离线机器
	private Integer exception;// 机器异常
	private Integer stockout;// 机器缺货
	private Integer waitOrder;// 待接单
	private Integer processed;// 处理中
	private Integer waitConfirm;// 待确认

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public Integer getOffline() {
		return offline;
	}

	public void setOffline(Integer offline) {
		this.offline = offline;
	}

	public Integer getException() {
		return exception;
	}

	public void setException(Integer exception) {
		this.exception = exception;
	}

	public Integer getStockout() {
		return stockout;
	}

	public void setStockout(Integer stockout) {
		this.stockout = stockout;
	}

	public Integer getWaitOrder() {
		return waitOrder;
	}

	public void setWaitOrder(Integer waitOrder) {
		this.waitOrder = waitOrder;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public Integer getWaitConfirm() {
		return waitConfirm;
	}

	public void setWaitConfirm(Integer waitConfirm) {
		this.waitConfirm = waitConfirm;
	}

}
