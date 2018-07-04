package com.inno72.model;

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

    private String machine_id;
    private String network_type;
    private String network_operate_name;
    private String accid;
    private long memory_free;
    private long memory_totle;
    private String cpu;
    private long sd_free;
    private long sd_totle;

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    private String ping;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public String getNetwork_operate_name() {
        return network_operate_name;
    }

    public void setNetwork_operate_name(String network_operate_name) {
        this.network_operate_name = network_operate_name;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public long getMemory_free() {
        return memory_free;
    }

    public void setMemory_free(long memory_free) {
        this.memory_free = memory_free;
    }

    public long getMemory_totle() {
        return memory_totle;
    }

    public void setMemory_totle(long memory_totle) {
        this.memory_totle = memory_totle;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public long getSd_free() {
        return sd_free;
    }

    public void setSd_free(long sd_free) {
        this.sd_free = sd_free;
    }

    public long getSd_totle() {
        return sd_totle;
    }

    public void setSd_totle(long sd_totle) {
        this.sd_totle = sd_totle;
    }

}
