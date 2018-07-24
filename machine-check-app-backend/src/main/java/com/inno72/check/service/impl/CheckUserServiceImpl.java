package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckFaultImageMapper;
import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultImage;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckFaultService;
import com.inno72.check.service.CheckUserService;
import com.inno72.common.*;
import com.inno72.common.json.JsonUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("checkUserService")
public class  CheckUserServiceImpl extends AbstractService<Inno72CheckUser> implements CheckUserService {

    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;

    @Autowired
    private MsgUtil msgUtil;

    @Resource
    private IRedisUtil redisUtil;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Result<String> smsCode(String phone) {
        Condition condition = new Condition(Inno72CheckUser.class);
        condition.createCriteria().andEqualTo("phone",phone);
        List<Inno72CheckUser> userList = inno72CheckUserMapper.selectByCondition(condition);
        if(userList == null || userList.size()==0){
            return Results.failure("用户不存在");
        }
        String code = "yp_validate_code";
        Map<String, String> params = new HashMap<>();
        String key = CommonConstants.SMS_CODE_KEY+phone;
        String smsCode = redisUtil.get(key);
        params.put("code", smsCode);
        String appName ="machine-check-app-backend";
        if(StringUtil.isEmpty(smsCode)){
            smsCode = StringUtil.createVerificationCode(6);
            redisUtil.setex(key,60*5,smsCode);
        }
        msgUtil.sendSMS(code, params, phone, appName);
        logger.info(key+"验证码为"+smsCode);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<SessionData> login(String phone, String smsCode) {
        Condition condition = new Condition(Inno72CheckUser.class);
        condition.createCriteria().andEqualTo("phone", phone);
        List<Inno72CheckUser> users = inno72CheckUserMapper.selectByCondition(condition);
        if (users == null || users.size() != 1) {
            return Results.failure("用户不存在");
        }
        String smsCodeValue = redisUtil.get(CommonConstants.SMS_CODE_KEY+phone);
        if(StringUtil.isEmpty(smsCodeValue)){
            return Results.failure("验证码已过期");
        }else if(!smsCodeValue.equals(smsCode)){
            return Results.failure("验证码不正确");
        }
        Inno72CheckUser user = users.get(0);
        String token = StringUtil.getUUID();
        SessionData sessionData = new SessionData(token, user);
        // 获取用户token使用
        String userTokenKey = CommonConstants.USER_LOGIN_TOKEN_CACHE_KEY_PREF + user.getId();
        // 获取用户之前登录的token
        String oldToken = redisUtil.get(userTokenKey);
        // 清除之前的登录信息
        if (StringUtil.isNotBlank(oldToken)) {
            redisUtil.del(CommonConstants.USER_LOGIN_CACHE_KEY_PREF + oldToken);
            // 记录被踢出
            redisUtil.sadd(CommonConstants.CHECK_OUT_USER_TOKEN_SET_KEY, oldToken);
            redisUtil.del(CommonConstants.SMS_CODE_KEY+phone);
        }
        // 保存新登录的token
        redisUtil.set(userTokenKey, token);
        // 用户登录信息缓存
        String userInfoKey = CommonConstants.USER_LOGIN_CACHE_KEY_PREF + token;
        // 缓存用户登录sessionData
        redisUtil.set(userInfoKey, JsonUtil.toJson(sessionData));
        redisUtil.expire(userInfoKey, CommonConstants.SESSION_DATA_EXP);
        redisUtil.expire(userTokenKey, CommonConstants.SESSION_DATA_EXP);
        return Results.success(sessionData);
    }

    @Override
    public Result<String> upload(MultipartFile file) {
        return UploadUtil.uploadImage(file,"headImage");
    }

    @Override
    public Result<Inno72CheckUser> updateUser(Inno72CheckUser inno72CheckUser) {
        inno72CheckUser.setId(UserUtil.getUser().getId());
        mapper.updateByPrimaryKeySelective(inno72CheckUser);
        inno72CheckUser = mapper.selectByPrimaryKey(inno72CheckUser.getId());
        return ResultGenerator.genSuccessResult(inno72CheckUser);
    }

}
