package com.inno72.task;

import com.inno72.model.DropGoodsExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @auther: wxt
 * @date: 2018/7/19 16:14
 * @Description:每天晚上零点定时删除掉货异常信息表中的数据
 */
@Configuration
@EnableScheduling
public class DeleteDropGoodsExceptionTask {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoOperations mongoTpl;

    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void checkActivityIsExist() {

        log.info("每天晚上零点定时删除掉货异常信息表中的数据定时任务，开始执行");
        List<DropGoodsExceptionInfo> dropGoodsExceptionInfos = mongoTpl.find(new Query(), DropGoodsExceptionInfo.class, "DropGoodsExceptionInfo");
        if (dropGoodsExceptionInfos.size() > 0) {
            mongoTpl.dropCollection("DropGoodsExceptionInfo");
        } else {
            return;
        }

        log.info("每天晚上零点定时删除掉货异常信息表中的数据定时任务，执行结束");

    }
}

