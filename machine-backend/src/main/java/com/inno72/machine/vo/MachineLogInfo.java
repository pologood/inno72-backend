package com.inno72.machine.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * @Auther: wxt
 * @Date: 2018/7/6 18:49
 * @Description:保存机器Id与时间
 */
public class MachineLogInfo {

    private String machineId;
    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }
    /**
     *
     *重写equls方法
     * */
    /*@Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }

        if(object == null){
            return false;
        }

        if(this.getClass() != object.getClass()){
            return false;
        }

        final MachineLogInfo machineLogInfo = (MachineLogInfo) object;
        if(this.getMachineId().equals(machineLogInfo.getMachineId())){
            return true;
        }

        return false;
    }*/

}

