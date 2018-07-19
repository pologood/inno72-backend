package com.inno72.check.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class Inno72CheckUser {

    /**
     * uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 名字
     */
    @Column(name="name")
    private String name;

    /**
     * 手机号
     */
    @Column(name="phone")
    private String phone;

    /**
     * 密码
     */
    @Column(name="password")
    private String password;

    /**
     * 身份证号
     */
    @Column(name="card_no")
    private String cardNo;

    /**
     * 公司
     */
    @Column(name="enterprise")
    private String enterprise;

    /**
     * 性别
     */
    @Column(name="sex")
    private int sex;

    /**
     * 邮箱
     */
    @Column(name="email")
    private String email;

    /**
     * 头像
     */
    @Column(name="head_image")
    private String headImage;



    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="create_time")
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @Column(name="is_delete")
    private int isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
