package com.inno72.common.util;

public enum LogType {
	CUT_APP("11"), DELETE_CHANNEL("12"), ENABLE_CHANNEL("13"), UPDATE_MACHINECODE("14"), MACHINE_INSTALL(
			"15"), TASK_INSTALL("16"), TASK_UNISTALL("17");
	private String code;

	private LogType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
