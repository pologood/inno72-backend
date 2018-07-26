package com.inno72.machine.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.SupplyRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/machine/machine")
public class MachineController {

    @Resource
    private MachineService machineService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 设置机器点位及管理人
     */
    @RequestMapping(value="set" ,method = {RequestMethod.POST,RequestMethod.GET})
    public Result<String> setMachine(@RequestBody SupplyRequestVo vo){
        logger.info("设置机器点位及管理人接口参数：{}",JSON.toJSON(vo));
        Result<String> result = machineService.setMachine(vo.getMachineId(),vo.getLocaleId());
        logger.info("设置机器点位及管理人接口结果：{}",JSON.toJSON(result));
        return result;
    }

    /**
     * 查询管理的机器
     */
    @RequestMapping(value="list", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<List<Inno72Machine>> list(){
        logger.info("查询管理的机器接口");
        Result<List<Inno72Machine>> result = machineService.getMachineList();
        logger.info("查询管理的机器接口结果{}",JSON.toJSON(result));
        return result;

    }

    /**
     * 查询单个一级区域及子区域
     */
    @RequestMapping(value="findAreaByCode")
    public Result<Inno72AdminArea> findAreaByCode(@RequestBody Inno72AdminArea adminArea){
        return machineService.findByFirstLevelCode(adminArea.getCode());
    }


    /**
     * 查询一级区域
     */
    @RequestMapping(value="findFistLevelArea")
    public Result<List<Inno72AdminArea>> findFistLevelArea(){
        return machineService.findFistLevelArea();
    }


    /**
     * 查询商场
     */
    @RequestMapping(value="findMallByCode")
    public Result<List<Inno72Locale>> findMallByCode(@RequestBody Inno72Locale locale){
        return machineService.findMallByCode(locale.getAreaCode());
    }

    /**
     * 查询e
     */
    @RequestMapping(value="findLocaleByMall")
    public Result<List<Inno72Locale>> findLocaleByMall(@RequestBody Inno72Locale locale){
        return machineService.findLocaleByMall(locale.getMall());
    }


}
