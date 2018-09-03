package com.inno72.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_user_function_data")
public class Inno72UserFunctionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 一级菜单ID
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * 二级菜单ID
     */
    @Column(name = "second_name")
    private String secondName;

    /**
     * 列级ID
     */
    @Column(name = "third_name")
    private String thirdName;

    /**
     * 对象包名
     */
    @Column(name = "vo_name")
    private String voName;

    /**
     * 不显示列
     */
    @Column(name = "vo_column")
    private String voColumn;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取一级菜单ID
     *
     * @return first_name - 一级菜单ID
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 设置一级菜单ID
     *
     * @param firstName 一级菜单ID
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 获取二级菜单ID
     *
     * @return second_name - 二级菜单ID
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * 设置二级菜单ID
     *
     * @param secondName 二级菜单ID
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * 获取列级ID
     *
     * @return third_name - 列级ID
     */
    public String getThirdName() {
        return thirdName;
    }

    /**
     * 设置列级ID
     *
     * @param thirdName 列级ID
     */
    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
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
     * 获取不显示列
     *
     * @return vo_column - 不显示列
     */
    public String getVoColumn() {
        return voColumn;
    }

    /**
     * 设置不显示列
     *
     * @param voColumn 不显示列
     */
    public void setVoColumn(String voColumn) {
        this.voColumn = voColumn;
    }

    /**
     * 获取创建人
     *
     * @return create_id - 创建人
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}