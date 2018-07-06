package com.inno72.system.model;

import javax.persistence.*;

@Table(name = "inno72_role")
public class Inno72Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}