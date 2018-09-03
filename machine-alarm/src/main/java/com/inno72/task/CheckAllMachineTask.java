package com.inno72.task;

import com.inno72.model.AlarmMachineBean;
import com.inno72.model.Inno72Machine;
import com.inno72.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
public class CheckAllMachineTask {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MachineService machineService;

    @Autowired
    private MongoOperations mongoTpl;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkAllMachine(){
        log.info("获取全部需要发送报警的机器开始");
        List<Inno72Machine> list = machineService.findAlarmAllMachine();
        if(list != null && list.size()>0){
            log.info("获取全部需要发送报警的机器，共找到"+list.size()+"台机器");
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
                    bean.setCreateTime(now);
                    bean.setUpdateTime(now);
                    mongoTpl.save(bean,"AlarmMachineBean");
                    log.info("获取全部需要发送报警的机器，向MongoDB中放入机器，机器编号为"+machine.getMachineCode());
                }
            }
            for(AlarmMachineBean alarmMachineBean:alarmMachineBeanList){
                String machineId = alarmMachineBean.getMachineId();
                if(!set.contains(machineId)){
                    Query delQuery = new Query();
                    delQuery.addCriteria(Criteria.where("machineId").is(machineId));
                    mongoTpl.remove(delQuery,"AlarmMachineBean");
                    log.info("获取全部需要发送报警的机器，从MongoDB中删除无需发送报警的机器");
                }

            }
            log.info("获取全部需要发送报警的机器结束");
        }
    }
}
