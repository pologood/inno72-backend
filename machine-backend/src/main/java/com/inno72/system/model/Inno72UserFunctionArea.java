package com.inno72.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_user_function_area")
public class Inno72UserFunctionArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 区域code
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 区域名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 省code
     */
    @Column(name = "province_code")
    private String provinceCode;

    /**
     * 省名称
     */
    @Column(name = "province_name")
    private String provinceName;

    /**
     * 市code
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 市名称
     */
    @Column(name = "city_name")
    private String cityName;

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
     * @return id
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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取区域code
     *
     * @return area_code - 区域code
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置区域code
     *
     * @param areaCode 区域code
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 获取区域名称
     *
     * @return area_name - 区域名称
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置区域名称
     *
     * @param areaName 区域名称
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * 获取省code
     *
     * @return province_code - 省code
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * 设置省code
     *
     * @param provinceCode 省code
     */
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    /**
     * 获取省名称
     *
     * @return province_name - 省名称
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * 设置省名称
     *
     * @param provinceName 省名称
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取市code
     *
     * @return city_code - 市code
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * 设置市code
     *
     * @param cityCode 市code
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 获取市名称
     *
     * @return city_name - 市名称
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * 设置市名称
     *
     * @param cityName 市名称
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
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
}