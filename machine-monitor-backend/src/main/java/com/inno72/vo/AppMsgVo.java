package com.inno72.vo;

public class AppMsgVo {
	private String targetCode;
	private String targetType;
	private Object data;
	private Integer isQueue;

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getIsQueue() {
		return isQueue;
	}

	public void setIsQueue(Integer isQueue) {
		this.isQueue = isQueue;
	}

}
