package com.inno72.check.controller;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.service.CheckFaultService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import tk.mybatis.mapper.entity.Condition;


import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/19.
*/
@RestController
@RequestMapping("/check/fault")
public class CheckFaultController {
    @Resource
    private CheckFaultService checkFaultService;

    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72CheckFault checkFault) {
        checkFaultService.update(checkFault);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72CheckFault> detail(@RequestParam String id) {
        Inno72CheckFault checkFault = checkFaultService.findById(id);
        return ResultGenerator.genSuccessResult(checkFault);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72CheckFault.class);
        List<Inno72CheckFault> list = checkFaultService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
