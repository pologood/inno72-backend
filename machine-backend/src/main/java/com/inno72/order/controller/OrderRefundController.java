package com.inno72.order.controller;

import java.util.List;
import java.util.Map;

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
import com.inno72.order.model.Inno72OrderRefund;
import com.inno72.order.service.OrderRefundService;

/**
 * Created by CodeGenerator on 2018/12/17.
 */
@RestController
@CrossOrigin
@RequestMapping("/order/refund")
public class OrderRefundController {
	@Resource
	private OrderRefundService orderRefundService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72OrderRefund orderRefund) {
		orderRefundService.save(orderRefund);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		orderRefundService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72OrderRefund orderRefund, String type) {
		return orderRefundService.updateModle(orderRefund, type);
	}

	@RequestMapping(value = "/refundAudit", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> refundAudit(String id, String auditStatus, String auditReason) {
		return orderRefundService.refundAudit(id, auditStatus, auditReason);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> detail(@RequestParam String id) {
		Map<String, Object> orderRefund = orderRefundService.selectRefundDetail(id);
		return ResultGenerator.genSuccessResult(orderRefund);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String channel, String time, String status, String auditStatus, String keyword) {
		List<Map<String, Object>> list = orderRefundService.getRefundList(channel, time, status, auditStatus, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
