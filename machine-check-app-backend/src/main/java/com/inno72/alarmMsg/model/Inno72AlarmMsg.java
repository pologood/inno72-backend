package com.inno72.alarmMsg.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class Inno72AlarmMsg {
    /**
     * uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * title
     */
    @Column(name = "title")
    private String title;

    /**
     * 系统
     */
    @Column(name="system")
    private String system;

	@Column(name="main_type")
    private int mainType;

    /**
     * 类型
     */
    @Column(name = "child_type")
    private int childType;

    /**
     * 详情
     */
    @Column(name="detail")
    private String detail;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /**
     * 机器编号
     */
    @Column(name = "machine_code")
    private String machineCode;

	/**
	 * 系统
	 */
	@Column(name="read_user_id")
	private String readUserId;

	/**
	 * 类型
	 */
	@Column(name = "is_read")
	private Integer isRead;


	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	private LocalDateTime readTime;


	/**
	 * 业务ID
	 */
	@Column(name = "service_id")
	private String serviceId;

	private int [] mainTypes;

	@Transient
    private List<Inno72CheckUserMachine> inno72CheckUserMachineList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public int getMainType() {
		return mainType;
	}

	public void setMainType(int mainType) {
		this.mainType = mainType;
	}

	public int getChildType() {
		return childType;
	}

	public void setChildType(int childType) {
		this.childType = childType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getReadUserId() {
		return readUserId;
	}

	public void setReadUserId(String readUserId) {
		this.readUserId = readUserId;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int[] getMainTypes() {
		return mainTypes;
	}

	public void setMainTypes(int[] mainTypes) {
		this.mainTypes = mainTypes;
	}

	public List<Inno72CheckUserMachine> getInno72CheckUserMachineList() {
		return inno72CheckUserMachineList;
	}

	public void setInno72CheckUserMachineList(List<Inno72CheckUserMachine> inno72CheckUserMachineList) {
		this.inno72CheckUserMachineList = inno72CheckUserMachineList;
	}
}
