package com.inno72.project.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.project.model.Inno72ActivityIndex;
import com.inno72.project.service.Inno72ActivityIndexService;

/**
* Created by CodeGenerator on 2019/01/11.
*/
@RestController
@RequestMapping("/inno72/activity/index")
@SuppressWarnings({"rawtypes", "unchecked"})
@CrossOrigin
public class Inno72ActivityIndexController {
    @Resource
    private Inno72ActivityIndexService inno72ActivityIndexService;

    @RequestMapping(value = "/activityInfo", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result activityInfo(String merchantId, String activityId) {
        return inno72ActivityIndexService.activityInfo(merchantId, activityId);
    }

    @RequestMapping(value = "/saveIndex", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> saveIndex(@RequestBody String index) {
        return inno72ActivityIndexService.saveIndex(index);
    }

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(Inno72ActivityIndex inno72ActivityIndex) {
		inno72ActivityIndexService.save(inno72ActivityIndex);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
		inno72ActivityIndexService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(Inno72ActivityIndex inno72ActivityIndex) {
		inno72ActivityIndexService.update(inno72ActivityIndex);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
		Inno72ActivityIndex inno72ActivityIndex = inno72ActivityIndexService.findById(id);
        return ResultGenerator.genSuccessResult(inno72ActivityIndex);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Inno72ActivityIndex> list = inno72ActivityIndexService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
