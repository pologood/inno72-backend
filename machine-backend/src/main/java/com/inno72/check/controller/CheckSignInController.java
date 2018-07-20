package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
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
* Created by CodeGenerator on 2018/07/20.
*/
@RestController
@RequestMapping("/check/sign/in")
public class CheckSignInController {
    @Resource
    private CheckSignInService checkSignInService;

    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72CheckSignIn> detail(@RequestParam String id) {
        Inno72CheckSignIn checkSignIn = checkSignInService.findById(id);
        return ResultGenerator.genSuccessResult(checkSignIn);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72CheckSignIn.class);
        List<Inno72CheckSignIn> list = checkSignInService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
