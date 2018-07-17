package com.inno72.check.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class Inno72CheckFault {

    /**
     * uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 机器ID
     */
    @Column(name="machine_id")
    private String machineId;

    /**
     * 类型
     */
    @Column(name="type")
    private String type;

    /**
     * 备注
     */
    @Column(name="remark")
    private String remark;

    /**
     * 提交人
     */
    @Column(name="submit_user")
    private String submitUser;

    /**
     * 解决人
     */
    @Column(name="finish_user")
    private String finishUser;

    /**
     * 提交时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="submit_time")
    private LocalDateTime submitTime;

    /**
     * 解决时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="finish_time")
    private LocalDateTime finishTime;

    /**
     * 状态
     */
    @Column(name="status")
    private int status;

    /**
     * 解决方案
     */
    @Column(name="finish_remark")
    private String finishRemark;

    /**
     * 提醒状态（0.未提醒，1.已提醒）
     */
    @Column(name="remind_status")
    private int remindStatus;

    @Transient
    private List<Inno72CheckFaultImage> imageList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }

    public String getFinishUser() {
        return finishUser;
    }

    public void setFinishUser(String finishUser) {
        this.finishUser = finishUser;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFinishRemark() {
        return finishRemark;
    }

    public void setFinishRemark(String finishRemark) {
        this.finishRemark = finishRemark;
    }

    public int getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(int remindStatus) {
        this.remindStatus = remindStatus;
    }

    public List<Inno72CheckFaultImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<Inno72CheckFaultImage> imageList) {
        this.imageList = imageList;
    }
}
