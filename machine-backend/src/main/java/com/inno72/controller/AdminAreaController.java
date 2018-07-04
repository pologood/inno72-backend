package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72AdminArea;
import com.inno72.service.AdminAreaService;
import com.inno72.common.ResultPages;
import com.inno72.common.StringUtil;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/admin/area")
@CrossOrigin
public class AdminAreaController {
    @Resource
    private AdminAreaService adminAreaService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72AdminArea adminArea) {
        adminAreaService.save(adminArea);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        adminAreaService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72AdminArea adminArea) {
        adminAreaService.update(adminArea);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72AdminArea> detail(@RequestParam String id) {
        Inno72AdminArea adminArea = adminAreaService.findById(id);
        return ResultGenerator.genSuccessResult(adminArea);
    }
    
    @RequestMapping(value = "/pageList", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView pageList() {
   	   Condition condition = new Condition( Inno72AdminArea.class);
        List<Inno72AdminArea> list = adminAreaService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<List<Inno72AdminArea>> list(String code) {
   	   	//联动查询
        List<Inno72AdminArea> list = adminAreaService.getLiset(code);
        return ResultGenerator.genSuccessResult(list);
    }
}
