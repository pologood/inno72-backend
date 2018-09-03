package com.inno72.task;

import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmExceptionMachineBean;
import com.inno72.model.AlarmMachineBean;
import com.inno72.redis.IRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class CheckExceptionMachineTask {

    @Autowired
    private MongoOperations mongoTpl;

    @Resource
    private IRedisUtil redisUtil;

    public void checkExceptionMachine(){
        List<AlarmMachineBean> list = mongoTpl.find(new Query(),AlarmMachineBean.class,"AlarmMachineBean");
        if(list != null && list.size()>0){
            Date now = new Date();
            for(AlarmMachineBean bean:list){
                String heartId = bean.getHeartId();
                String connectId = bean.getConnectId();
                if(StringUtil.isNotEmpty(heartId)){
                    String heartKey = redisUtil.get("ALARM_DETAIL_"+bean.getMachineCode());
                    Date heartTime = bean.getHeartTime();
                    if(DateUtil.subTime(now,heartTime,2)>1){//间隔时间大于1分钟
                        AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
                        exceptionBean.setId(StringUtil.getUUID());
                        exceptionBean.setDetailId(heartId);
                        exceptionBean.setLevel(1);
                        exceptionBean.setType(1);
                        exceptionBean.setMachineId(bean.getMachineId());
                        exceptionBean.setMachineCode(bean.getMachineCode());
                        exceptionBean.setCreateTime(now);
                        mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
                    }
                }
                if(StringUtil.isNotEmpty(connectId)){
                    Date connectTime = bean.getConnectTime();
                    if(DateUtil.subTime(now,connectTime,2)>1){//间隔时间大于1分钟
                        AlarmExceptionMachineBean exceptionBean = new AlarmExceptionMachineBean();
                        exceptionBean.setId(StringUtil.getUUID());
                        exceptionBean.setDetailId(connectId);
                        exceptionBean.setLevel(1);
                        exceptionBean.setType(2);
                        exceptionBean.setMachineId(bean.getMachineId());
                        exceptionBean.setMachineCode(bean.getMachineCode());
                        exceptionBean.setCreateTime(now);
                        mongoTpl.save(exceptionBean,"AlarmExceptionMachineBean");
                    }
                }
            }
        }
    }
}
