package com.inno72.order.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.order.model.Inno72Order;
import com.inno72.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderService orderService;

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
		Inno72Order order = orderService.findById(id);
		return ResultGenerator.genSuccessResult(order);
	}

	/**
	 * 订单列表
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(Inno72Order order) {
		List<Inno72Order> list = orderService.getOrderList(order);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

}
