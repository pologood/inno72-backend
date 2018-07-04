package com.inno72.model;

/**
 * @Auther: wxt
 * @Date: 2018/7/4 14:01
 * @Description:
 */
public class AppStatus {

    private String machine_id;
    private String app_package_name;
    private String version_name;
    private int version_code;
    private String app_name;
    private int app_status;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getApp_package_name() {
        return app_package_name;
    }

    public void setApp_package_name(String app_package_name) {
        this.app_package_name = app_package_name;
    }

    public String getService_name() {
        return version_name;
    }

    public void setService_name(String service_name) {
        this.version_name = service_name;
    }

    public int getService_status() {
        return version_code;
    }

    public void setService_status(int service_status) {
        this.version_code = service_status;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public int getApp_status() {
        return app_status;
    }

    public void setApp_status(int app_status) {
        this.app_status = app_status;
    }
}

