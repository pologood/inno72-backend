package com.inno72.store.model;

import javax.persistence.*;

@Table(name = "inno72_check_goods_num")
public class Inno72CheckGoodsNum {
    /**
     * PK
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 巡检ID
     */
    @Column(name = "check_user_id")
    private String checkUserId;

    /**
     * 活动ID
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     * 收到商品总数
     */
    @Column(name = "receive_total_count")
    private Integer receiveTotalCount;

    /**
     * 补货总数量
     */
    @Column(name = "supply_total_count")
    private Integer supplyTotalCount;

    /**
     * 差异总数量
     */
    @Column(name = "differ_total_count")
    private Integer differTotalCount;

    /**
     * 获取PK
     *
     * @return id - PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置PK
     *
     * @param id PK
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取商品ID
     *
     * @return goods_id - 商品ID
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品ID
     *
     * @param goodsId 商品ID
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取巡检ID
     *
     * @return check_user_id - 巡检ID
     */
    public String getCheckUserId() {
        return checkUserId;
    }

    /**
     * 设置巡检ID
     *
     * @param checkUserId 巡检ID
     */
    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    /**
     * 获取活动ID
     *
     * @return activity_id - 活动ID
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * 设置活动ID
     *
     * @param activityId 活动ID
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /**
     * 获取收到商品总数
     *
     * @return receive_total_count - 收到商品总数
     */
    public Integer getReceiveTotalCount() {
        return receiveTotalCount;
    }

    /**
     * 设置收到商品总数
     *
     * @param receiveTotalCount 收到商品总数
     */
    public void setReceiveTotalCount(Integer receiveTotalCount) {
        this.receiveTotalCount = receiveTotalCount;
    }

    /**
     * 获取补货总数量
     *
     * @return supply_total_count - 补货总数量
     */
    public Integer getSupplyTotalCount() {
        return supplyTotalCount;
    }

    /**
     * 设置补货总数量
     *
     * @param supplyTotalCount 补货总数量
     */
    public void setSupplyTotalCount(Integer supplyTotalCount) {
        this.supplyTotalCount = supplyTotalCount;
    }

    /**
     * 获取差异总数量
     *
     * @return differ_total_count - 差异总数量
     */
    public Integer getDifferTotalCount() {
        return differTotalCount;
    }

    /**
     * 设置差异总数量
     *
     * @param differTotalCount 差异总数量
     */
    public void setDifferTotalCount(Integer differTotalCount) {
        this.differTotalCount = differTotalCount;
    }
}