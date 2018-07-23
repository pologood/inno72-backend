package com.inno72.check.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.common.AesUtils;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.SessionData;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/check/user")
@RestController
@CrossOrigin
public class CheckUserController {

    @Resource
    private CheckUserService checkUserService;

    /**
     * 验证码
     * @param
     * @return
     */
    @RequestMapping(value="/smsCode", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result smsCode(@RequestBody Inno72CheckUser inno72CheckUser){
        Result<String> result = checkUserService.smsCode(inno72CheckUser.getPhone());
        return result;
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping(value="/login", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<SessionData> login(@RequestBody Inno72CheckUser inno72CheckUser){
        Result<SessionData> result = checkUserService.login(inno72CheckUser.getPhone(),inno72CheckUser.getSmsCode());
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
    public Result<Inno72CheckUser> update(@RequestBody Inno72CheckUser inno72CheckUser){
        Result<Inno72CheckUser> result = checkUserService.updateUser(inno72CheckUser);
        return result;
    }

    @RequestMapping(value="/jiami",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result<String> jiami(@RequestParam(name="data",required = false) String data){
        return ResultGenerator.genSuccessResult(AesUtils.encrypt(data));
    }
}
