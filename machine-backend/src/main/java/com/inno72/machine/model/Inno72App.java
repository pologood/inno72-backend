package com.inno72.machine.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_app")
public class Inno72App {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "app_package_name")
	private String appPackageName;

	/**
	 * 1：监控 2：正常
	 */
	@Column(name = "app_type")
	private Integer appType;

	@Column(name = "app_name")
	private String appName;

	@Column(name = "app_belong")
	private Integer appBelong;

	@Column(name = "download_url")
	private String downloadUrl;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return app_package_name
	 */
	public String getAppPackageName() {
		return appPackageName;
	}

	/**
	 * @param appPackageName
	 */
	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getAppBelong() {
		return appBelong;
	}

	public void setAppBelong(Integer appBelong) {
		this.appBelong = appBelong;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

}