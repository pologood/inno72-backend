package com.inno72.model;

import java.util.Date;

public class AlarmMachineBean {

    private String id;

    private String machineId;

    private String machineCode;

    private String monitorStart;

    private String monitorEnd;

    private String connectId;

    private Date connectTime;

    private String heartId;

    private Date heartTime;

    private Date createTime;

    private Date updateTime;

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

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMonitorStart() {
        return monitorStart;
    }

    public void setMonitorStart(String monitorStart) {
        this.monitorStart = monitorStart;
    }

    public String getMonitorEnd() {
        return monitorEnd;
    }

    public void setMonitorEnd(String monitorEnd) {
        this.monitorEnd = monitorEnd;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public Date getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Date connectTime) {
        this.connectTime = connectTime;
    }

    public String getHeartId() {
        return heartId;
    }

    public void setHeartId(String heartId) {
        this.heartId = heartId;
    }

    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
