package com.inno72.check.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.service.CheckFaultService;
import com.inno72.check.vo.Inno72CheckFaultVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

/**
 * Created by CodeGenerator on 2018/07/19.
 */
@RestController
@RequestMapping("/check/fault")
@CrossOrigin
public class CheckFaultController {
	@Resource
	private CheckFaultService checkFaultService;

	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> save(Inno72CheckFault checkFault) {
		checkFaultService.update(checkFault);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72CheckFault checkFault) {
		checkFaultService.update(checkFault);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72CheckFault> detail(@RequestParam String id) {
		Inno72CheckFaultVo checkFault = checkFaultService.selectFaultDetail(id);
		return ResultGenerator.genSuccessResult(checkFault);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String keyword, String status, String type, String startTime, String endTime) {
		List<Inno72CheckFault> list = checkFaultService.findByPage(keyword, status, type, startTime, endTime);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/answer", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> answer(String id, String remark) {
		return checkFaultService.faultAnswer(id, remark);
	}
}
