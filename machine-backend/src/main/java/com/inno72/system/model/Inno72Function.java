package com.inno72.system.model;

import javax.persistence.*;

@Table(name = "inno72_function")
public class Inno72Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "function_depict")
    private String functionDepict;

    @Column(name = "function_path")
    private String functionPath;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "function_level")
    private Integer functionLevel;

    @Column(name = "function_icon")
    private String functionIcon;

    private String color;

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
     * @return function_depict
     */
    public String getFunctionDepict() {
        return functionDepict;
    }

    /**
     * @param functionDepict
     */
    public void setFunctionDepict(String functionDepict) {
        this.functionDepict = functionDepict;
    }

    /**
     * @return function_path
     */
    public String getFunctionPath() {
        return functionPath;
    }

    /**
     * @param functionPath
     */
    public void setFunctionPath(String functionPath) {
        this.functionPath = functionPath;
    }

    /**
     * @return parent_id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return function_level
     */
    public Integer getFunctionLevel() {
        return functionLevel;
    }

    /**
     * @param functionLevel
     */
    public void setFunctionLevel(Integer functionLevel) {
        this.functionLevel = functionLevel;
    }

    /**
     * @return function_icon
     */
    public String getFunctionIcon() {
        return functionIcon;
    }

    /**
     * @param functionIcon
     */
    public void setFunctionIcon(String functionIcon) {
        this.functionIcon = functionIcon;
    }

    /**
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }
}