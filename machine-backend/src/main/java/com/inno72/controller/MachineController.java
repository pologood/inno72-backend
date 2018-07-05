package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Machine;
import com.inno72.service.MachineService;
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
* Created by CodeGenerator on 2018/06/29.
*/
@RestController
@RequestMapping("/machine")
public class MachineController {
    @Resource
    private MachineService machineService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Machine machine) {
        machineService.save(machine);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        machineService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Machine machine) {
        machineService.update(machine);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Machine> detail(@RequestParam String id) {
        Inno72Machine machine = machineService.findById(id);
        return ResultGenerator.genSuccessResult(machine);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72Machine.class);
        List<Inno72Machine> list = machineService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    @RequestMapping(value = "/generateMachineId", method = { RequestMethod.POST,  RequestMethod.GET})
    public String generateMachineId(@RequestParam String id) {
        Inno72Machine machine = machineService.findById(id);
        String machineId = machine.getMachineId();
        return machineId;
    }

}
