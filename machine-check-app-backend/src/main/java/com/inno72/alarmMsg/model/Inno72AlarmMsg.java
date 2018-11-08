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

    /**
     * 类型
     */
    @Column(name = "type")
    private int type;

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

    @Transient
    private Integer isRead;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public List<Inno72CheckUserMachine> getInno72CheckUserMachineList() {
		return inno72CheckUserMachineList;
	}

	public void setInno72CheckUserMachineList(List<Inno72CheckUserMachine> inno72CheckUserMachineList) {
		this.inno72CheckUserMachineList = inno72CheckUserMachineList;
	}
}
