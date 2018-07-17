package com.inno72.vo;

/**
 * @Auther: wxt
 * @Date: 2018/7/17 10:18
 * @Description:用于查询没有活动排期的机器信息
 */
public class Inno72NoPlanInfoVo {

    private String id;

    private String machineCode;

    private String area;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

