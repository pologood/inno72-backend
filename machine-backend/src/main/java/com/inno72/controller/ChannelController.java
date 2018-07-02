package com.inno72.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72Channel;
import com.inno72.service.ChannelService;
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
@RequestMapping("/channel")
public class ChannelController {
    @Resource
    private ChannelService channelService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Channel channel) {
        channelService.save(channel);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam Integer id) {
        channelService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Channel channel) {
        channelService.update(channel);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Channel> detail(@RequestParam Integer id) {
        Inno72Channel channel = channelService.findById(id);
        return ResultGenerator.genSuccessResult(channel);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72Channel.class);
        List<Inno72Channel> list = channelService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
