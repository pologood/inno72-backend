package com.inno72.project.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.inno72.project.vo.Inno72MerchantUserVo;
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

		LOGGER.info("---------------------保存或修改商户-------------------");

		LOGGER.info("user parameter -> {}", JSON.toJSONString(user));

		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			return Results.failure("未找到用户登录信息");
		}
		LOGGER.info("保存商户用户 user - {}, 操作用户 - {}", JSON.toJSONString(user), JSON.toJSONString(mUser));
		String id = user.getId();
		if (StringUtil.isEmpty(id)){
			//保存新用户
			Result<String> valid = JSR303Util.valid(user);
			if (valid.getCode() == Result.FAILURE){
				return valid;
			}
			if (user.getLoginStatus().equals("1")){
				if (StringUtil.isEmpty(user.getPhone()) || StringUtil.isEmpty(user.getLoginName())){
					return Results.failure("请设置用户手机号码!");
				}
				int loginNameSize = inno72MerchantUserMapper.selectByLoginName(user.getLoginName());
				if (loginNameSize > 0){
					return Results.failure("登录账号重复!");
				}
				user.setPassword(Encrypt.md5AndSha(user.getPhone()));
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

			Inno72MerchantUser curUser = inno72MerchantUserMapper.selectByPrimaryKey(id);
			if (curUser == null){
				return Results.failure("用户不存在!");
			}

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

		LOGGER.info("---------------------修改商户状态-------------------");

		LOGGER.info("id status parameter -> {} {}", id, status);

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
		inno72MerchantUserMapper.updateByPrimaryKeySelective(user);
		return Results.success();
	}

	@Override
	public List<Inno72MerchantUserVo> findByPage(String keyword) {

		LOGGER.info("---------------------查询商户列表-------------------");

		LOGGER.info("keyword parameter -> {}", keyword);

		Map<String, Object> params = new HashMap<>();
		keyword = Optional.ofNullable(keyword).map(a -> a.replace("'", "")).orElse(keyword);
		params.put("keyword", keyword);

		return inno72MerchantUserMapper.selectByPage(params);
	}

	@Override
	public Result resetPwd(Inno72MerchantUser user) {

		LOGGER.info("---------------------修改商户密码-------------------");

		LOGGER.info("user parameter -> {}", JSON.toJSONString(user));

		if (user == null || StringUtil.isEmpty(user.getId())){
			return Results.failure("参数不全!");
		}

		Inno72MerchantUser curUser = inno72MerchantUserMapper.selectByPrimaryKey(user.getId());
		if (curUser == null){
			return Results.failure("用户不存在!");
		}
		SessionData session = SessionUtil.sessionData.get();
		Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
		if (mUser == null) {
			return Results.failure("未找到用户登录信息");
		}
		if (StringUtil.isEmpty(curUser.getLoginName()) || StringUtil.isEmpty(curUser.getPhone())){
			return Results.failure("请设置登录名和相关手机号码后再重置密码!");
		}
		curUser.setPassword(Encrypt.md5AndSha(curUser.getPhone()));
		curUser.setLastUpdator(mUser.getName());
		curUser.setLastUpdateTime(LocalDateTime.now());

		inno72MerchantUserMapper.updateByPrimaryKeySelective(curUser);

		return Results.success();
	}

	private String genMerchantCode(){
		String yyyyMMdd = LocalDateTimeUtil.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		String redisKye = CommonConstants.MERCHANT_CODE_REDIS_KEY + yyyyMMdd;
		StringBuilder merchantCode = new StringBuilder(redisUtil.incr(redisKye) + "");

		while(merchantCode.length() < 4){
			merchantCode.insert(0, "0");
		}
		return yyyyMMdd + merchantCode.toString();
	}

}
