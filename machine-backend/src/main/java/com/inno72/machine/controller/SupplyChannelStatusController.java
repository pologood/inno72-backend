package com.inno72.machine.controller;


import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.goods.model.GoodsChannelBean;
import com.inno72.machine.model.Inno72SupplyChannelStatus;
import com.inno72.machine.service.SupplyChannelStatusService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/07/18.
 */
@RestController
@RequestMapping("/supply/channel/status")
public class SupplyChannelStatusController {
    @Resource
    private SupplyChannelStatusService supplyChannelStatusService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<String> add(Inno72SupplyChannelStatus supplyChannelStatus) {
        supplyChannelStatusService.save(supplyChannelStatus);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        supplyChannelStatusService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<String> update(Inno72SupplyChannelStatus supplyChannelStatus) {
        supplyChannelStatusService.update(supplyChannelStatus);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Inno72SupplyChannelStatus> detail(@RequestParam String id) {
        Inno72SupplyChannelStatus supplyChannelStatus = supplyChannelStatusService.findById(id);
        return ResultGenerator.genSuccessResult(supplyChannelStatus);
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView list() {
        Condition condition = new Condition(Inno72SupplyChannelStatus.class);
        List<Inno72SupplyChannelStatus> list = supplyChannelStatusService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    @RequestMapping(value = "/getChannelErrorDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<List<GoodsChannelBean>> getChannelErrorDetail(@RequestBody List<GoodsChannelBean> goodsChannelStatus) {
        List<GoodsChannelBean> errorDetail = supplyChannelStatusService.getChannelErrorDetail(goodsChannelStatus);
        return ResultGenerator.genSuccessResult(errorDetail);
    }

}
