package com.inno72.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.DateUtil;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Auther: wxt
 * @Date: 2018/7/6 18:36
 * @Description:检查并修改网络状态的定时任务
 */
@Configuration
@EnableScheduling
public class CheckNetStatusSchedule {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoOperations mongoTpl;

	// 每15分钟执行一次
    @Scheduled(cron = "0 0/15 * * * ?")
	public void checkNetStatus() {

		log.info("检查并修改网络状态的定时任务开始执行");
		// 从mongoDB中获取
		DBCollection dbCollection = mongoTpl.getCollection("MachineLogInfo");
		DBCursor dbCursor = dbCollection.find();
		List<DBObject> list = dbCursor.toArray();
		log.info("检查并修改网络状态的定时任务执行中,数据库数据{}" , list.get(0));
		if(list != null){
			for (DBObject db : list) {

				Date date1 = null;

				JSONObject jsonObject = JSON.parseObject(db.toString());
				JSONObject createTimeObject = jsonObject.getJSONObject("createTime");
				if (createTimeObject != null) {
					log.info("创建时间date:{}", JSON.toJSONString(createTimeObject));
					String createTime = createTimeObject.getString("$date");
					if (StringUtils.isNotEmpty(createTime)) {
						createTime = createTime.replace("T", " ");
						createTime = createTime.substring(0, createTime.indexOf("."));
						log.info("检查并修改网络状态的定时任务执行中,数据库createTime:{} " , createTime);
						// 创建时间
						date1 = DateUtil.toDateOld(createTime, DateUtil.DF_ONLY_ALL_S1_OLD);

					}
					// TODO
					// 将当前时间与数据库时间对比，如果大于10分钟，调用修改网络状态方法
					int timeDifference = DateUtil.compareTime(date1, new Date(), 2);
					log.info("数据库时间与当前时间的时间差，timeDifference :{}" ,timeDifference);
					if (timeDifference >= 10) {

						log.info("调用修改网络状态的方法");
					}

				}

				log.info("检查并修改网络状态的定时任务执行结束");
			}

		}else {
			log.info("当前数据库中不存在数据");
		}

		log.info("检查并修改网络状态的定时任务执行结束");

	}
}
