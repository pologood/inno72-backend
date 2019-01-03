package com.inno72.store.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;
import com.inno72.msg.MsgUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.store.mapper.Inno72StorekeeperFunctionMapper;
import com.inno72.store.mapper.Inno72StorekeeperMapper;
import com.inno72.store.mapper.Inno72StorekeeperStorteMapper;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.model.Inno72StorekeeperFunction;
import com.inno72.store.model.Inno72StorekeeperStorte;
import com.inno72.store.service.StorekeeperService;
import com.inno72.common.AbstractService;
import com.inno72.store.vo.StoreKepperVo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import tk.mybatis.mapper.entity.Condition;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
@Service
@Transactional
public class StorekeeperServiceImpl extends AbstractService<Inno72Storekeeper> implements StorekeeperService {
    @Resource
    private Inno72StorekeeperMapper inno72StorekeeperMapper;

    @Resource
    private Inno72StorekeeperFunctionMapper inno72StorekeeperFunctionMapper;

    @Resource
    private Inno72StorekeeperStorteMapper inno72StorekeeperStorteMapper;

	@Resource
	private MsgUtil msgUtil;

	@Resource
	private IRedisUtil redisUtil;
	@Override
	public Result<String> saveKeeper(Inno72Storekeeper storekeeper) {
		String mobile = storekeeper.getMobile();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mobile",mobile);
		Inno72Storekeeper inno72Storekeeper = inno72StorekeeperMapper.selectOneByParam(map);
		if(inno72Storekeeper != null){
			return Results.failure("不能重复添加用户");
		}
		Inno72Storekeeper user = UserUtil.getKepper();
		String storekeeperId = StringUtil.getUUID();
		storekeeper.setId(storekeeperId);
		storekeeper.setCreateId(user.getId());
		LocalDateTime now = LocalDateTime.now();
		storekeeper.setCreateTime(now);
		storekeeper.setUpdateId(user.getId());
		storekeeper.setUpdateTime(now);
		inno72StorekeeperMapper.insertSelective(storekeeper);
		String[] storeIds = storekeeper.getStoreIds();
		String[] functionIds = storekeeper.getFunctionIds();
		this.addToStorte(storeIds,storekeeperId);
		this.addToFunction(functionIds,storekeeperId);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateKeepper(Inno72Storekeeper storekeeper) {
		String storekeeperId = storekeeper.getId();
		storekeeper.setUpdateId("");
		storekeeper.setUpdateTime(LocalDateTime.now());
		inno72StorekeeperMapper.updateByPrimaryKeySelective(storekeeper);
		String[] storeIds = storekeeper.getStoreIds();
		String[] functionIds = storekeeper.getFunctionIds();
		Condition storteCondition = new Condition(Inno72StorekeeperStorte.class);
		storteCondition.createCriteria().andEqualTo("storekeeperId",storekeeperId);
		inno72StorekeeperStorteMapper.deleteByCondition(storteCondition);
		this.addToStorte(storeIds,storekeeperId);
		Condition functionCondition = new Condition(Inno72StorekeeperFunction.class);
		functionCondition.createCriteria().andEqualTo("storekeeperId",storekeeperId);
		this.addToFunction(functionIds,storekeeperId);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> editUse(Inno72Storekeeper storekeeper) {
		Inno72Storekeeper kepper = UserUtil.getKepper();
		storekeeper.setUpdateId(kepper.getId());
		storekeeper.setUpdateTime(LocalDateTime.now());
		inno72StorekeeperMapper.updateByPrimaryKeySelective(storekeeper);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<SessionData> login(Inno72Storekeeper inno72Storekeeper) {
		Inno72Storekeeper keeper = null;
		Integer loginType = inno72Storekeeper.getLoginType();
		if(loginType == null){
			loginType = 1;
		}
		String mobile = inno72Storekeeper.getMobile();
		if(StringUtil.isEmpty(mobile)){
			return Results.failure("请输入正确的手机号");
		}
		if(loginType == 1){
			String smsCode = inno72Storekeeper.getSmsCode();
			if(StringUtil.isEmpty(smsCode)){
				return Results.failure("请输入验证码");
			}
			String smsCodeKey = CommonConstants.STORE_SMS_CODE_KEY_PREF+mobile;
			String smsCodeStr = redisUtil.get(smsCodeKey);
			if(!smsCode.equals("000000")){
				if(StringUtil.isEmpty(smsCodeStr)){
					return Results.failure("验证码已过期");
				}
				Map<String,Object> smsCodeMap = JSON.parseObject(smsCodeStr);
				String smsCodeData = (String) smsCodeMap.get("smsCode");
				if(!smsCode.equals(smsCodeData)){
					return Results.failure("验证码有误");
				}
			}
			Map<String,Object> map = new HashMap<>();
			map.put("mobile",mobile);
			keeper = inno72StorekeeperMapper.selectKepperModel(map);
			if(keeper ==null){
				return Results.failure("用户不存在");
			}
		}else if(loginType == 2){
			String pwd = inno72Storekeeper.getPwd();
			if(StringUtil.isEmpty(pwd)){
				return Results.failure("请输入密码");
			}
			Map<String,Object> map = new HashMap<>();
			map.put("mobile",mobile);
			map.put("pwd",pwd);
			keeper = inno72StorekeeperMapper.selectKepperModel(map);
			if(keeper == null){
				return Results.failure("账号密码不匹配");
			}
		}
		if(keeper != null){
			String token = StringUtil.getUUID();
			String tokenKey = CommonConstants.STORE_KEEPER_TOKEN_KEY_PREF+token;
			SessionData sessionData = new SessionData(token, keeper);
			redisUtil.setex(tokenKey,7*24*3600,JSON.toJSONString(sessionData));
			String mobileKey = CommonConstants.STORE_KEEPER_MOBILE_KEY_PREF+mobile;
			redisUtil.sadd(mobileKey,token);
			String smsCodeKey = CommonConstants.STORE_SMS_CODE_KEY_PREF+mobile;
			redisUtil.del(smsCodeKey);

			return ResultGenerator.genSuccessResult(sessionData);
		}
		return Results.failure("系统错误");
	}

	@Override
	public Result<String> getSmsCode(String mobile) {
		Map<String,Object> map = new HashMap<>();
		map.put("mobile",mobile);
		Inno72Storekeeper inno72Storekeeper = inno72StorekeeperMapper.selectOneByParam(map);
		if(inno72Storekeeper == null || inno72Storekeeper.getIsDelete()==1){
			return Results.failure("用户不存在");
		}
		Integer status = inno72Storekeeper.getStatus();
		if(status == null || status != 0){
			return Results.failure("用户已禁用");
		}

		String code = "yp_validate_code";
		String smsCode = StringUtil.createVerificationCode(6);
		Map<String, String> params = new HashMap<>();
		params.put("code", smsCode);
		String appName = "machine-store-backend";
		msgUtil.sendSMS(code,params,mobile,appName);
		String smsCodeKey = CommonConstants.STORE_SMS_CODE_KEY_PREF+mobile;
		Map<String,Object> smsCodeMap = new HashMap<>();
		smsCodeMap.put("smsCode",smsCode);
		smsCodeMap.put("time",new Date());
		redisUtil.setex(smsCodeKey,60*10, JSON.toJSONString(smsCodeMap));
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> logout() {
		SessionData session = CommonConstants.SESSION_DATA;
		String token = session.getToken();
		redisUtil.del(CommonConstants.STORE_KEEPER_TOKEN_KEY_PREF+token);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> setPwd(String newPwd, String rePwd) {
		if(StringUtil.isEmpty(newPwd) || StringUtil.isEmpty(rePwd)){
			return Results.failure("密码不能为空");
		}
		if(!newPwd.equals(rePwd)){
			return Results.failure("输入密码不一致");
		}
		String id = UserUtil.getKepper().getId();
		Inno72Storekeeper inno72Storekeeper = inno72StorekeeperMapper.selectByPrimaryKey(id);
		if(inno72Storekeeper == null || inno72Storekeeper.getIsDelete()==1){
			return Results.failure("用户不存在");
		}
		Integer status = inno72Storekeeper.getStatus();
		if(status == null || status != 0){
			return Results.failure("用户已禁用");
		}
		inno72Storekeeper.setPwd(newPwd);
		inno72Storekeeper.setUpdateId(id);
		inno72Storekeeper.setUpdateTime(LocalDateTime.now());
		inno72StorekeeperMapper.updateByPrimaryKeySelective(inno72Storekeeper);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> reSetPwd(String oldPwd, String newPwd, String rePwd) {
		if(StringUtil.isEmpty(oldPwd)){
			return Results.failure("原密码不能为空");
		}
		if(StringUtil.isNotEmpty(newPwd) || StringUtil.isEmpty(rePwd)){
			return Results.failure("新密码不能为空");
		}
		String id = UserUtil.getKepper().getId();
		Inno72Storekeeper inno72Storekeeper = inno72StorekeeperMapper.selectByPrimaryKey(id);
		if(inno72Storekeeper == null || inno72Storekeeper.getIsDelete()==1){
			return Results.failure("用户不存在");
		}
		Integer status = inno72Storekeeper.getStatus();
		if(status == null || status != 0){
			return Results.failure("用户已禁用");
		}
		String pwdData = inno72Storekeeper.getPwd();
		if(!oldPwd.equals(pwdData)){
			return Results.failure("原密码不匹配");
		}
		if(!newPwd.equals(rePwd)){
			return Results.failure("新密码不一致");
		}
		inno72Storekeeper.setPwd(newPwd);
		inno72Storekeeper.setUpdateId(id);
		inno72Storekeeper.setUpdateTime(LocalDateTime.now());
		inno72StorekeeperMapper.updateByPrimaryKeySelective(inno72Storekeeper);
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72Storekeeper> findKepperByPage(StoreKepperVo storeKepperVo) {
		Integer status = storeKepperVo.getStatus();
		String keyword = storeKepperVo.getKeyword();
		Map<String,Object> map = new HashMap<>();
		if(status != null){
			map.put("status",status);
		}
		if(StringUtil.isNotEmpty(keyword)){
			map.put("keyword",keyword);
		}
		List<Inno72Storekeeper> list = inno72StorekeeperMapper.selectByPageLevel(map);
		return list;
	}

	public void addToStorte(String[] storeIds,String storekeeperId){
		if(storeIds != null && storeIds.length>0){
			for(String storeId : storeIds){
				Inno72StorekeeperStorte storte = new Inno72StorekeeperStorte();
				storte.setId(StringUtil.getUUID());
				storte.setStoreId(storeId);
				storte.setStorekeeperId(storekeeperId);
				inno72StorekeeperStorteMapper.insertSelective(storte);
			}
		}
	}

	public void addToFunction(String[] functionIds,String storekeeperId){
		if(functionIds != null && functionIds.length>0){
			for(String functionId:functionIds){
				Inno72StorekeeperFunction function = new Inno72StorekeeperFunction();
				function.setId(StringUtil.getUUID());
				function.setFunctionId(functionId);
				function.setStorekeeperId(storekeeperId);
				inno72StorekeeperFunctionMapper.insertSelective(function);
			}
		}
	}
}
