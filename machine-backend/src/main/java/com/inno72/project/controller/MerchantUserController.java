package com.inno72.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.project.model.Inno72MerchantUser;
import com.inno72.project.service.Inno72MerchantUserService;
import com.inno72.project.vo.Inno72MerchantUserVo;

/**
 * Created by CodeGenerator on 2018/11/07.
 */
@RestController
@RequestMapping
@CrossOrigin
public class MerchantUserController {

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
	 * 保存客户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/resetPwd")
	public Result resetPwd(Inno72MerchantUser user){
		return inno72MerchantUserService.resetPwd(user);
	}

	/**
	 * 查询详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/detail")
	public Result detail(String id){
		if (StringUtil.isEmpty(id)){
			return Results.failure("参数错误!");
		}
		Inno72MerchantUser byId = inno72MerchantUserService.findById(id);
		if (byId == null){
			return Results.failure("不存在的用户!");
		}
		return Results.success(byId);
	}

	/**
	 * 账户停用启用
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/alterStatus")
	public Result alterStatus(String id, String status){
		return inno72MerchantUserService.alterStatus(id, status);
	}


	/**
	 * 查询商户列表
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/getList")
	public Result<List<Inno72MerchantUser>> getList(Inno72MerchantUser user){
		return inno72MerchantUserService.getList(user);
	}

	/**
	 * 分页查询
	 *
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/inno72/merchant/user/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public ModelAndView list(@RequestParam(required=false) String keyword) {
		List<Inno72MerchantUserVo> list = inno72MerchantUserService.findByPage(keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
