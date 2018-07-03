package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_merchant")
public class Inno72Merchant {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 商户号
     */
    @Column(name = "merchant_id")
    private String merchantId;

    /**
     * 商户名称
     */
    @Column(name = "merchant_name")
    private String merchantName;

    /**
     * 商户所属渠道
     */
    @Column(name = "channel_id")
    private String channelId;
    
    /**
     * 商户状态
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return Id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取商户号
     *
     * @return merchant_id - 商户号
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * 设置商户号
     *
     * @param merchantId 商户号
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 获取商户名称
     *
     * @return merchant_name - 商户名称
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * 设置商户名称
     *
     * @param merchantName 商户名称
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * 获取商户所属渠道
     *
     * @return channel_id - 商户所属渠道
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * 设置商户所属渠道
     *
     * @param channelId 商户所属渠道
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    
    /**
     * 获取商户状态
     *
     * @return status - 商户状态
     */
    public int getStatus() {
		return status;
	}
    /**
     * 设置商户状态
     *
     * @param status 商户状态
     */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
     * 获取创建人
     *
     * @return create_id - 创建人
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取更新人
     *
     * @return update_id - 更新人
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
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

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}