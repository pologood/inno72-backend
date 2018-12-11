package com.inno72.Interact.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.Interact.service.InteractMerchantService;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.Interact.vo.MerchantVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/merchant")
@CrossOrigin
public class InteractMerchantController {
	@Resource
	private InteractMerchantService interactMerchantService;

	@RequestMapping(value = "/checkMerchantUser", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> checkMerchantUser(String keyword) {
		List<Map<String, Object>> list = interactMerchantService.getMerchantUserList(keyword);

		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/checkMerchant", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Map<String, Object>>> checkMerchant(String interactId, String merchantAccountId) {
		List<Map<String, Object>> list = interactMerchantService.checkMerchant(interactId, merchantAccountId);

		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> add(@RequestBody InteractMerchantVo interactMerchant) {
		return interactMerchantService.save(interactMerchant);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(String interactId, String merchantId, Integer isFocus) {
		return interactMerchantService.update(interactId, merchantId, isFocus);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<MerchantVo> detail(@RequestParam String id, String interactId) {
		MerchantVo interactMerchant = interactMerchantService.findMerchantsById(id, interactId);
		return ResultGenerator.genSuccessResult(interactMerchant);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(String interactId, String merchantId) {
		return interactMerchantService.deleteById(interactId, merchantId);
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<MerchantVo>> getList(String interactId) {
		List<MerchantVo> list = interactMerchantService.getList(interactId);

		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/exportMachineSellerId")
	public void exportMachineSellerId(@RequestParam(required = false) String activityId,
			@RequestParam(required = false) String activityType, HttpServletResponse response) throws Exception {
		interactMerchantService.findMachineSellerId(activityId, activityType, response);

	}

}
