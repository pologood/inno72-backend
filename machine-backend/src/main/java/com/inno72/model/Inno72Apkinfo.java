package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_apkInfo")
public class Inno72Apkinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "marchine_id")
    private String marchineId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "version_code")
    private Integer versionCode;

    @Column(name = "version_name")
    private String versionName;

    private String url;

    private Integer status;

    @Column(name = "onoff_status")
    private Integer onoffStatus;

    @Column(name = "create_id")
    private String createId;

    @Column(name = "update_id")
    private String updateId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * @return marchine_id
     */
    public String getMarchineId() {
        return marchineId;
    }

    /**
     * @param marchineId
     */
    public void setMarchineId(String marchineId) {
        this.marchineId = marchineId;
    }

    /**
     * @return app_name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return version_code
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return version_name
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return onoff_status
     */
    public Integer getOnoffStatus() {
        return onoffStatus;
    }

    /**
     * @param onoffStatus
     */
    public void setOnoffStatus(Integer onoffStatus) {
        this.onoffStatus = onoffStatus;
    }

    /**
     * @return create_id
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * @param createId
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * @return update_id
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * @param updateId
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}