package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.project.model.Inno72Channel;
import com.inno72.project.service.ChannelService;

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
@RequestMapping("/project/channel")
@CrossOrigin
public class ChannelController {
    @Resource
    private ChannelService channelService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@Valid Inno72Channel channel,BindingResult bindingResult) {
    	try {
    		if(bindingResult.hasErrors()){
    			return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
            }else{
            	return channelService.saveModel(channel);
            }
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
    		return channelService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Channel channel) {
    	try {
    		return channelService.updateModel(channel);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}

    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Channel> detail(@RequestParam String id) {
        Inno72Channel channel = channelService.findById(id);
        return ResultGenerator.genSuccessResult(channel);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestParam(required=false) String keyword) {
        List<Inno72Channel> list = channelService.findByPage(keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<List<Inno72Channel>> getList(Inno72Channel activity) {
        List<Inno72Channel> list = channelService.getList(activity);
        return ResultGenerator.genSuccessResult(list);
    }
}
