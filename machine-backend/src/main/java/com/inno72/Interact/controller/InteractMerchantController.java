package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.Interact.service.InteractMerchantService;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.project.model.Inno72Merchant;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/merchant")
@CrossOrigin
public class InteractMerchantController {
	@Resource
	private InteractMerchantService interactMerchantService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(InteractMerchantVo interactMerchant) {
		interactMerchantService.save(interactMerchant);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(InteractMerchantVo interactMerchant) {
		interactMerchantService.update(interactMerchant);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Merchant> detail(@RequestParam String id) {
		Inno72Merchant interactMerchant = interactMerchantService.findMerchantsById(id);
		return ResultGenerator.genSuccessResult(interactMerchant);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		interactMerchantService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<InteractMerchantVo>> getList(String interactId) {
		List<InteractMerchantVo> list = interactMerchantService.getList(interactId);

		return ResultGenerator.genSuccessResult(list);
	}

}
