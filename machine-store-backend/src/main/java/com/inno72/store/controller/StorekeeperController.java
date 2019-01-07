package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.SessionData;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StorekeeperService;
import com.inno72.store.vo.StoreKepperVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@RestController
@RequestMapping("/storekeeper")
@CrossOrigin
public class StorekeeperController {
	@Resource
	private StorekeeperService storekeeperService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody Inno72Storekeeper storekeeper) {
		logger.info("添加库存管理人员接口参数:{}", JSON.toJSON(storekeeper));
		Result<String> result = storekeeperService.saveKeeper(storekeeper);
		logger.info("添加库存管理人员接口返回结果:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		storekeeperService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(@RequestBody Inno72Storekeeper storekeeper) {
		logger.info("修改库存管理人员接口参数:{}", JSON.toJSON(storekeeper));
		Result<String> result = storekeeperService.updateKeepper(storekeeper);
		logger.info("修改库存管理人员接口返回数据:{}", JSON.toJSON(result));
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Storekeeper> detail(@RequestParam String id) {
		logger.info("查询库存管理人员详情接口参数:{}", JSON.toJSON(id));
		Inno72Storekeeper storekeeper = storekeeperService.findDetail(id);
		logger.info("查询库存管理人员详情接口返回数据:{}", JSON.toJSON(storekeeper));
		return ResultGenerator.genSuccessResult(storekeeper);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(StoreKepperVo storeKepperVo) {
		logger.info("分页查询库存管理人员接口参数:{}", JSON.toJSON(storeKepperVo));
		List<Inno72Storekeeper> list = storekeeperService.findKepperByPage(storeKepperVo);
		ModelAndView result = ResultPages.page(ResultGenerator.genSuccessResult(list));
		logger.info("分页查询库存管理人员接口返回数据:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/editUse")
	public Result<String> editUse(Inno72Storekeeper storekeeper) {
		logger.info("库存管理人员启用禁用接口参数:{}", JSON.toJSON(storekeeper));
		Result<String> result = storekeeperService.editUse(storekeeper);
		logger.info("库存管理人员启用禁用接口返回数据:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/smsCode")
	public Result<String> smsCode(String mobile) {
		logger.info("库存管理人员获取验证码接口参数:{}", JSON.toJSON(mobile));
		Result<String> result = storekeeperService.getSmsCode(mobile);
		logger.info("库存管理人员获取验证码接口返回数据:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/login")
	public Result<SessionData> login(Inno72Storekeeper inno72Storekeeper) {
		logger.info("库存管理人员登录接口参数:{}", JSON.toJSON(inno72Storekeeper));
		Result<SessionData> result = storekeeperService.login(inno72Storekeeper);
		logger.info("库存管理人员登录接口返回数据:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/logout")
	public Result<String> logout() {
		logger.info("库存管理人员退出登录接口:{}");
		Result<String> result = storekeeperService.logout();
		logger.info("库存管理人员退出登录返回数据:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/setPwd")
	public Result<String> setPwd(String newPwd, String rePwd) {
		logger.info("库存管理人员设置密码接口参数:" + "newPwd=" + newPwd + ",rePwd=" + rePwd);
		Result<String> result = storekeeperService.setPwd(newPwd, rePwd);
		logger.info("库存管理人员设置密码返回数据:{}", JSON.toJSON(result));
		return result;
	}

	@RequestMapping(value = "/reSetPwd")
	public Result<String> reSetPwd(String oldPwd, String newPwd, String rePwd) {
		logger.info("库存管理人员重置密码接口参数:oldPwd=" + oldPwd + ",newPwd=" + newPwd + ",rePwd=" + rePwd);
		Result<String> result = storekeeperService.reSetPwd(oldPwd, newPwd, rePwd);
		logger.info("库存管理人员重置密码返回数据:{}", JSON.toJSON(result));
		return result;
	}

}
