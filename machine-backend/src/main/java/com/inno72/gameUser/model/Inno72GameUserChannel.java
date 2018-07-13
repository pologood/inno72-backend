package com.inno72.gameUser.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Inno72GameUserChannel {

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
     * 游戏用户ID
     */
    @Column(name="game_user_id")
    private String gameUserId;

    /**
     * 来源
     */
    @Column(name="channel_id")
    private String channelId;

    /**
     * 来源
     */
    @Column(name="channel_name")
    private String channelName;

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

    public String getGameUserId() {
        return gameUserId;
    }

    public void setGameUserId(String gameUserId) {
        this.gameUserId = gameUserId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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
}
