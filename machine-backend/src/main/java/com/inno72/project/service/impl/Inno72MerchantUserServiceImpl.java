package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Encrypt;
import com.inno72.common.JSR303Util;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.project.mapper.Inno72MerchantUserMapper;
import com.inno72.project.model.Inno72MerchantUser;
import com.inno72.project.service.Inno72MerchantUserService;
import com.inno72.redis.IRedisUtil;
import com.inno72.system.model.Inno72User;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantUserServiceImpl extends AbstractService<Inno72MerchantUser>
		implements Inno72MerchantUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72MerchantUserServiceImpl.class);

	@Resource
	private Inno72MerchantUserMapper inno72MerchantUserMapper;

	@Resource
	private IRedisUtil redisUtil;

	@Override
	public Result saveOrUpdate(Inno72MerchantUser user){
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		LOGGER.info("保存商户用户 user - {}, 操作用户 - {}", JSON.toJSONString(user), JSON.toJSONString(mUser));
		String id = user.getId();
		if (StringUtil.isEmpty(id)){
			//保存新用户
			Result<String> valid = JSR303Util.valid(user);
			if (valid.getCode() == Result.FAILURE){
				return valid;
			}
			user.setMerchantId(genMerchantCode());
			user.setCreateTime(LocalDateTime.now());
			user.setCreator(mUser.getName());
			user.setLastUpdateTime(LocalDateTime.now());
			user.setLastUpdator(mUser.getName());

			user.setId(StringUtil.getUUID());

			inno72MerchantUserMapper.insertSelective(user);
			LOGGER.info("保存用户 [{}] 完成", user);

		}else {
			user.setLastUpdateTime(LocalDateTime.now());
			user.setLastUpdator(mUser.getName());

			if (StringUtil.isNotEmpty(user.getPassword())){
				user.setPassword(Encrypt.md5AndSha(user.getPassword()));
			}
			inno72MerchantUserMapper.updateByPrimaryKeySelective(user);

		}
		return Results.success();
	}

	@Override
	public Result getCode() {
		return Results.success(genMerchantCode());
	}

	@Override
	public Result alterStatus(String id, String status) {
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)){
			return Results.failure("修改失败!");
		}
		Inno72MerchantUser inno72MerchantUser = inno72MerchantUserMapper.selectByPrimaryKey(id);
		if (inno72MerchantUser == null){
			return Results.failure("用户不存在");
		}
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		Inno72MerchantUser user = new Inno72MerchantUser();
		user.setLoginStatus(status);
		user.setLastUpdateTime(LocalDateTime.now());
		user.setLastUpdator(mUser.getName());
		user.setId(id);
		return Results.success();
	}

	private String genMerchantCode(){
		String yyyyMMdd = LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		String redisKye = CommonConstants.MERCHANT_CODE_REDIS_KEY + yyyyMMdd;
		StringBuilder merchantCode = new StringBuilder(redisUtil.incr(redisKye) + "");

		while(merchantCode.length() < 4){
			merchantCode.insert(0, "0");
		}
		return merchantCode.toString();
	}

}
