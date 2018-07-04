package com.inno72.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_channel")
public class Inno72Channel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 渠道编码
     */
    @NotEmpty(message="请填写渠道编码")
    @Column(name = "channel_code")
    private String channelCode;

    /**
     * 渠道名称
     */
    @NotEmpty(message="请填写渠道名称")
    @Column(name = "channel_name")
    private String channelName;

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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 渠道是否删除，0:正常，1:删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

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
     * 获取渠道id
     *
     * @return channel_code - 渠道id
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * 设置渠道id
     *
     * @param channelCode 渠道id
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    /**
     * 获取渠道名称
     *
     * @return channel_name - 渠道名称
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * 设置渠道名称
     *
     * @param channelName 渠道名称
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
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
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取渠道是否删除，0:正常，1:删除
     *
     * @return is_delete - 渠道是否删除，0:正常，1:删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置渠道是否删除，0:正常，1:删除
     *
     * @param isDelete 渠道是否删除，0:正常，1:删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}