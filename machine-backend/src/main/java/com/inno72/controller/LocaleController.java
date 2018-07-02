package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Locale;
import com.inno72.service.LocaleService;
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
@RequestMapping("/locale")
public class LocaleController {
    @Resource
    private LocaleService localeService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Locale locale) {
    	try {
    		localeService.save(locale);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
    		localeService.deleteById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Locale locale) {
    	try {
    		localeService.update(locale);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Locale> detail(@RequestParam String id) {
        Inno72Locale locale = localeService.findById(id);
        return ResultGenerator.genSuccessResult(locale);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(Inno72Locale locale) {
        List<Inno72Locale> list = localeService.findByPage(locale);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
