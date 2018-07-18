package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.common.Results;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/18.
*/
@RestController
@RequestMapping("/check/user")
public class CheckUserController {
    @Resource
    private CheckUserService checkUserService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@RequestBody Inno72CheckUserVo checkUser) {
    	try {
    		return checkUserService.saveModel(checkUser);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
    		return checkUserService.delById(id);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(@RequestBody Inno72CheckUserVo checkUser) {
    	try {
    		return checkUserService.updateModel(checkUser);
		} catch (Exception e) {
			return Results.failure("操作失败");
		}
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72CheckUser> detail(@RequestParam String id) {
        Inno72CheckUser checkUser = checkUserService.findById(id);
        return ResultGenerator.genSuccessResult(checkUser);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestParam(required=false) String keyword) {
        List<Inno72CheckUser> list = checkUserService.findByPage(keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
