package com.inno72.check.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.check.vo.SignVo;
import com.inno72.common.Result;
import com.inno72.machine.model.Inno72Machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/check/signIn")
@CrossOrigin
@RestController
public class CheckSignInController {


    @Resource
    private CheckSignInService checkSignInService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 打卡
     */
    @RequestMapping(value="add", method = {RequestMethod.POST})
    public Result<String> add(@RequestBody Inno72CheckSignIn signIn){
        logger.info("打卡接口参数{}", JSON.toJSON(signIn));
        Result<String> result = checkSignInService.add(signIn);
        logger.info("打卡接口结果{}", JSON.toJSON(result));
        return result;
    }

    /**
     * 查询本月打卡记录
     */
    @RequestMapping(value="list", method = {RequestMethod.POST})
    public Result<List<SignVo>> list(@RequestBody SignVo signVo){
        logger.info("查询本月打卡记录接口参数{}", JSON.toJSON(signVo));
        Result<List<SignVo>> result = checkSignInService.findByMonth(signVo);
        logger.info("查询本月打卡记录接口结果{}", JSON.toJSON(result));
        return result;
    }

    /**
     * 查询机器打卡状态
     */
    @RequestMapping(value="machineList", method = {RequestMethod.POST})
    public Result<List<MachineSignInVo>> machineSignInList(@RequestBody Inno72Machine machine){
        logger.info("查询机器打卡状态");
        Result<List<MachineSignInVo>> result = checkSignInService.findMachineSignList(machine);
        logger.info("查询机器打卡状态结果{}",JSON.toJSON(result));
        return result;
    }


}
