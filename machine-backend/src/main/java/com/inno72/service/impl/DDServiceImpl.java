package com.inno72.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.model.Inno72Dept;
import com.inno72.model.Inno72User;
import com.inno72.model.Inno72UserDept;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.DDService;
import com.inno72.service.DeptService;
import com.inno72.service.UserDeptService;
import com.inno72.service.UserService;
import com.inno72.util.dd.DingTalkEncryptException;
import com.inno72.util.dd.DingTalkEncryptor;
import com.inno72.vo.UserDeptVo;

@Service
public class DDServiceImpl implements DDService {
	Logger logger = LoggerFactory.getLogger(DDServiceImpl.class);
	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDeptService userDeptService;

	@Override
	public String process(String data, String signature, String timestamp, String nonce) {

		JSONObject jsonEncrypt = JSONObject.parseObject(data);
		String encrypt = jsonEncrypt.getString("encrypt");
		DingTalkEncryptor dingTalkEncryptor = null;
		String plainText = null;
		try {
			dingTalkEncryptor = new DingTalkEncryptor(CommonConstants.DD_TOKEN, CommonConstants.AES_KEY,
					CommonConstants.DD_CORPID);
			plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encrypt);
		} catch (DingTalkEncryptException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		JSONObject plainTextJson = JSONObject.parseObject(plainText);
		String eventType = plainTextJson.getString("EventType");
		logger.info(plainText);
		String result = null;

		switch (eventType) {
		case "user_add_org":// 新增用户
			logger.info("钉钉新增用户回调");
			plainTextJson.getJSONArray("UserId").forEach(userId -> {
				Result<Inno72User> userResult = userService.getUserByUserId(userId.toString());
				Inno72User user_db = userResult.getData();
				UserDeptVo userVo = buidUserById(userId.toString());
				if (user_db == null) {
					Inno72User user = userVo.getUser();
					user.setId(StringUtil.getUUID());
					userService.save(user);

					userVo.getDeptIds().forEach(dept -> {
						Inno72UserDept ud = new Inno72UserDept();
						ud.setId(StringUtil.getUUID());
						ud.setDeptId(dept.toString());
						ud.setUserId(user.getId());
						userDeptService.save(ud);
					});
				}

			});
			result = "success";
			break;
		case "user_modify_org":// 修改用户
			logger.info("钉钉修改用户回调");
			plainTextJson.getJSONArray("UserId").forEach(userId -> {
				Result<Inno72User> userResult = userService.getUserByUserId(userId.toString());
				Inno72User user_db = userResult.getData();
				UserDeptVo userVo = buidUserById(userId.toString());
				if (user_db != null) {

					Inno72User user = userVo.getUser();
					user.setId(user_db.getId());
					userService.update(user);

					userDeptService.deleteByUserId(user_db.getId());
					userVo.getDeptIds().forEach(dept -> {
						Inno72UserDept ud = new Inno72UserDept();
						ud.setId(StringUtil.getUUID());
						ud.setDeptId(dept.toString());
						ud.setUserId(user.getId());
						userDeptService.save(ud);
					});
				}

			});
			result = "success";
			break;
		case "user_leave_org":// 用户离职
			logger.info("钉钉离职用户回调");
			plainTextJson.getJSONArray("UserId").forEach(userId -> {
				Result<Inno72User> userResult = userService.getUserByUserId(userId.toString());
				Inno72User user_db = userResult.getData();
				if (user_db != null) {
					user_db.setIsDelete(1);
					userService.update(user_db);
					userDeptService.deleteByUserId(user_db.getId());
				}

			});
			result = "success";
			break;

		case "org_dept_create":// 创建部门
			logger.info("钉钉新增部门回调");
			plainTextJson.getJSONArray("DeptId").forEach(deptId -> {
				Inno72Dept dept = buidDeptById(deptId.toString());
				deptService.save(dept);
			});
			result = "success";
			break;

		case "org_dept_modify":// 修改部门
			logger.info("钉钉修改部门回调");
			plainTextJson.getJSONArray("DeptId").forEach(deptId -> {
				deptService.deleteById(deptId.toString());
				Inno72Dept dept = buidDeptById(deptId.toString());
				deptService.save(dept);
			});
			result = "success";
			break;

		case "org_dept_remove":// 删除部门
			logger.info("钉钉删除部门回调");
			plainTextJson.getJSONArray("DeptId").forEach(deptid -> {
				deptService.deleteById(deptid.toString());
			});
			result = "success";
			break;
		case "check_url":// 回调接口注册验证
			result = "success";
		default: // do something
			result = "success";
			break;
		}
		long timeStampLong = Long.parseLong(timestamp);
		Map<String, String> jsonMap = null;
		try {
			jsonMap = dingTalkEncryptor.getEncryptedMap(result, timeStampLong, nonce);
		} catch (DingTalkEncryptException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.putAll(jsonMap);
		return json.toString();
	}

	@Override
	public Result<String> getToken() {
		String url = "https://oapi.dingtalk.com/gettoken?corpid=dingd04d2d6ca18d0fd535c2f4657eb6378f&corpsecret=2ralypy62nV4kL8DOMjWWEoJyQkFnjNhlin3PzdkIMs1LQ7jj8huTsqibi7UdaKD";
		String result = HttpClient.get(url);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return ResultGenerator.genSuccessResult(resultJson.getString("access_token"));
		}
		return ResultGenerator.genFailResult(resultJson.getString("errmsg"));
	}

