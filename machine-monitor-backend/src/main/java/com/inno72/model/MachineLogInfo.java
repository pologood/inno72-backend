package com.inno72.model;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Auther: wxt
 * @Date: 2018/7/6 18:49
 * @Description:保存机器Id与时间
 */
public class MachineLogInfo {

    private String machineId;
    private Date createTime;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

