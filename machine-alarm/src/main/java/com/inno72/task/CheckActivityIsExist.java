package com.inno72.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.DateUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.vo.Inno72NoPlanInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

import static com.inno72.common.DateUtil.DF_FULL_S1;

/**
 * @Auther: wxt
 * @Date: 2018/7/16 17:00
 * @Description:检测机器上是否有活动
 */
@Configuration
@EnableScheduling
public class CheckActivityIsExist {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 10 * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void checkActivityIsExist() {

        log.info("检查机器上是否有活动排期的定时任务，开始执行");

        String nowTime = DateUtil.toTimeStr(LocalDateTime.now(),DF_FULL_S1) ;
        String noPlanInfoVos = HttpClient.post("http://localhost:8880/project/activityPlan/selectNoPlanMachines",nowTime);
        JSONObject jsonObject = JSONObject.parseObject(noPlanInfoVos);
        Integer resultCdoe = jsonObject.getInteger("code");
        List<Inno72NoPlanInfoVo> noPlanInfoVoList = JSON.parseArray(jsonObject.getString("data"), Inno72NoPlanInfoVo.class);

        log.info("检查机器上是否有活动排期的定时任务，查询后list：{}",noPlanInfoVoList.toString());
        log.info("检查机器上是否有活动排期的定时任务，执行结束");

    }

}

