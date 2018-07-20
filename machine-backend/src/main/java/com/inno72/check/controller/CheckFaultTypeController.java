package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.check.service.CheckFaultTypeService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/20.
*/
@RestController
@RequestMapping("/check/faultType")
@CrossOrigin
public class CheckFaultTypeController {
    @Resource
    private CheckFaultTypeService checkFaultTypeService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72CheckFaultType checkFaultType) {
        checkFaultTypeService.save(checkFaultType);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        checkFaultTypeService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72CheckFaultType checkFaultType) {
        checkFaultTypeService.update(checkFaultType);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72CheckFaultType> detail(@RequestParam String id) {
        Inno72CheckFaultType checkFaultType = checkFaultTypeService.findById(id);
        return ResultGenerator.genSuccessResult(checkFaultType);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestParam(required=false) String code,@RequestParam(required=false) String keyword) {
    	List<Inno72CheckFaultType> list = checkFaultTypeService.findByPage(keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
