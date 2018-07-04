package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.validator.constraints.NotEmpty;

@Table(name = "inno72_merchant")
public class Inno72Merchant {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 商户号
     */
    @NotEmpty(message="请填写商户编码")
    @Column(name = "merchant_code")
    private String merchantCode;

    /**
     * 商户名称
     */
    @NotEmpty(message="请填写商户名称")
    @Column(name = "merchant_name")
    private String merchantName;

    /**
     * 商户所属渠道
     */
    @NotEmpty(message="请选择所属渠道")
    @Column(name = "channel_id")
    private String channelId;

    /**
     * 商户可用状态0:可用，1:不可用
     */
    @Column(name = "is_delete")
    private Integer isDelete;

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
     * @return merchant_code - 商户号
     */
    public String getMerchantCode() {
        return merchantCode;
    }

    /**
     * 设置商户号
     *
     * @param merchantCode 商户号
     */
    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
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
     * 获取商户可用状态0:可用，1:不可用
     *
     * @return is_delete - 商户可用状态0:可用，1:不可用
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置商户可用状态0:可用，1:不可用
     *
     * @param isDelete 商户可用状态0:可用，1:不可用
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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