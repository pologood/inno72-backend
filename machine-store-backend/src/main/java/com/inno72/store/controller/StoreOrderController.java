package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.store.model.Inno72StoreOrder;
import com.inno72.store.service.StoreOrderService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@RestController
@RequestMapping("/store/order")
@CrossOrigin
public class StoreOrderController {
	@Resource
	private StoreOrderService storeOrderService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72StoreOrder storeOrder) {
		storeOrderService.save(storeOrder);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		storeOrderService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72StoreOrder storeOrder) {
		storeOrderService.update(storeOrder);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72StoreOrder> detail(@RequestParam String id) {
		Inno72StoreOrder storeOrder = storeOrderService.findById(id);
		return ResultGenerator.genSuccessResult(storeOrder);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		Condition condition = new Condition(Inno72StoreOrder.class);
		List<Inno72StoreOrder> list = storeOrderService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
