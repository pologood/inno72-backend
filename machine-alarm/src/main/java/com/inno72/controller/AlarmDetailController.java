package com.inno72.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.model.AlarmDetailBean;
import com.inno72.service.AlarmDetailService;

@RestController
@RequestMapping("/alarm/detail")
public class AlarmDetailController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AlarmDetailService alarmDetailService;

    @RequestMapping(value="add")
    public Result<String> add(@RequestBody AlarmDetailBean bean){
        logger.info("接收网络及心跳信息接口参数：{}", JSON.toJSON(bean));
    	return alarmDetailService.add(bean);
    }
}
