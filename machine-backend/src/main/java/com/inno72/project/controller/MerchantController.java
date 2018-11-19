package com.inno72.project.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.service.MerchantService;
import com.inno72.project.vo.Inno72MerchantVo;

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
@RequestMapping("/project/merchant")
@CrossOrigin
public class MerchantController {
    @Resource
    private MerchantService merchantService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@Valid Inno72Merchant merchant, BindingResult bindingResult) {
    	try {
    		if(bindingResult.hasErrors()){
    			return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
            }else{
            	return merchantService.saveModel(merchant);
            }
		} catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
    		return merchantService.delById(id);
	    } catch (Exception e) {
	    	return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Merchant merchant) {
    	try {
    		return merchantService.updateModel(merchant);
	    } catch (Exception e) {
			ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Merchant> detail(@RequestParam String id) {
        Inno72Merchant merchant = merchantService.findById(id);
        return ResultGenerator.genSuccessResult(merchant);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestParam(required=false) String code,@RequestParam(required=false) String keyword) {
        List<Inno72MerchantVo> list = merchantService.findByPage(code,keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<List<Inno72Merchant>> getList(Inno72Merchant merchant) {
        List<Inno72Merchant> list = merchantService.getList(merchant);
        return ResultGenerator.genSuccessResult(list);
    }

	@RequestMapping(value = "/uploadImage", method = { RequestMethod.POST,  RequestMethod.GET})
	public @ResponseBody
	Result<String> uploadImage(@RequestParam(value = "file",required = false) MultipartFile file) {
		try {
			return merchantService.uploadImage(file);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}
}
