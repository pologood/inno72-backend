package com.inno72.share.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.share.service.ShareService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;

/**
* Created by CodeGenerator on 2018/06/29.
*/
@RestController
@RequestMapping("/share")
@CrossOrigin
public class ShareController {
    @Resource
    private ShareService shareService;

    @RequestMapping(value = "/uploadImage", method = { RequestMethod.POST,  RequestMethod.GET})
    public @ResponseBody Result<String> uploadImage(@RequestParam(value = "file",required = false) MultipartFile file,String type) {
    	try {
    		return shareService.uploadImage(file,type);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
}
