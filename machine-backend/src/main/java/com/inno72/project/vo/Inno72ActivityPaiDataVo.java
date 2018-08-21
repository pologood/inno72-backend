package com.inno72.project.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.project.model.Inno72PaiNowData;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

public class Inno72ActivityPaiDataVo {

    private String activityName;

    private String activityId;

    private String activityPlanId;

    /**
     * 活动开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;


    private List<Inno72PaiNowData> inno72PaiNowDataList;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityPlanId() {
        return activityPlanId;
    }

    public void setActivityPlanId(String activityPlanId) {
        this.activityPlanId = activityPlanId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Inno72PaiNowData> getInno72PaiNowDataList() {
        return inno72PaiNowDataList;
    }

    public void setInno72PaiNowDataList(List<Inno72PaiNowData> inno72PaiNowDataList) {
        this.inno72PaiNowDataList = inno72PaiNowDataList;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


}
