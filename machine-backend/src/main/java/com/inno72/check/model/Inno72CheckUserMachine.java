package com.inno72.check.model;

import javax.persistence.*;

@Table(name = "inno72_check_user_machine")
public class Inno72CheckUserMachine {
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
}