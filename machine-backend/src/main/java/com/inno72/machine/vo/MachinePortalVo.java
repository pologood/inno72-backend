package com.inno72.machine.vo;

public class MachinePortalVo {
	private Integer offline;// 离线机器
	private Integer channelException;// 货道故障
	private Integer dropGoodsSwitchException;// 掉货开关异常
	private Integer stockout;// 机器缺货
	private Integer waitOrder;// 待接单
	private Integer processed;// 处理中
	private Integer waitConfirm;// 待确认
	private Integer paiActivityCount;// 派样活动
	private Integer lockCount;// 锁定机器
	private Integer trafficCount;// 流量报警
	private Integer sdCount;// 内存报警

	public Integer getOffline() {
		return offline;
	}

	public void setOffline(Integer offline) {
		this.offline = offline;
	}

	public Integer getChannelException() {
		return channelException;
	}

	public void setChannelException(Integer channelException) {
		this.channelException = channelException;
	}

	public Integer getDropGoodsSwitchException() {
		return dropGoodsSwitchException;
	}

	public void setDropGoodsSwitchException(Integer dropGoodsSwitchException) {
		this.dropGoodsSwitchException = dropGoodsSwitchException;
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

	public Integer getPaiActivityCount() {
		return paiActivityCount;
	}

	public void setPaiActivityCount(Integer paiActivityCount) {
		this.paiActivityCount = paiActivityCount;
	}

	public Integer getLockCount() {
		return lockCount;
	}

	public void setLockCount(Integer lockCount) {
		this.lockCount = lockCount;
	}

	public Integer getTrafficCount() {
		return trafficCount;
	}

	public void setTrafficCount(Integer trafficCount) {
		this.trafficCount = trafficCount;
	}

	public Integer getSdCount() {
		return sdCount;
	}

	public void setSdCount(Integer sdCount) {
		this.sdCount = sdCount;
	}

}
