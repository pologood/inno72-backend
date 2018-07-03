package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Goods;
import com.inno72.service.GoodsService;
import com.inno72.common.ResultPages;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

/**
* Created by CodeGenerator on 2018/06/29.
*/
@RestController
@RequestMapping("/goods")
@Validated
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@Valid  Inno72Goods goods,BindingResult bindingResult,@RequestParam(value = "file",required = false) MultipartFile file) {
    	try {
    		if(bindingResult.hasErrors()){
    			return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
            }else{
            	goodsService.save(goods,file);
            }
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
            goodsService.deleteById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Goods goods) {
    	try {
    		goodsService.update(goods);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Goods> detail(@RequestParam String id) {
        Inno72Goods goods = goodsService.findById(id);
        return ResultGenerator.genSuccessResult(goods);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(Inno72Goods goods) {
        List<Inno72Goods> list = goodsService.findByPage(goods);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView getList(Inno72Goods goods) {
        List<Inno72Goods> list = goodsService.getList(goods);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
