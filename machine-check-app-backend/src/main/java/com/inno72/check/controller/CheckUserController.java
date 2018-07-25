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
import java.io.UnsupportedEncodingException;
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

    /**
     * 加密
     * @param map
     * @return
     */
    @RequestMapping(value="/encrypt",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Result<String> encrypt(@RequestBody Map<String,Object> map){
        return ResultGenerator.genSuccessResult(AesUtils.encrypt(JSON.toJSONString(map)));
    }

    /**
     * 解密
     * @param map
     * @return
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
