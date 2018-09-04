package com.inno72.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.AlarmExceptionMachineBean;
import com.inno72.model.AlarmMachineBean;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Machine;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmDetailService;
import com.inno72.service.CheckUserService;

@Service
@Transactional
public class AlarmDetailServiceImpl implements AlarmDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoOperations mongoTpl;

    @Resource
    private IRedisUtil redisUtil;

    @Autowired
    private MsgUtil msgUtil;

    @Resource
    private CheckUserService checkUserService;

    @Value("${inno72.dingding.groupId}")
    private String groupId;
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
            key = CommonConstants.MACHINE_ALARM_HEART_BEF +bean.getMachineId();
            timeKey = CommonConstants.MACHINE_ALARM_HEART_TIME_BEF+bean.getMachineId();
        }else if(type == 2){//网络
            update.set("connectId",beanId);
            update.set("connectTime",now);
            key = CommonConstants.MACHINE_ALARM_CONNECT_BEF+bean.getMachineId();
            timeKey = CommonConstants.MACHINE_ALARM_CONNECT_TIME_BEF+bean.getMachineId();
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
            String localeStr = machine.getLocaleStr();
            if(!beanSet.contains(machineId)){
                AlarmMachineBean bean = new AlarmMachineBean();
                Date now = new Date();
                bean.setMachineId(machineId);
                bean.setMachineCode(machine.getMachineCode());
                bean.setMonitorStart(machine.getMonitorStart());
                bean.setMonitorEnd(machine.getMonitorEnd());
                bean.setLocaleStr(localeStr);
                bean.setHeartTime(now);
                bean.setConnectTime(now);
                bean.setCreateTime(now);
                bean.setUpdateTime(now);
                mongoTpl.save(bean,"AlarmMachineBean");
                logger.info("获取全部需要发送报警的机器，向MongoDB中放入机器，机器编号为"+machine.getMachineCode());
            }else if(StringUtil.isNotEmpty(localeStr)){
                Update update = new Update();
                update.set("localeStr",localeStr);
                Query upQuery = new Query();
                upQuery.addCriteria(Criteria.where("machineId").is(machineId));
                mongoTpl.updateFirst(upQuery,update,"AlarmMachineBean");
            }
        }
        if(alarmMachineBeanList != null && alarmMachineBeanList.size()>0){
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

    @Override
    public void sendExceptionMachineAlarm() {
        List<AlarmExceptionMachineBean> list = mongoTpl.find(new Query(),AlarmExceptionMachineBean.class,"AlarmExceptionMachineBean");
        if(list != null && list.size()>0){
            for(AlarmExceptionMachineBean bean:list){
                int type = bean.getType();
                int level = bean.getLevel();
                Map<String,String> param = new HashMap<>();
                String localeStr = bean.getLocaleStr();
                String machineCode = bean.getMachineCode();
                param.put("machineCode", machineCode);
                param.put("localStr", localeStr);
                String text = "";
                Query query = new Query();
                query.addCriteria(Criteria.where("id").is(bean.getDetailId()));
                AlarmDetailBean detailBean = mongoTpl.findOne(query,AlarmDetailBean.class,"AlarmDetailBean");
                String pageInfo = "";
                if(detailBean != null){
                    pageInfo = detailBean.getPageInfo();
                    if(StringUtil.isNotEmpty(pageInfo)){
                        pageInfo = "，页面停留在"+pageInfo;
                    }
                }
                if(type == 1){
                    if(level == 1){
                        text = "您好，"+localeStr+"，机器编号："+machineCode+"，出现网络异常，已经持续1分钟"+pageInfo+"，请及时联系巡检人员。";
                    }else if(level == 2){
                        text = "您好，"+localeStr+"，机器编号："+machineCode+"，出现网络异常，已经持续5分钟"+pageInfo+"，请及时联系巡检人员。";
                    }else if(level == 3){
                        text = "您好，"+localeStr+"，机器编号："+machineCode+"，出现网络异常，已经持续10分钟"+pageInfo+"，请及时联系巡检人员。";
                    }else if(level == 4){
                        text = "您好，"+localeStr+"，机器编号："+machineCode+"，出现网络异常，已经持续30分钟"+pageInfo+"，请及时联系巡检人员。";
                    }
                    param.put("text",text);
                    msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-AlarmDetailService");
                    logger.info("心跳异常发送钉钉消息："+groupId);
                }else if(type == 2){
                    if(level == 1){
                        String active = System.getenv("spring_profiles_active");
                        if (StringUtil.isNotEmpty(active) && active.equals("prod")) {
                            List<Inno72CheckUserPhone> phones = getInno72CheckUserPhones(machineCode);
                            if(phones != null && phones.size()>0){
                                text = "【互动管家】您好，"+localeStr+"，机器编号："+machineCode+"，网络已经连续10分钟未连接成功，请及时处理";
                                param.put("text",text);
                                for (Inno72CheckUserPhone userPhone:phones){
                                    msgUtil.sendSMS("sms_alarm_common", param, userPhone.getPhone(), "machineAlarm-AlarmDetailService");
                                }
                            }
                        }
                        text = "您好，"+localeStr+"，机器编号："+machineCode+"，网络已经连续10分钟未连接成功，请及时联系巡检人员。";
                    }else if(level == 2){
                        text = "您好，"+localeStr+"，机器编号："+machineCode+"，网络已经连续30分钟未连接成功，请及时联系巡检人员。";
                    }
                    param.put("text",text);
                    msgUtil.sendDDTextByGroup("dingding_alarm_common", param, groupId, "machineAlarm-AlarmDetailService");
                    logger.info("网络连接异常发送钉钉消息："+groupId);
                }
                Query removeQuery = new Query();
                removeQuery.addCriteria(Criteria.where("_id").is(bean.getId()));
                mongoTpl.remove(removeQuery,"AlarmExceptionMachineBean");
            }
        }
    }

    public void addHeartExceptionMachine(AlarmMachineBean bean,Date now){
        String key = CommonConstants.MACHINE_ALARM_HEART_BEF+bean.getMachineId();
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
        exceptionBean.setLocaleStr(bean.getLocaleStr());
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
            String heartTimeKey = CommonConstants.MACHINE_ALARM_HEART_TIME_BEF+bean.getMachineId();
            String heartTimeValue = redisUtil.get(heartTimeKey);
            if(StringUtil.isEmpty(heartTimeValue)){//redis为空时发送
                exceptionBean.setLevel(4);
                mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
                logger.info("超过30分钟未发送心跳的机器，编号为：{}",bean.getMachineCode());
                redisUtil.setex(heartTimeKey,60*30,"1");//有效时间半个小时
            }
        }
    }

    public void addConnectExceptionMachine(AlarmMachineBean bean,Date now){
        Date connectTime = bean.getConnectTime();
        long sub = DateUtil.subTime(now,connectTime,2);
        String key = CommonConstants.MACHINE_ALARM_CONNECT_BEF+bean.getMachineId();
        String value = redisUtil.get(key);
        AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
        exceptionBean.setId(StringUtil.getUUID());
        exceptionBean.setDetailId(bean.getConnectId());
        exceptionBean.setType(2);
        exceptionBean.setMachineId(bean.getMachineId());
        exceptionBean.setMachineCode(bean.getMachineCode());
        exceptionBean.setLocaleStr(bean.getLocaleStr());
        exceptionBean.setCreateTime(now);
        if(StringUtil.isEmpty(value) && sub>10){//间隔时间大于10分钟
            exceptionBean.setLevel(1);
            mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
            logger.info("超过10分钟未发送连接的机器，编号为：{}",bean.getMachineCode());
            redisUtil.set(key,"2");
        }else if("2".equals(value) && sub>30){
            String connectTimeKey = CommonConstants.MACHINE_ALARM_CONNECT_TIME_BEF+bean.getMachineId();
            String connectTimeValue = redisUtil.get(connectTimeKey);
            if(StringUtil.isEmpty(connectTimeValue)){//redis为空时发送
                exceptionBean.setLevel(2);
                mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
                logger.info("超过30分钟未发送连接的机器，编号为：{}",bean.getMachineCode());
                redisUtil.setex(connectTimeKey,60*30,"1");//有效时间半个小时
            }
        }
    }

    private List<Inno72CheckUserPhone> getInno72CheckUserPhones(String machineCode) {
        Inno72CheckUserPhone inno72CheckUserPhone = new Inno72CheckUserPhone();
        inno72CheckUserPhone.setMachineCode(machineCode);
        return checkUserService.selectPhoneByMachineCode(inno72CheckUserPhone);
    }
}
