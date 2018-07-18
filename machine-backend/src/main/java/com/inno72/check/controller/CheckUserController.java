package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckUserService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;


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
    public Result<String> add(Inno72CheckUser checkUser) {
        checkUserService.save(checkUser);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        checkUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72CheckUser checkUser) {
        checkUserService.update(checkUser);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72CheckUser> detail(@RequestParam String id) {
        Inno72CheckUser checkUser = checkUserService.findById(id);
        return ResultGenerator.genSuccessResult(checkUser);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72CheckUser.class);
        List<Inno72CheckUser> list = checkUserService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