	@Override
	public Result<String> registryCallback(String url) {
		String callback = "http://47.95.217.215:30901/dd";
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return ResultGenerator.genFailResult("获取token失败");
		}
		String token = tokenResult.getData();
		String api = "https://oapi.dingtalk.com/call_back/register_call_back?access_token=" + token;
		String[] tags = { "user_add_org", "user_modify_org", "user_leave_org" };
		JSONObject json = new JSONObject();
		json.put("call_back_tag", tags);
		json.put("token", CommonConstants.DD_TOKEN);
		json.put("aes_key", CommonConstants.AES_KEY);
		json.put("url", callback);

		String $j = HttpClient.post(api, json.toJSONString());
		JSONObject $json = JSON.parseObject($j);
		String errcode = $json.getString("errcode");
		if (!errcode.equals("0")) {
			return ResultGenerator.genFailResult($json.getString("errmsg"));
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateRegistryCallback(String url) {
		String callback = "http://47.95.217.215:30901/dd";
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return ResultGenerator.genFailResult("获取token失败");
		}
		String token = tokenResult.getData();
		String[] tags = { "user_add_org", "user_modify_org", "user_leave_org", "org_dept_create", "org_dept_modify",
				"org_dept_remove" };
		String api = "https://oapi.dingtalk.com/call_back/update_call_back?access_token=" + token;
		JSONObject json = new JSONObject();
		json.put("call_back_tag", tags);
		json.put("token", CommonConstants.DD_TOKEN);
		json.put("aes_key", CommonConstants.AES_KEY);
		json.put("url", callback);

		String $j = HttpClient.post(api, json.toJSONString());
		JSONObject $json = JSON.parseObject($j);
		String errcode = $json.getString("errcode");
		if (!errcode.equals("0")) {
			return ResultGenerator.genFailResult($json.getString("errmsg"));
		}
		return ResultGenerator.genSuccessResult();

	}

	@Override
	public Result<String> initDData() {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return ResultGenerator.genFailResult("获取token失败");
		}
		String token = tokenResult.getData();
		// 初始化部门数据 先删除所有部门
		deptService.deleteAll();
		userDeptService.deleteAll();
		String deptUrl = MessageFormat.format("https://oapi.dingtalk.com/department/list?access_token={0}", token);
		String dept_result = HttpClient.get(deptUrl);
		JSONObject deptJson = JSON.parseObject(dept_result);
		JSONArray dept_ary = deptJson.getJSONArray("department");
		dept_ary.forEach(dept -> {
			JSONObject dept_info = JSON.parseObject(dept.toString());
			String deptId = dept_info.getString("id");
			Inno72Dept inno72Dept = buidDeptById(deptId);
			if (inno72Dept != null) {
				deptService.save(inno72Dept);
			}
			// 初始化用户
			initUser(deptId, token);
		});
		return ResultGenerator.genSuccessResult();
	}

	private void initUser(String deptId, String token) {
		String dept_user_url = MessageFormat
				.format("https://oapi.dingtalk.com/user/list?access_token={0}&department_id={1}", token, deptId);
		String dept_user_result = HttpClient.get(dept_user_url);
		JSONObject dept_user_json = JSON.parseObject(dept_user_result);
		JSONArray user_ary = dept_user_json.getJSONArray("userlist");
		user_ary.forEach(user_info -> {
			// 解析用户数据
			JSONObject $user = JSON.parseObject(user_info.toString());
			// 根据userid查询本地库,有数据为修改事件，没数据为新增事件
			Result<Inno72User> userResult = userService.getUserByUserId($user.getString("userid"));
			Inno72User user = userResult.getData();
			if (user == null) {
				user = new Inno72User();
				String id = StringUtil.getUUID();
				user.setId(id);
				user.setAvatar($user.getString("avatar"));
				user.setCreateTime(LocalDateTime.now());
				user.setEmail($user.getString("email"));
				user.setMobile($user.getString("mobile"));
				user.setName($user.getString("name"));
				user.setOrgEmail($user.getString("orgEmail"));
				user.setPosition($user.getString("position"));
				user.setUserId($user.getString("userid"));
				user.setIsDelete(0);
				userService.save(user);

			}
			Inno72UserDept ud = new Inno72UserDept();
			ud.setId(StringUtil.getUUID());
			ud.setDeptId(deptId);
			ud.setUserId(user.getId());
			userDeptService.save(ud);
		});
	}

	public Inno72Dept buidDeptById(String deptId) {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return null;
		}
		String token = tokenResult.getData();
		String deptDetailUrl = MessageFormat.format("https://oapi.dingtalk.com/department/get?access_token={0}&id={1}",
				token, deptId);
		String json = HttpClient.get(deptDetailUrl);
		JSONObject $json = JSON.parseObject(json);
		Inno72Dept inno72Dept = new Inno72Dept();
		inno72Dept.setId($json.getString("id"));
		inno72Dept.setName($json.getString("name"));
		inno72Dept.setSeq($json.getInteger("order"));
		inno72Dept.setParentId($json.getString("parentid"));
		return inno72Dept;
	}

	public UserDeptVo buidUserById(String userId) {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return null;
		}
		String token = tokenResult.getData();
		String userDetailUrl = MessageFormat.format("https://oapi.dingtalk.com/user/get?access_token={0}&userid={1}",
				token, userId);
		String json = HttpClient.get(userDetailUrl);
		JSONObject $user = JSON.parseObject(json);
		Inno72User user = new Inno72User();
		user.setAvatar($user.getString("avatar"));
		user.setCreateTime(LocalDateTime.now());
		user.setEmail($user.getString("email"));
		user.setMobile($user.getString("mobile"));
		user.setName($user.getString("name"));
		user.setOrgEmail($user.getString("orgEmail"));
		user.setPosition($user.getString("position"));
		user.setUserId($user.getString("userid"));
		user.setIsDelete(0);
		UserDeptVo vo = new UserDeptVo();
		vo.setUser(user);
		vo.setDeptIds($user.getJSONArray("department"));
		return vo;
	}
}
