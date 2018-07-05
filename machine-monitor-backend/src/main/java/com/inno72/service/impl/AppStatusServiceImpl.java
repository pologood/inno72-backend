package com.inno72.service.impl;

import com.inno72.service.AppStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * @Auther: wxt
 * @Date: 2018/7/4 14:08
 * @Description:
 */
@Component
public class AppStatusServiceImpl implements AppStatusService {

    @Autowired
    private MongoOperations mongoTpl;
}


