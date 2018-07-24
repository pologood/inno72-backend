package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.project.model.Inno72Activity;
import com.inno72.project.service.ActivityService;
import com.inno72.project.vo.Inno72ActivityVo;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

/**
* Created by CodeGenerator on 2018/06/29.
*/
@RestController
@RequestMapping("/project/activity")
@CrossOrigin
public class ActivityController {
    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@Valid Inno72Activity activity,BindingResult bindingResult) {
    	try {
    		if(bindingResult.hasErrors()){
    			return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
            }else{
            	return activityService.saveModel(activity);
            }
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        try {
        	return activityService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Activity activity) {
    	
    	try {
    		return activityService.updateModel(activity);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72ActivityVo> detail(@RequestParam String id) {
    	Inno72ActivityVo activity = activityService.selectById(id);
        return ResultGenerator.genSuccessResult(activity);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestParam(required=false) String code,@RequestParam(required=false) String keyword) {
        List<Inno72ActivityVo> list = activityService.findByPage(code,keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<List<Inno72Activity>> getList(Inno72Activity activity) {
        List<Inno72Activity> list = activityService.getList();
        return ResultGenerator.genSuccessResult(list);
    }
    
    @RequestMapping(value = "/getDefaultActivity", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Activity> getDefaultActivity() {
    	Inno72Activity activity = activityService.selectDefaultActivity();
        return ResultGenerator.genSuccessResult(activity);
    }
    
}
