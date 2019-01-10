package com.inno72.store.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.inno72.store.vo.StoreOrderVo;

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
	public Result<Object> add(Inno72StoreOrder storeOrder) {
		return storeOrderService.saveModel(storeOrder);
	}

	@RequestMapping(value = "/receiverConfirm", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> receiverConfirm(@RequestBody StoreOrderVo storeOrderVo) {
		return storeOrderService.receiverConfirm(storeOrderVo);
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
	public Result<StoreOrderVo> detail(@RequestParam String id) {
		StoreOrderVo storeOrder = storeOrderService.findDetailById(id);
		return ResultGenerator.genSuccessResult(storeOrder);
	}

	/**
	 * 查询出库单
	 */
	@RequestMapping(value = "/sendOrderList", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView sendOrderList(String date, String keyword) {
		List<Map<String, Object>> list = storeOrderService.findSendOrderByPage(date, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 查询入库单
	 */
	@RequestMapping(value = "/receiveOrderList", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView receiveOrderList(String date, String keyword) {
		List<Map<String, Object>> list = storeOrderService.findReceiveOrderByPage(date, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
