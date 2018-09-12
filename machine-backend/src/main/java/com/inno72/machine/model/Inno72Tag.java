package com.inno72.machine.model;

import javax.persistence.*;

@Table(name = "inno72_tag")
public class Inno72Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 标签名称
     */
    private String name;

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
     * 获取标签名称
     *
     * @return name - 标签名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标签名称
     *
     * @param name 标签名称
     */
    public void setName(String name) {
        this.name = name;
    }
}