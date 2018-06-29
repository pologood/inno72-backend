package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_locale")
public class Inno72Locale {
    /**
     * 活动ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 省ID
     */
    private String province;

    /**
     * 市ID
     */
    private String city;

    /**
     * 区/县ID
     */
    private String district;

    /**
     * 商圈ID
     */
    private String circle;

    /**
     * 商场
     */
    private String mail;

    /**
     * 运营人员
     */
    private String manager;

    /**
     * 运营人员手机
     */
    private String mobile;

    /**
     * 状态：0正常，1停止
     */
    private Integer state;

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
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取省ID
     *
     * @return province - 省ID
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省ID
     *
     * @param province 省ID
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取市ID
     *
     * @return city - 市ID
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市ID
     *
     * @param city 市ID
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取区/县ID
     *
     * @return district - 区/县ID
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 设置区/县ID
     *
     * @param district 区/县ID
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 获取商圈ID
     *
     * @return circle - 商圈ID
     */
    public String getCircle() {
        return circle;
    }

    /**
     * 设置商圈ID
     *
     * @param circle 商圈ID
     */
    public void setCircle(String circle) {
        this.circle = circle;
    }

    /**
     * 获取商场
     *
     * @return mail - 商场
     */
    public String getMail() {
        return mail;
    }

    /**
     * 设置商场
     *
     * @param mail 商场
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 获取运营人员
     *
     * @return manager - 运营人员
     */
    public String getManager() {
        return manager;
    }

    /**
     * 设置运营人员
     *
     * @param manager 运营人员
     */
    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * 获取运营人员手机
     *
     * @return mobile - 运营人员手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置运营人员手机
     *
     * @param mobile 运营人员手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取状态：0正常，1停止
     *
     * @return state - 状态：0正常，1停止
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：0正常，1停止
     *
     * @param state 状态：0正常，1停止
     */
    public void setState(Integer state) {
        this.state = state;
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