package com.inno72.store.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_storekeeper_storte")
public class Inno72StorekeeperStorte {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 仓库管理员ID
     */
    @Column(name = "storekeeper_id")
    private String storekeeperId;

    /**
     * 仓库ID
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取仓库管理员ID
     *
     * @return storekeeper_id - 仓库管理员ID
     */
    public String getStorekeeperId() {
        return storekeeperId;
    }

    /**
     * 设置仓库管理员ID
     *
     * @param storekeeperId 仓库管理员ID
     */
    public void setStorekeeperId(String storekeeperId) {
        this.storekeeperId = storekeeperId;
    }

    /**
     * 获取仓库ID
     *
     * @return store_id - 仓库ID
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * 设置仓库ID
     *
     * @param storeId 仓库ID
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}