package com.inno72.goods.model;

import java.io.Serializable;

/**
 * @author liubin
 * @package com.inno72.installer.model
 * @date 2018/7/4 下午3:51
 * @email liubin@inno72.com
 */
public class GoodsChannelBean implements Serializable {
    //货道号
    private int goodsChannelNum;
    //货道状态
    private int goodsChannelStatus;
    //详细原因
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGoodsChannelNum() {
        return goodsChannelNum;
    }

    public void setGoodsChannelNum(int goodsChannelNum) {
        this.goodsChannelNum = goodsChannelNum;
    }

    public int getGoodsChannelStatus() {
        return goodsChannelStatus;
    }

    public void setGoodsChannelStatus(int goodsChannelStatus) {
        this.goodsChannelStatus = goodsChannelStatus;
    }
}
