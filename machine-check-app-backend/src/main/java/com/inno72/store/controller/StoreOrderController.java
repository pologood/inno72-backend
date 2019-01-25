package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.check.model.Inno72CheckGoodsNum;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.service.StoreOrderService;
import com.inno72.store.vo.StoreOrderVo;

import tk.mybatis.mapper.entity.Condition;

/**
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/store/order")
public class StoreOrderController {
    @Resource
    private StoreOrderService storeOrderService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(@RequestBody StoreOrderVo storeOrderVo) {
        logger.info("添加商品寄出单接收参数：{}", JSON.toJSON(storeOrderVo));
    	Result<String> result = storeOrderService.saveOrder(storeOrderVo);
		logger.info("添加商品寄出单结果：{}", JSON.toJSON(result));
        return result;
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestBody StoreOrderVo storeOrderVo) {
    	String id = storeOrderVo.getId();
    	logger.info("删除寄出单接口接收参数：{}",JSON.toJSON(id));
        Result<String> result = storeOrderService.deleteModel(id);
		logger.info("删除寄出单接口返回结果：{}",JSON.toJSON(result));
        return result;
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(@RequestBody StoreOrderVo storeOrderVo) {
		logger.info("修改寄出单接口接收参数：{}",JSON.toJSON(storeOrderVo));
        storeOrderService.updateOrder(storeOrderVo);
        logger.info("修改寄出单返回结果：{}",JSON.toJSON(ResultGenerator.genSuccessResult()));
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72StoreOrder> detail(@RequestBody StoreOrderVo storeOrderVo) {
    	logger.info("查询寄出单详情接收参数：{}",JSON.toJSON(storeOrderVo));
        Inno72StoreOrder storeOrder = storeOrderService.findOrderById(storeOrderVo.getId());
		logger.info("查询寄出单详情返回结果：{}",JSON.toJSON(storeOrder));
        return ResultGenerator.genSuccessResult(storeOrder);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestBody StoreOrderVo storeOrderVo) {
    	logger.info("查询订单集合接收参数:{}",JSON.toJSON(storeOrderVo));
        List<Inno72StoreOrder> list = storeOrderService.findOrderByPage(storeOrderVo);
        logger.info("查询订单集合返回结果:{}",JSON.toJSON(list));
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

    @RequestMapping(value = "/activityList")
    public Result<List<Inno72CheckGoodsNum>> activityList(@RequestBody StoreOrderVo storeOrderVo){
    	logger.info("查询活动列表接收参数{}",JSON.toJSON(storeOrderVo));
		Result<List<Inno72CheckGoodsNum>> result = storeOrderService.findActivityList(storeOrderVo);
		logger.info("查询活动列表返回结果{}",JSON.toJSON(result));
		return result;
	}
}
