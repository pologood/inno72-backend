package com.inno72.model;

/**
 * @Auther: wxt
 * @Date: 2018/7/22 17:53
 * @Description:
 */
public class ChannelGoodsAlarmBean {

    private String goodsName;
    private String machineCode;
    private String channelNum;
    private int lackNum;//缺货多少个
    private int surPlusNum;//剩余多少个

    public int getLackNum() {
        return lackNum;
    }

    public void setLackNum(int lackNum) {
        this.lackNum = lackNum;
    }

    public int getSurPlusNum() {
        return surPlusNum;
    }

    public void setSurPlusNum(int surPlusNum) {
        this.surPlusNum = surPlusNum;
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }


}

