package com.inno72.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

/**
 * @Auther: wxt
 * @Date: 2018/7/4 16:19
 * @Description:机器状态
 */
public class MachineStatus {

    private String machineId;
    private int machineDoorStatus;
    private int dropGoodsSwitch;
    private String goodsChannelStatus;
    private String temperature;
    private int temperatureSwitchStatus;
    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getMachineDoorStatus() {
        return machineDoorStatus;
    }

    public void setMachineDoorStatus(int machineDoorStatus) {
        this.machineDoorStatus = machineDoorStatus;
    }

    public int getDropGoodsSwitch() {
        return dropGoodsSwitch;
    }

    public void setDropGoodsSwitch(int dropGoodsSwitch) {
        this.dropGoodsSwitch = dropGoodsSwitch;
    }

    public String getGoodsChannelStatus() {
        return goodsChannelStatus;
    }

    public void setGoodsChannelStatus(String goodsChannelStatus) {
        this.goodsChannelStatus = goodsChannelStatus;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getTemperatureSwitchStatus() {
        return temperatureSwitchStatus;
    }

    public void setTemperatureSwitchStatus(int temperatureSwitchStatus) {
        this.temperatureSwitchStatus = temperatureSwitchStatus;
    }
}

