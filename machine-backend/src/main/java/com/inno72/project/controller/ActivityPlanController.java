package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.project.model.Inno72ActivityPlan;
import com.inno72.project.service.ActivityPlanService;
import com.inno72.project.vo.Inno72ActivityPlanVo;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.common.ResultPages;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/11.
*/
@RestController
@RequestMapping("/project/activityPlan")
public class ActivityPlanController {
    @Resource
    private ActivityPlanService activityPlanService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@RequestBody Inno72ActivityPlanVo activityPlan) {
    	try {
    		return activityPlanService.saveActPlan(activityPlan);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
    		return activityPlanService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(@RequestBody Inno72ActivityPlanVo activityPlan) {
    	try {
    		return activityPlanService.updateModel(activityPlan);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    	
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72ActivityPlan> detail(@RequestParam String id) {
        Inno72ActivityPlan activityPlan = activityPlanService.findById(id);
        return ResultGenerator.genSuccessResult(activityPlan);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(String code,String startTime,String endTime) {
        List<Inno72ActivityPlanVo> list = activityPlanService.selectPlanList(code,startTime,endTime);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    

    @RequestMapping(value = "/selectAreaMachines", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<List<Inno72AdminAreaVo>> selectMachines(String code,String level) {
   	   	//联动查询
        List<Inno72AdminAreaVo> list = activityPlanService.selectAreaMachineList(code,level);
        return ResultGenerator.genSuccessResult(list);
    }
    
    
    
    
    
}
