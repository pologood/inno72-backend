package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.common.Result;
import com.inno72.common.SessionData;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RequestMapping(value = "/check/user")
@RestController
@CrossOrigin
public class CheckUserController {

    @Resource
    private CheckUserService checkUserService;

    /**
     * 验证码
     * @param phone
     * @return
     */
    @RequestMapping(value="/smsCode")
    public Result smsCode(String phone){
        Result<String> result = checkUserService.smsCode(phone);
        return result;
    }

    /**
     * 登录
     * @param phone
     * @param smsCode
     * @return
     */
    @RequestMapping(value="/login")
    public Result<SessionData> login(String phone,String smsCode){
        Result<SessionData> result = checkUserService.login(phone,smsCode);
        return result;
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @RequestMapping(value="/upload")
    public Result<String> upload(MultipartFile file){
        Result<String> result = checkUserService.upload(file);
        return result;
    }

    /**
     * 编辑用户
     * @param inno72CheckUser
     * @return
     */
    @RequestMapping(value="/update",method = {RequestMethod.POST,RequestMethod.GET})
    public Result<Inno72CheckUser> update(Inno72CheckUser inno72CheckUser){
        Result<Inno72CheckUser> result = checkUserService.updateUser(inno72CheckUser);
        return result;
    }
}
