package com.inno72.order.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="order_time")
    private LocalDateTime orderTime;

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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="pay_time")
    private LocalDateTime payTime;

    /**
     * 活动ID
     */
    @Column(name="activity_id")
    private String activityId;

    @Transient
    private String channelCode;

    @Transient
    private String channelName;

    @Transient
    private String machineCode;

    @Transient
    private String machineName;

    @Transient
    private String gameName;

    @Transient
    private String gameRemark;


    @Transient
    private int pageNo;


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

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
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

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }


    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameRemark() {
        return gameRemark;
    }

    public void setGameRemark(String gameRemark) {
        this.gameRemark = gameRemark;
    }
}
