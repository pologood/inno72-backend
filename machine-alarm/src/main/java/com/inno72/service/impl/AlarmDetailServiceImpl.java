package com.inno72.service.impl;

import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.AlarmExceptionMachineBean;
import com.inno72.model.AlarmMachineBean;
import com.inno72.model.Inno72Machine;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AlarmDetailServiceImpl implements AlarmDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoOperations mongoTpl;

    @Resource
    private IRedisUtil redisUtil;
    @Override
    public Result<String> add(AlarmDetailBean bean) {
        Date now = new Date();
        bean.setCreateTime(now);
        String beanId = StringUtil.getUUID();
        bean.setId(beanId);
        mongoTpl.save(bean,"AlarmDetailBean");
        Query query = new Query();
        query.addCriteria(Criteria.where("machineId").is(bean.getMachineId()));
        Update update = new Update();
        int type = bean.getType();
        AlarmMachineBean machineBean = new AlarmMachineBean();
        machineBean.setUpdateTime(now);
        String key = "";
        String timeKey = "";
        if(type == 1){//心跳
            update.set("heartId",beanId);
            update.set("heartTime",now);
            key = "ALARM_HEART_"+bean.getMachineId();
            timeKey = "ALARM_HEART_TIME_"+bean.getMachineId();
        }else if(type == 2){//网络
            update.set("connectId",beanId);
            update.set("connectTime",now);
            key = "ALARM_CONNECT_"+bean.getMachineId();
            timeKey = "ALARM_CONNECT_TIME_"+bean.getMachineId();
        }
        update.set("updateTime",now);
        mongoTpl.updateFirst(query,update,"AlarmMachineBean");
        redisUtil.del(key);
        redisUtil.del(timeKey);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public void addToMachineBean(List<Inno72Machine> list) {
        Query query = new Query();
        List<AlarmMachineBean> alarmMachineBeanList = mongoTpl.find(query,AlarmMachineBean.class,"AlarmMachineBean");
        Set<String> beanSet = new HashSet<>();
        if(alarmMachineBeanList != null && alarmMachineBeanList.size()>0){
            for(AlarmMachineBean alarmMachineBean:alarmMachineBeanList){
                beanSet.add(alarmMachineBean.getMachineId());
            }
        }
        Set<String> set = new HashSet<>();
        for(Inno72Machine machine:list){
            String machineId = machine.getId();
            set.add(machineId);
            if(!beanSet.contains(machineId)){
                AlarmMachineBean bean = new AlarmMachineBean();
                Date now = new Date();
                bean.setMachineId(machineId);
                bean.setMachineCode(machine.getMachineCode());
                bean.setMonitorStart(machine.getMonitorStart());
                bean.setMonitorEnd(machine.getMonitorEnd());
                bean.setHeartTime(now);
                bean.setConnectTime(now);
                bean.setCreateTime(now);
                bean.setUpdateTime(now);
                mongoTpl.save(bean,"AlarmMachineBean");
                logger.info("获取全部需要发送报警的机器，向MongoDB中放入机器，机器编号为"+machine.getMachineCode());
            }
        }
        for(AlarmMachineBean alarmMachineBean:alarmMachineBeanList){
            String machineId = alarmMachineBean.getMachineId();
            if(!set.contains(machineId)){
                Query delQuery = new Query();
                delQuery.addCriteria(Criteria.where("machineId").is(machineId));
                mongoTpl.remove(delQuery,"AlarmMachineBean");
                logger.info("获取全部需要发送报警的机器，从MongoDB中删除无需发送报警的机器");
            }

        }
    }

    @Override
    public void addToExceptionMachineBean() {
        List<AlarmMachineBean> list = mongoTpl.find(new Query(),AlarmMachineBean.class,"AlarmMachineBean");
        if(list != null && list.size()>0){
            Date now = new Date();
            for(AlarmMachineBean bean:list){
                String monitorStart = bean.getMonitorStart();
                String monitorEnd = bean.getMonitorEnd();
                Date startDate = null;
                String nowTime = DateUtil.toStrOld(now,DateUtil.DF_ONLY_YMD_S1_OLD);
                if(StringUtil.isNotEmpty(monitorStart)){
                    String startTime = nowTime + " " +monitorStart;
                    startDate = DateUtil.toDateOld(startTime,DateUtil.DF_ONLY_YMDHM);
                }
                Date endDate = null;
                if(StringUtil.isNotEmpty(monitorEnd)){
                    String endTime = nowTime + " " +monitorEnd;
                    endDate = DateUtil.toDateOld(endTime,DateUtil.DF_ONLY_YMDHM);
                }
                Boolean alarmFlag = true;
                if((startDate != null && startDate.after(now) || (endDate != null && endDate.before(now)))){
                    alarmFlag = false;//不发报警
                }
                if(alarmFlag){
                    this.addHeartExceptionMachine(bean,now);
                    this.addConnectExceptionMachine(bean,now);
                }

            }
        }
    }

    public void addHeartExceptionMachine(AlarmMachineBean bean,Date now){
        String key = "ALARM_HEART_"+bean.getMachineId();
        String value = redisUtil.get(key);
        Date heartTime = bean.getHeartTime();
        long sub = DateUtil.subTime(now,heartTime,2);
        AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
        exceptionBean.setId(StringUtil.getUUID());
        exceptionBean.setDetailId(bean.getHeartId());
        exceptionBean.setType(1);
        exceptionBean.setMachineId(bean.getMachineId());
        exceptionBean.setMachineCode(bean.getMachineCode());
        exceptionBean.setCreateTime(now);
        if(StringUtil.isEmpty(value) && sub>1) {//间隔时间大于1分钟
            exceptionBean.setLevel(1);
            mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
            logger.info("超过1分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
            redisUtil.set(key,"2");
        }else if("2".equals(value) && sub>5){//间隔时间大于5分钟
            exceptionBean.setLevel(2);
            mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
            logger.info("超过5分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
            redisUtil.set(key,"3");
        }else if("3".equals(value) && sub>10){//间隔大于10分钟
            exceptionBean.setLevel(3);
            mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
            logger.info("超过10分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
            redisUtil.set(key,"4");
        }else if("4".equals(value) && sub>30){//间隔大于30分钟
            String heartTimeKey = "ALARM_HEART_TIME_"+bean.getMachineId();
            String heartTimeValue = redisUtil.get(heartTimeKey);
            if(StringUtil.isEmpty(heartTimeValue)){//redis为空时发送
                exceptionBean.setLevel(4);
                mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
                logger.info("超过30分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
                redisUtil.setex(heartTimeKey,60*60*30,"1");//有效时间半个小时
            }
        }
    }

    public void addConnectExceptionMachine(AlarmMachineBean bean,Date now){
        Date connectTime = bean.getConnectTime();
        long sub = DateUtil.subTime(now,connectTime,2);
        String key = "ALARM_CONNECT_"+bean.getMachineId();
        String value = redisUtil.get(key);
        AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
        exceptionBean.setId(StringUtil.getUUID());
        exceptionBean.setDetailId(bean.getConnectId());
        exceptionBean.setType(2);
        exceptionBean.setMachineId(bean.getMachineId());
        exceptionBean.setMachineCode(bean.getMachineCode());
        exceptionBean.setCreateTime(now);
        if(StringUtil.isEmpty(value) && sub>10){//间隔时间大于10分钟
            exceptionBean.setLevel(1);
            mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
            logger.info("超过10分钟未发送连接的机器，编号为：{}",bean.getMachineCode());
            redisUtil.set(key,"2");
        }else if("2".equals(value) && sub>30){
            String connectTimeKey = "ALARM_CONNECT_TIME_"+bean.getMachineId();
            String connectTimeValue = redisUtil.get(connectTimeKey);
            if(StringUtil.isEmpty(connectTimeValue)){//redis为空时发送
                exceptionBean.setLevel(2);
                mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
                logger.info("超过30分钟未发送连接的机器，编号为：{}",bean.getMachineCode());
                redisUtil.setex(connectTimeKey,60*60*30,"1");//有效时间半个小时
            }
        }
    }
}
