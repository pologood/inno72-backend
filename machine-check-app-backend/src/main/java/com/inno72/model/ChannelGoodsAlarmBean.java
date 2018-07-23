package com.inno72.model;

/**
 * @Auther: wxt
 * @Date: 2018/7/22 17:53
 * @Description:
 */
public class ChannelGoodsAlarmBean {

    private String goodsName;
    private String machineCode;
    private int lackGoodsType;//1.缺货10% 2.缺货20%
    private String channelNum;

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

    public int getLackGoodsType() {
        return lackGoodsType;
    }

    public void setLackGoodsType(int lackGoodsType) {
        this.lackGoodsType = lackGoodsType;
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum;
    }
}

