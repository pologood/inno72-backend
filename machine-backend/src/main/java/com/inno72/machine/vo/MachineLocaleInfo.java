package com.inno72.machine.vo;

/**
 * @Auther: wxt
 * @Date: 2018/7/18 11:25
 * @Description:机器与点位信息表
 */
public class MachineLocaleInfo {

    //机器编码
    private String machineCode;

    //点位信息
    private String localeStr;

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }
}

