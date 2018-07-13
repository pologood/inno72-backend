package com.inno72.order.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
     * 店铺ID
     */
    @Column(name="shops_id")
    private String shopsId;

    /**
     * 店铺名称
     */
    @Column(name="shops_name")
    private String shopsName;

    @Column(name="merchant_id")
    private String merchantId;

    @Column(name="inno72_activity_id")
    private String inno72ActivityId;

    @Column(name="inno72_activity_plan_id")
    private String inno72ActivityPlanId;


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


    @Column(name="pay_price")
    private BigDecimal payPrice;
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

    @Column(name="goods_status")
    private int goodsStatus;

    /**
     * 支付时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="pay_time")
    private LocalDateTime payTime;


    @Column(name="ref_order_status")
    private String refOrderStatus;

    @Column(name="ref_order_id")
    private String refOrderId;


    @Column(name="repetition")
    private int repetition;


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
    private String keyword;

    /**
     * 游戏ID
     */
    @Column(name="game_id")
    private String gameId;

    private List<Inno72OrderGoods> orderGoodsList;


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

    public String getShopsId() {
        return shopsId;
    }

    public void setShopsId(String shopsId) {
        this.shopsId = shopsId;
    }

    public String getShopsName() {
        return shopsName;
    }

    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getInno72ActivityId() {
        return inno72ActivityId;
    }

    public void setInno72ActivityId(String inno72ActivityId) {
        this.inno72ActivityId = inno72ActivityId;
    }

    public String getInno72ActivityPlanId() {
        return inno72ActivityPlanId;
    }

    public void setInno72ActivityPlanId(String inno72ActivityPlanId) {
        this.inno72ActivityPlanId = inno72ActivityPlanId;
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

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
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

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public String getRefOrderStatus() {
        return refOrderStatus;
    }

    public void setRefOrderStatus(String refOrderStatus) {
        this.refOrderStatus = refOrderStatus;
    }

    public String getRefOrderId() {
        return refOrderId;
    }

    public void setRefOrderId(String refOrderId) {
        this.refOrderId = refOrderId;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<Inno72OrderGoods> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<Inno72OrderGoods> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
