package com.inno72.task;

import com.inno72.machine.model.Inno72SupplyChannel;
import com.inno72.machine.service.SupplyChannelService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SupplyChannelTask {

    @Resource
    private SupplyChannelService supplyChannelService;

//    @Scheduled(cron = "0/5 * * * * ?")
    public void task(){
        List<Inno72SupplyChannel> list = supplyChannelService.findByTaskParam();
        if(list != null && list.size()>0){
            System.out.print("获取到数据"+LocalDateTime.now());
        }
    }
}
