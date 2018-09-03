package com.inno72.service.impl;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.AlarmMachineBean;
import com.inno72.service.AlarmDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AlarmDetailServiceImpl implements AlarmDetailService {

    @Autowired
    private MongoOperations mongoTpl;
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

        if(type == 1){//心跳
            update.set("heartId",beanId);
            update.set("heartTime",now);
        }else if(type == 2){//网络
            update.set("connectId",beanId);
            update.set("connectTime",now);
        }
        update.set("updateTime",now);
        mongoTpl.updateFirst(query,update,"AlarmMachineBean");
        return ResultGenerator.genSuccessResult();
    }
}
