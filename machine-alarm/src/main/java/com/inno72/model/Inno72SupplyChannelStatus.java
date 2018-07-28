package com.inno72.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_supply_channel_status")
public class Inno72SupplyChannelStatus {
    @Id
    private Integer code;

    private String result;

    /**
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }
}