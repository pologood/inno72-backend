package com.inno72.gameUser.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Inno72GameUser {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 用户昵称
     */
    @Column(name="user_nick")
    private String userNick;

    /**
     * 手机号
     */
    @Column(name="phone")
    private String phone;

    /**
     * 来源
     */
    @Column(name="channel")
    private String channel;

    /**
     * 用户key
     */
    @Column(name="channel_user_key")
    private String channelUserKey;

    /**
     * 创建时间
     */
    @Column(name="create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name="update_time")
    private Date updateTime;
    /**
     * 操作人
     */
    @Column(name="operator")
    private String operator;

    /**
     * 操作人ID
     */
    @Column(name="operator_id")
    private String operatord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelUserKey() {
        return channelUserKey;
    }

    public void setChannelUserKey(String channelUserKey) {
        this.channelUserKey = channelUserKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatord() {
        return operatord;
    }

    public void setOperatord(String operatord) {
        this.operatord = operatord;
    }
}
