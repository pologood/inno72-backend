package com.inno72.check.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_check_sign_in")
public class Inno72CheckSignIn {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 巡检人员ID
     */
    @Column(name = "check_user_id")
    private String checkUserId;

    /**
     * 机器ID
     */
    @Column(name = "machine_id")
    private String machineId;

    /**
     * 签到项
     */
    private String type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取巡检人员ID
     *
     * @return check_user_id - 巡检人员ID
     */
    public String getCheckUserId() {
        return checkUserId;
    }

    /**
     * 设置巡检人员ID
     *
     * @param checkUserId 巡检人员ID
     */
    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    /**
     * 获取机器ID
     *
     * @return machine_id - 机器ID
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * 设置机器ID
     *
     * @param machineId 机器ID
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取签到项
     *
     * @return type - 签到项
     */
    public String getType() {
        return type;
    }

    /**
     * 设置签到项
     *
     * @param type 签到项
     */
    public void setType(String type) {
        this.type = type;
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