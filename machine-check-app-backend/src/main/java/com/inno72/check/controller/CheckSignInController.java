package com.inno72.check.controller;

import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/check/signIn")
@CrossOrigin
@RestController
public class CheckSignInController {


    @Resource
    private CheckSignInService checkSignInService;

    /**
     * 打卡
     * @param signIn
     * @return
     */
    @RequestMapping(value="add", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<String> add(@RequestBody Inno72CheckSignIn signIn){
        Result<String> result = checkSignInService.add(signIn);
        return result;
    }

    /**
     * 查询本月打卡记录
     * @return
     */
    @RequestMapping(value="list", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<List<Inno72CheckSignIn>> list(@RequestBody Inno72CheckSignIn signIn){
        Result<List<Inno72CheckSignIn>> result = checkSignInService.findThisMonth(signIn.getMachineId());
        return result;
    }

    @RequestMapping(value="machineList", method = {RequestMethod.POST,RequestMethod.GET})
    public Result<List<MachineSignInVo>> machineSignInList(){
        Result<List<MachineSignInVo>> result = checkSignInService.findMachineSignList();
        return result;
    }


}
