package com.inno72.machine.controller;

import com.inno72.common.Result;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
     * @param machineId
     * @param localeId
     * @return
     */
    @RequestMapping(value="set" ,method = {RequestMethod.POST,RequestMethod.GET})
    public Result<String> setMachine(String machineId, String localeId){
        Result<String> result = machineService.setMachine(machineId,localeId);
        return result;
    }

    /**
     * 查询管理的机器
     * @return
     */
    @RequestMapping(value="list")
    public Result<List<Inno72Machine>> list(){
        Result<List<Inno72Machine>> result = machineService.getMachineList();
        return result;

    }
}
