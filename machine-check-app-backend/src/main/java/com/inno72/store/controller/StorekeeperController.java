package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.SessionData;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StorekeeperService;

import tk.mybatis.mapper.entity.Condition;

/**
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/storekeeper")
public class StorekeeperController {
    @Resource
    private StorekeeperService storekeeperService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

}
