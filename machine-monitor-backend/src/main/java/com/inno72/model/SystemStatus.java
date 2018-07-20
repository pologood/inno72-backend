package com.inno72.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * @package com.inno72.model
 * @auther wxt
 * @date 2018/7/4 上午9:19
 */
public class SystemStatus {


    /**
     * mechine_id : 12313131313
     * network_type : 4G
     * network_operate_name : 中国电信
     * accid : 15701200581
     * memory_free : 22222
     * memory_totle : 444444
     * cpu : 34%,23%
     * sd_free : 21313
     * sd_totle : 2311313
     * appData : [{"mechine_id":"w23131313","app_name":"com.inno72.installer","app_status":0},{"mechine_id":"w23131312","app_name":"com.inno72.setting","app_status":1}]
     * serviceData : [{"machine_id":"sdfsfd23e","app_package_name":"com.inno72.installler","service_name":"com.inno72.installler.MonitorService","service_status":1},{"machine_id":"sdfsfd23e","app_package_name":"com.inno72.installler","service_name":"com.inno72.installler.MonitorService","service_status":1}]
     */

    private String machineId;
    private String networkType;
    private String networkOperateName;
    private String accid;
    private long memoryFree;
    private long memoryTotle;
    private String cpu;
    private long sdFree;
    private long sdTotle;
    private String ping;
    private int screenIntensity;
    private int voice;

    public int getScreenIntensity() {
        return screenIntensity;
    }

    public void setScreenIntensity(int screenIntensity) {
        this.screenIntensity = screenIntensity;
    }

    public int getVoice() {
        return voice;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }

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

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getNetworkOperateName() {
        return networkOperateName;
    }

    public void setNetworkOperateName(String networkOperateName) {
        this.networkOperateName = networkOperateName;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public long getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(long memoryFree) {
        this.memoryFree = memoryFree;
    }

    public long getMemoryTotle() {
        return memoryTotle;
    }

    public void setMemoryTotle(long memoryTotle) {
        this.memoryTotle = memoryTotle;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public long getSdFree() {
        return sdFree;
    }

    public void setSdFree(long sdFree) {
        this.sdFree = sdFree;
    }

    public long getSdTotle() {
        return sdTotle;
    }

    public void setSdTotle(long sdTotle) {
        this.sdTotle = sdTotle;
    }
}
