package com.inno72.machine.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_task")
public class Inno72Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "code")
	private String code;

	/**
	 * 任务类型：1升级 2卸载 3 合并 4 拆分
	 */
	private Integer type;

	/**
	 * 任务状态：0未执行 1待执行 2已执行 3继续执行
	 */
	private Integer status;

	/**
	 * 强制升级：0 强制， 1不强制
	 */
	@Column(name = "is_force")
	private Integer isForce;

	/**
	 * 执行类型：1 socket 2 push
	 */
	@Column(name = "do_type")
	private Integer doType;

	/**
	 * 执行时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "do_time")
	private LocalDateTime doTime;

	/**
	 * 操作app_id
	 */
	@Column(name = "app_id")
	private String appId;

	/**
	 * 操作app
	 */
	private String app;

	/**
	 * app地址
	 */
	@Column(name = "app_url")
	private String appUrl;

	/**
	 * app版本号
	 */
	@Column(name = "app_version")
	private String appVersion;

	/**
	 * 货道操作信息
	 */
	@Column(name = "channel_code")
	private String channelCode;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取任务类型：1升级 2卸载 3 合并 4 拆分
	 *
	 * @return type - 任务类型：1升级 2卸载 3 合并 4 拆分
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置任务类型：1升级 2卸载 3 合并 4 拆分
	 *
	 * @param type
	 *            任务类型：1升级 2卸载 3 合并 4 拆分
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取任务状态：0未执行 1待执行 2已执行 3继续执行
	 *
	 * @return status - 任务状态：0未执行 1待执行 2已执行 3继续执行
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置任务状态：0未执行 1待执行 2已执行 3继续执行
	 *
	 * @param status
	 *            任务状态：0未执行 1待执行 2已执行 3继续执行
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsForce() {
		return isForce;
	}

	public void setIsForce(Integer isForce) {
		this.isForce = isForce;
	}

	/**
	 * 获取执行类型：1 socket 2 push
	 *
	 * @return do_type - 执行类型：1 socket 2 push
	 */
	public Integer getDoType() {
		return doType;
	}

	/**
	 * 设置执行类型：1 socket 2 push
	 *
	 * @param doType
	 *            执行类型：1 socket 2 push
	 */
	public void setDoType(Integer doType) {
		this.doType = doType;
	}

	/**
	 * 获取执行时间
	 *
	 * @return do_time - 执行时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = LocalDateTimeConverter.class)
	public LocalDateTime getDoTime() {
		return doTime;
	}

	/**
	 * 设置执行时间
	 *
	 * @param doTime
	 *            执行时间
	 */
	public void setDoTime(LocalDateTime doTime) {
		this.doTime = doTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * 获取操作app
	 *
	 * @return app - 操作app
	 */
	public String getApp() {
		return app;
	}

	/**
	 * 设置操作app
	 *
	 * @param app
	 *            操作app
	 */
	public void setApp(String app) {
		this.app = app;
	}

	/**
	 * 获取app地址
	 *
	 * @return app_url - app地址
	 */
	public String getAppUrl() {
		return appUrl;
	}

	/**
	 * 设置app地址
	 *
	 * @param appUrl
	 *            app地址
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	/**
	 * 获取app版本号
	 *
	 * @return app_version - app版本号
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * 设置app版本号
	 *
	 * @param appVersion
	 *            app版本号
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * 获取货道操作信息
	 *
	 * @return channel_code - 货道操作信息
	 */
	public String getChannelCode() {
		return channelCode;
	}

	/**
	 * 设置货道操作信息
	 *
	 * @param channelCode
	 *            货道操作信息
	 */
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	/**
	 * 获取创建人
	 *
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId
	 *            创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime
	 *            更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
}