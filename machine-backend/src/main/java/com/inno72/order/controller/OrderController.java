package com.inno72.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.model.Page;
import com.inno72.order.service.OrderService;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderService orderService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72Order order) {
		orderService.save(order);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		orderService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72Order order) {
		orderService.update(order);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Order> detail(@RequestParam String id) {
		logger.info("查询订单详情参数：{}", JSON.toJSON(id));
		Result<Inno72Order> result = orderService.getOrderDetail(id);
		logger.info("查询订单详情结果：{}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 订单列表
	 * 
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String,Object> list(Inno72Order order) {
		logger.info("查询订单列表参数：{}", JSON.toJSON(order));
		return orderService.getOrderList(order);
	}

}
