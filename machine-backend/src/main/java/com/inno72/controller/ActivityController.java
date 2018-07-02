package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Activity;
import com.inno72.service.ActivityService;
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
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Activity activity) {
    	try {
    		activityService.save(activity);
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        try {
    		activityService.deleteById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Activity activity) {
    	
    	try {
    		activityService.update(activity);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Activity> detail(@RequestParam String id) {
        Inno72Activity activity = activityService.findById(id);
        return ResultGenerator.genSuccessResult(activity);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(Inno72Activity activity) {
   	   Condition condition = new Condition( Inno72Activity.class);
   	   condition.createCriteria().andEqualTo(activity);
        List<Inno72Activity> list = activityService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
