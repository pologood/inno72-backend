package com.inno72.machine.controller;

import com.inno72.common.Result;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.SupplyChannelVo;
import com.inno72.machine.vo.SupplyRequestVo;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/machine/machine")
public class MachineController {

    @Resource
    private MachineService machineService;

    /**
     * 设置机器点位及管理人
     * @return
     */
    @RequestMapping(value="set" ,method = {RequestMethod.POST,RequestMethod.GET})
    public Result<String> setMachine(@RequestBody SupplyRequestVo vo){
        Result<String> result = machineService.setMachine(vo.getMachineId(),vo.getLocaleId());
        return result;
    }

    /**
     * 查询管理的机器
     * @return
     */
    @RequestMapping(value="list", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<List<Inno72Machine>> list(){
        Result<List<Inno72Machine>> result = machineService.getMachineList();
        return result;

    }
}
