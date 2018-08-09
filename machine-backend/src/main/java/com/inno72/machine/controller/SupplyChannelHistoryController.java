package com.inno72.machine.controller;

import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.machine.service.SupplyChannelHistoryService;
import com.inno72.machine.vo.SupplyOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ModelAndView list(@RequestBody SupplyOrderVo supplyOrderVo){
        List<SupplyOrderVo> list = supplyChannelHistoryService.list(supplyOrderVo);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
