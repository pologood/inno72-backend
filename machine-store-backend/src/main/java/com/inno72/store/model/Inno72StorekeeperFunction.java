package com.inno72.store.model;

import javax.persistence.*;

@Table(name = "inno72_storekeeper_function")
public class Inno72StorekeeperFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "storekeeper_id")
    private String storekeeperId;

    @Column(name = "function_id")
    private String functionId;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return storekeeper_id
     */
    public String getStorekeeperId() {
        return storekeeperId;
    }

    /**
     * @param storekeeperId
     */
    public void setStorekeeperId(String storekeeperId) {
        this.storekeeperId = storekeeperId;
    }

    /**
     * @return function_id
     */
    public String getFunctionId() {
        return functionId;
    }

    /**
     * @param functionId
     */
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }
}