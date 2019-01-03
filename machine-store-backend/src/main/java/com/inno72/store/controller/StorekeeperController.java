package com.inno72.store.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.SessionData;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.store.service.StorekeeperService;
import com.inno72.common.ResultPages;
import com.inno72.store.vo.StoreKepperVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;


import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/storekeeper")
public class StorekeeperController {
    @Resource
    private StorekeeperService storekeeperService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@RequestBody Inno72Storekeeper storekeeper) {
        logger.info("添加库存管理人员接口参数:{}", JSON.toJSON(storekeeper));
    	Result<String> result = storekeeperService.saveKeeper(storekeeper);
		logger.info("添加库存管理人员接口返回结果:{}", JSON.toJSON(result));
        return result;
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        storekeeperService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(@RequestBody Inno72Storekeeper storekeeper) {
        Result<String> result = storekeeperService.updateKeepper(storekeeper);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Storekeeper> detail(@RequestParam String id) {
        Inno72Storekeeper storekeeper = storekeeperService.findDetail(id);
        return ResultGenerator.genSuccessResult(storekeeper);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(StoreKepperVo storeKepperVo) {
        List<Inno72Storekeeper> list = storekeeperService.findKepperByPage(storeKepperVo);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    @RequestMapping(value = "/editUse")
    public Result<String> editUse(Inno72Storekeeper storekeeper){
    	Result<String> result = storekeeperService.editUse(storekeeper);
    	return result;
	}


	@RequestMapping(value = "/smsCode")
	public Result<String> smsCode(String mobile){
    	Result<String> result = storekeeperService.getSmsCode(mobile);
    	return result;
	}

	@RequestMapping(value = "/login")
	public Result<SessionData> login(Inno72Storekeeper inno72Storekeeper){
    	Result<SessionData> result = storekeeperService.login(inno72Storekeeper);
    	return result;
	}

	@RequestMapping(value = "/logout")
	public Result<String> logout(){
    	Result<String> result = storekeeperService.logout();
    	return result;
	}

	@RequestMapping(value = "/setPwd")
	public Result<String> setPwd(String newPwd,String rePwd){
    	Result<String> result = storekeeperService.setPwd(newPwd,rePwd);
    	return result;
	}

	@RequestMapping(value = "/reSetPwd")
	public Result<String> reSetPwd(String oldPwd,String newPwd,String rePwd){
    	Result<String> result = storekeeperService.reSetPwd(oldPwd,newPwd,rePwd);
    	return result;
	}


}
