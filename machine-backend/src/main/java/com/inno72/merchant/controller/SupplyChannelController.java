package com.inno72.merchant.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.merchant.model.Inno72SupplyChannel;
import com.inno72.merchant.service.SupplyChannelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/03.
*/
@RestController
@RequestMapping("/merchant/supply/channel")
public class SupplyChannelController {
    @Resource
    private SupplyChannelService supplyChannelService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72SupplyChannel supplyChannel) {
        supplyChannelService.save(supplyChannel);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        supplyChannelService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72SupplyChannel supplyChannel) {
        supplyChannelService.update(supplyChannel);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72SupplyChannel> detail(@RequestParam String id) {
        Inno72SupplyChannel supplyChannel = supplyChannelService.findById(id);
        return ResultGenerator.genSuccessResult(supplyChannel);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72SupplyChannel.class);
        List<Inno72SupplyChannel> list = supplyChannelService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    /**
     * 初始化数据
     * @param merchantId
     * @return
     */
    @RequestMapping(value = "/init" ,method={RequestMethod.POST,RequestMethod.GET})
    public Result init(@RequestParam String merchantId){
        Result result = supplyChannelService.init(merchantId);
        return result;
    }



}
