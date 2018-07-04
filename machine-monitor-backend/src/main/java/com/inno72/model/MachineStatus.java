package com.inno72.model;

/**
 * @Auther: wxt
 * @Date: 2018/7/4 16:19
 * @Description:机器状态
 */
public class MachineStatus {

    private String machine_id;
    private int machine_door_status;
    private int drop_goods_switch;
    private String goods_channel_status;
    private String temperature;
    private int temperature_switch_status;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public int getMachine_door_status() {
        return machine_door_status;
    }

    public void setMachine_door_status(int machine_door_status) {
        this.machine_door_status = machine_door_status;
    }

    public int getDrop_goods_switch() {
        return drop_goods_switch;
    }

    public void setDrop_goods_switch(int drop_goods_switch) {
        this.drop_goods_switch = drop_goods_switch;
    }

    public String getGoods_channel_status() {
        return goods_channel_status;
    }

    public void setGoods_channel_status(String goods_channel_status) {
        this.goods_channel_status = goods_channel_status;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getTemperature_switch_status() {
        return temperature_switch_status;
    }

    public void setTemperature_switch_status(int temperature_switch_status) {
        this.temperature_switch_status = temperature_switch_status;
    }







}

