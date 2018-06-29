package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Apkinfo;
import com.inno72.service.ApkinfoService;
import com.inno72.common.ResultPages;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/06/29.
*/
@RestController
@RequestMapping("/apkinfo")
public class ApkinfoController {
    @Resource
    private ApkinfoService apkinfoService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Apkinfo apkinfo) {
        apkinfoService.save(apkinfo);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam Integer id) {
        apkinfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Apkinfo apkinfo) {
        apkinfoService.update(apkinfo);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Apkinfo> detail(@RequestParam Integer id) {
        Inno72Apkinfo apkinfo = apkinfoService.findById(id);
        return ResultGenerator.genSuccessResult(apkinfo);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
        List<Inno72Apkinfo> list = apkinfoService.findByPage();
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
