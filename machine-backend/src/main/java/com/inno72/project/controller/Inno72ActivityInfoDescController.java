package com.inno72.project.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.Interact.mapper.Inno72InteractMapper;
import com.inno72.Interact.model.Inno72Interact;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.SessionUtil;
import com.inno72.common.StringUtil;
import com.inno72.project.model.Inno72ActivityInfoDesc;
import com.inno72.project.service.Inno72ActivityInfoDescService;
import com.inno72.system.model.Inno72User;

/**
* Created by CodeGenerator on 2019/01/11.
*/
@RestController
@RequestMapping("/inno72/activity/info/desc")
@SuppressWarnings({"rawtypes", "unchecked"})
@CrossOrigin
public class Inno72ActivityInfoDescController {
    @Resource
    private Inno72ActivityInfoDescService inno72ActivityInfoDescService;

    @Resource
    private Inno72InteractMapper inno72InteractMapper;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(@Valid Inno72ActivityInfoDesc inno72ActivityInfoDesc) {
		Inno72User inno72User = getmUser();
		inno72ActivityInfoDesc.setCreator(inno72User.getId());
		inno72ActivityInfoDesc.setCreateTime(LocalDateTime.now());
		inno72ActivityInfoDesc.setId(StringUtil.getUUID());
		String activityId = inno72ActivityInfoDesc.getActivityId();
		Inno72Interact inno72Interact = inno72InteractMapper.selectByPrimaryKey(activityId);
		if (inno72Interact == null){
			return Results.failure("活动错误!");
		}
		inno72ActivityInfoDesc.setActivityName(inno72Interact.getName());
		inno72ActivityInfoDescService.save(inno72ActivityInfoDesc);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
		inno72ActivityInfoDescService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72ActivityInfoDesc inno72ActivityInfoDesc) {
		inno72ActivityInfoDescService.update(inno72ActivityInfoDesc);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
		Inno72ActivityInfoDesc inno72ActivityInfoDesc = inno72ActivityInfoDescService.findById(id);
        return ResultGenerator.genSuccessResult(inno72ActivityInfoDesc);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72ActivityInfoDesc> list = inno72ActivityInfoDescService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

	private Inno72User getmUser(){
		SessionData session = SessionUtil.sessionData.get();
		return Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
	}
}
