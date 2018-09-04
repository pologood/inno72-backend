package com.inno72.system.model;

import javax.persistence.*;

@Table(name = "inno72_function_data")
public class Inno72FunctionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 权限描述
     */
    @Column(name = "function_depict")
    private String functionDepict;

    /**
     * 路径
     */
    @Column(name = "function_path")
    private String functionPath;

    /**
     * 父级ID
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 对象包名
     */
    @Column(name = "vo_name")
    private String voName;

    /**
     * 列
     */
    private String column;

    /**
     * 排序
     */
    private Integer seq;

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
     * 获取权限描述
     *
     * @return function_depict - 权限描述
     */
    public String getFunctionDepict() {
        return functionDepict;
    }

    /**
     * 设置权限描述
     *
     * @param functionDepict 权限描述
     */
    public void setFunctionDepict(String functionDepict) {
        this.functionDepict = functionDepict;
    }

    /**
     * 获取路径
     *
     * @return function_path - 路径
     */
    public String getFunctionPath() {
        return functionPath;
    }

    /**
     * 设置路径
     *
     * @param functionPath 路径
     */
    public void setFunctionPath(String functionPath) {
        this.functionPath = functionPath;
    }

    /**
     * 获取父级ID
     *
     * @return parent_id - 父级ID
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置父级ID
     *
     * @param parentId 父级ID
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取对象包名
     *
     * @return vo_name - 对象包名
     */
    public String getVoName() {
        return voName;
    }

    /**
     * 设置对象包名
     *
     * @param voName 对象包名
     */
    public void setVoName(String voName) {
        this.voName = voName;
    }

    /**
     * 获取列
     *
     * @return column - 列
     */
    public String getColumn() {
        return column;
    }

    /**
     * 设置列
     *
     * @param column 列
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}