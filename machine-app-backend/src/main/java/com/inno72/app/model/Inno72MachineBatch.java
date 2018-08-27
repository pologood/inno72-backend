package com.inno72.app.model;

import javax.persistence.*;

@Table(name = "inno72_machine_batch")
public class Inno72MachineBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "batch_name")
    private String batchName;

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
     * @return batch_name
     */
    public String getBatchName() {
        return batchName;
    }

    /**
     * @param batchName
     */
    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
}