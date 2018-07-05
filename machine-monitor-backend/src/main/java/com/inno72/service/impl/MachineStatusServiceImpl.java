package com.inno72.service.impl;

import com.inno72.service.MachineStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * @Auther: wxt
 * @Date: 2018/7/4 10:50
 * @Description:
 */
@Component
public class MachineStatusServiceImpl implements MachineStatusService {

    @Autowired
    private MongoOperations mongoTpl;


}

