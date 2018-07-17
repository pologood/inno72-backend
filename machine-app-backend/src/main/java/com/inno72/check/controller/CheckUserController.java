package com.inno72.check.controller;

import com.inno72.check.service.CheckUserService;
import com.inno72.common.Result;
import com.inno72.common.SessionData;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping(value = "/check/user")
@RestController
@CrossOrigin
public class CheckUserController {

    @Resource
    private CheckUserService checkUserService;

    @RequestMapping(value="/smsCode")
    public Result smsCode(String phone){
        Result<String> result = checkUserService.smsCode(phone);
        return result;
    }

    @RequestMapping(value="/login")
    public Result<SessionData> login(String phone,String smsCode){
        Result<SessionData> result = checkUserService.login(phone,smsCode);
        return result;
    }
}
