package com.inno72.store.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StorekeeperService;
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
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/storekeeper")
public class StorekeeperController {
    @Resource
    private StorekeeperService storekeeperService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Storekeeper storekeeper) {
        storekeeperService.save(storekeeper);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        storekeeperService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Storekeeper storekeeper) {
        storekeeperService.update(storekeeper);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Storekeeper> detail(@RequestParam String id) {
        Inno72Storekeeper storekeeper = storekeeperService.findById(id);
        return ResultGenerator.genSuccessResult(storekeeper);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72Storekeeper.class);
        List<Inno72Storekeeper> list = storekeeperService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
