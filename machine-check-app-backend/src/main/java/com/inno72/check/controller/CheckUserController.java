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
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RequestMapping(value = "/check/user")
@RestController
@CrossOrigin
public class CheckUserController {

    @Resource
    private CheckUserService checkUserService;

    /**
     * 验证码
     */
    @RequestMapping(value="/smsCode", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result smsCode(@RequestBody Inno72CheckUser inno72CheckUser){
        return checkUserService.smsCode(inno72CheckUser.getPhone());
    }

    /**
     * 登录
     */
    @RequestMapping(value="/login", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<SessionData> login(@RequestBody Inno72CheckUser inno72CheckUser){
        return checkUserService.login(inno72CheckUser.getPhone(),inno72CheckUser.getSmsCode());
    }

    /**
     * 上传头像
     */
    @RequestMapping(value="/upload")
    public Result<String> upload(MultipartFile file){
        return checkUserService.upload(file);
    }

    /**
     * 编辑用户
     */
    @RequestMapping(value="/update",method = {RequestMethod.POST,RequestMethod.GET})
    public Result<Inno72CheckUser> update(@RequestBody Inno72CheckUser inno72CheckUser){
        return checkUserService.updateUser(inno72CheckUser);
    }

    /**
     * 加密
     */
    @RequestMapping(value="/encrypt",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result<String> encrypt(@RequestBody Map<String,Object> map){
        return ResultGenerator.genSuccessResult(AesUtils.encrypt(JSON.toJSONString(map)));
    }

    /**
     * 解密
     */
    @RequestMapping(value="decrypt")
    public Result<String> decrypt(@RequestBody Map<String,Object> map){
        String data = map.get("data").toString();
        String decryptData = AesUtils.decrypt(data);
        String result = null;
        try {
           result =  new String(decryptData.getBytes("UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult(result);
    }
}
