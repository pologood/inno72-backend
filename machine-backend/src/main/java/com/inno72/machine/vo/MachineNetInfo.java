package com.inno72.machine.vo;

/**
 * @Auther: wxt
 * @Date: 2018/7/13 14:14
 * @Description:机器编码以及网络状态vo
 */
public class MachineNetInfo {

    private Integer netStatus;
    private String machineCode;

    public Integer getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(Integer netStatus) {
        this.netStatus = netStatus;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}

