package com.inno72.model;

public class MachineInstallAppBean {
	private String appPackageName;
	private int versionCode;
	private String url;
	private int seq;

	public String getAppPackageName() {
		return appPackageName;
	}

	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
