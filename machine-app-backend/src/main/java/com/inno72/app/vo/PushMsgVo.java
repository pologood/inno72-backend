package com.inno72.app.vo;

import java.util.Map;

public class PushMsgVo {
	private String alias;
	private String machineCode;
	private String title;
	private String text;
	private Integer pushType;
	private Map<String, Object> msgInfo;

	public enum Push_Type {
		// 安装app
		INSTALL_APP(1),
		// 发送日志
		SEND_LOG(2),
		// 发送图片
		SEND_IMG(3),
		// 发送adb
		SEND_ADB(4);

		private int v;

		private Push_Type(int v) {
			this.v = v;
		}

		public int v() {
			return this.v;
		}

		public static Push_Type get(int v) {
			for (Push_Type c : Push_Type.values()) {
				if (c.v == v) {
					return c;
				}
			}
			return null;
		}
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public Map<String, Object> getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(Map<String, Object> msgInfo) {
		this.msgInfo = msgInfo;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

}
