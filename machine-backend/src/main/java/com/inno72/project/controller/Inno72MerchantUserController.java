package com.inno72.project.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.project.model.Inno72MerchantUser;
import com.inno72.project.service.Inno72MerchantUserService;

/**
 * Created by CodeGenerator on 2018/11/07.
 */
@RestController
@RequestMapping
public class Inno72MerchantUserController {

	@Resource
	private Inno72MerchantUserService inno72MerchantUserService;


	/**
	 * 保存客户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/save")
	public Result save(Inno72MerchantUser user){
		return inno72MerchantUserService.saveOrUpdate(user);
	}

	/**
	 * 账户停用启用
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/alterStatus")
	public Result alterStatus(String id, String status){
		return inno72MerchantUserService.alterStatus(id, status);
	}
}
