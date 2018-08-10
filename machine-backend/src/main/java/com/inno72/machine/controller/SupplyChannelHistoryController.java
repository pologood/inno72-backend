package com.inno72.machine.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.service.SupplyChannelHistoryService;
import com.inno72.machine.vo.SupplyOrderVo;
import com.inno72.machine.vo.SupplyRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/supply/channel/history")
public class SupplyChannelHistoryController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SupplyChannelHistoryService supplyChannelHistoryService;
    @RequestMapping(value="/list")
    public ModelAndView findListByPage(@RequestBody SupplyOrderVo supplyOrderVo){
        List<SupplyOrderVo> list = supplyChannelHistoryService.findListByPage(supplyOrderVo);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    /**
     * 补货详情
     */
    @RequestMapping(value="/detail",method = {RequestMethod.POST})
    public Result<List<Inno72SupplyChannelHistory>> workOrderDetail(@RequestBody SupplyRequestVo vo){
        logger.info("查询补货记录详情接口参数：{}",JSON.toJSON(vo));
        return supplyChannelHistoryService.detail(vo);
    }
}
