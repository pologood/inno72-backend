package com.inno72.store.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.store.model.Inno72StorekeeperStorte;
import com.inno72.store.service.StorekeeperStorteService;
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
@RequestMapping("/storekeeper/storte")
public class StorekeeperStorteController {
    @Resource
    private StorekeeperStorteService storekeeperStorteService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72StorekeeperStorte storekeeperStorte) {
        storekeeperStorteService.save(storekeeperStorte);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        storekeeperStorteService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72StorekeeperStorte storekeeperStorte) {
        storekeeperStorteService.update(storekeeperStorte);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72StorekeeperStorte> detail(@RequestParam String id) {
        Inno72StorekeeperStorte storekeeperStorte = storekeeperStorteService.findById(id);
        return ResultGenerator.genSuccessResult(storekeeperStorte);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72StorekeeperStorte.class);
        List<Inno72StorekeeperStorte> list = storekeeperStorteService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
