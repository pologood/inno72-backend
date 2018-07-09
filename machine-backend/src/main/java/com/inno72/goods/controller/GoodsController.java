package com.inno72.goods.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.goods.model.Inno72Goods;
import com.inno72.goods.service.GoodsService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/goods/goods")
@CrossOrigin
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@Valid  Inno72Goods goods,BindingResult bindingResult) {
    	try {
    		if(bindingResult.hasErrors()){
    			return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
            }else{
            	goodsService.save(goods);
            }
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
            goodsService.delById(id);
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
    public ModelAndView list(@RequestParam(required=false) String code,@RequestParam(required=false) String keyword) {
        List<Inno72Goods> list = goodsService.findByPage(code,keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView getList(Inno72Goods goods) {
        List<Inno72Goods> list = goodsService.getList(goods);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/uploadImage", method = { RequestMethod.POST,  RequestMethod.GET})
    public @ResponseBody Result<String> uploadImage(@RequestParam(value = "file",required = false) MultipartFile file) {
    	try {
    		return goodsService.uploadImage(file);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    
}
