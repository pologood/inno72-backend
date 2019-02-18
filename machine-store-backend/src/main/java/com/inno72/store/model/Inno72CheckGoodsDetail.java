package com.inno72.store.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_check_goods_detail")
public class Inno72CheckGoodsDetail {
    /**
     * PK
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 主表ID
     */
    @Column(name = "goods_num_id")
    private String goodsNumId;

    /**
     * 实收数量
     */
    @Column(name = "receive_count")
    private Integer receiveCount;

    /**
     * 实补数量
     */
    @Column(name = "supply_count")
    private Integer supplyCount;

    /**
     * 差异数量
     */
    @Column(name = "differ_count")
    private Integer differCount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取主表ID
     *
     * @return goods_num_id - 主表ID
     */
    public String getGoodsNumId() {
        return goodsNumId;
    }

    /**
     * 设置主表ID
     *
     * @param goodsNumId 主表ID
     */
    public void setGoodsNumId(String goodsNumId) {
        this.goodsNumId = goodsNumId;
    }

    /**
     * 获取实收数量
     *
     * @return receive_count - 实收数量
     */
    public Integer getReceiveCount() {
        return receiveCount;
    }

    /**
     * 设置实收数量
     *
     * @param receiveCount 实收数量
     */
    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    /**
     * 获取实补数量
     *
     * @return supply_count - 实补数量
     */
    public Integer getSupplyCount() {
        return supplyCount;
    }

    /**
     * 设置实补数量
     *
     * @param supplyCount 实补数量
     */
    public void setSupplyCount(Integer supplyCount) {
        this.supplyCount = supplyCount;
    }

    /**
     * 获取差异数量
     *
     * @return differ_count - 差异数量
     */
    public Integer getDifferCount() {
        return differCount;
    }

    /**
     * 设置差异数量
     *
     * @param differCount 差异数量
     */
    public void setDifferCount(Integer differCount) {
        this.differCount = differCount;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}