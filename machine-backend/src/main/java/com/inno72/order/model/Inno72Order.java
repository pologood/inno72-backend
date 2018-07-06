package com.inno72.order.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Inno72Order {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 订单号
     */
    @Column(name="order_num")
    private String orderNum;

    /**
     * 用户ID
     */
    @Column(name="user_id")
    private String userId;

    /**
     * 渠道ID
     */
    @Column(name="channel_id")
    private String channelId;

    /**
     * 机器ID
     */
    @Column(name="machine_id")
    private String machineId;

    /**
     * 游戏ID
     */
    @Column(name="game_id")
    private String gameId;

    /**
     * 下单时间
     */
    @Column(name="order_time")
    private Date orderTime;

    /**
     * 订单价格
     */
    @Column(name="order_price")
    private BigDecimal orderPrice;

    /**
     * 订单类型
     */
    @Column(name="order_type")
    private String orderType;

    /**
     * 支付状态
     */
    @Column(name="pay_status")
    private String payStatus;

    /**
     * 支付时间
     */
    @Column(name="pay_time")
    private Date payTime;

    /**
     * 活动ID
     */
    @Column(name="activity_id")
    private String activityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
