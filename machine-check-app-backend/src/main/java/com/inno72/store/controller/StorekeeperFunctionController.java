package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.store.model.Inno72StorekeeperFunction;
import com.inno72.store.service.StorekeeperFunctionService;

import tk.mybatis.mapper.entity.Condition;

/**
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/storekeeper/function")
public class StorekeeperFunctionController {
    @Resource
    private StorekeeperFunctionService storekeeperFunctionService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72StorekeeperFunction storekeeperFunction) {
        storekeeperFunctionService.save(storekeeperFunction);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        storekeeperFunctionService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72StorekeeperFunction storekeeperFunction) {
        storekeeperFunctionService.update(storekeeperFunction);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72StorekeeperFunction> detail(@RequestParam String id) {
        Inno72StorekeeperFunction storekeeperFunction = storekeeperFunctionService.findById(id);
        return ResultGenerator.genSuccessResult(storekeeperFunction);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72StorekeeperFunction.class);
        List<Inno72StorekeeperFunction> list = storekeeperFunctionService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
