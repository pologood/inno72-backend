package com.inno72.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

@Table(name = "inno72_activity")
public class Inno72Activity {
    /**
     * 活动ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 活动名称
     */
    @NotEmpty(message="请填写活动名称")
    private String name;

    /**
     * 商户ID
     */
    @NotEmpty(message="请选择所属商铺")
    @Column(name = "shop_id")
    private String shopId;

    /**
     * 活动开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 状态：0正常，1停止
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 备注描述
     */
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 获取活动ID
     *
     * @return id - 活动ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置活动ID
     *
     * @param id 活动ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取活动名称
     *
     * @return name - 活动名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置活动名称
     *
     * @param name 活动名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

    /**
     * 获取活动开始时间
     *
     * @return start_time - 活动开始时间
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置活动开始时间
     *
     * @param startTime 活动开始时间
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取活动结束时间
     *
     * @return end_time - 活动结束时间
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置活动结束时间
     *
     * @param endTime 活动结束时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取状态：0正常，1停止
     *
     * @return is_delete - 状态：0正常，1停止
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置状态：0正常，1停止
     *
     * @param isDelete 状态：0正常，1停止
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取备注描述
     *
     * @return remark - 备注描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注描述
     *
     * @param remark 备注描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
}