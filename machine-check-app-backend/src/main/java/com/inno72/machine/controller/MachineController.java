package com.inno72.machine.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.SupplyChannelVo;
import com.inno72.machine.vo.SupplyRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 设置机器点位及管理人
     * @return
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
     * @return
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
     * @param adminArea
     * @return
     */
    @RequestMapping(value="findAreaByCode")
    public Result<Inno72AdminArea> findAreaByCode(@RequestBody Inno72AdminArea adminArea){
        Result<Inno72AdminArea> result = machineService.findByFirstLevelCode(adminArea.getCode());
        return result;
    }


    /**
     * 查询单个一级区域及子区域
     * @return
     */
    @RequestMapping(value="findFistLevelArea")
    public Result<List<Inno72AdminArea>> findFistLevelArea(){
        Result<List<Inno72AdminArea>> result = machineService.findFistLevelArea();
        return result;
    }


    /**
     * 查询商场
     * @param locale
     * @return
     */
    @RequestMapping(value="findMallByCode")
    public Result<List<Inno72Locale>> findMallByCode(@RequestBody Inno72Locale locale){
        Result<List<Inno72Locale>> result = machineService.findMallByCode(locale.getAreaCode());
        return result;
    }

    @RequestMapping(value="findLocalByMall")
    public Result<List<Inno72Locale>> findLocalByMall(@RequestBody Inno72Locale locale){
        Result<List<Inno72Locale>> result = machineService.findLocalByMall(locale.getMall());
        return result;
    }


}
