package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.Interact.model.Inno72InteractMachineGoods;
import com.inno72.Interact.service.InteractMachineGoodsService;
import com.inno72.Interact.vo.Inno72InteractMachineGoodsVo;
import com.inno72.Interact.vo.InteractMachineGoods;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact/machine/goods")
@CrossOrigin
public class InteractMachineGoodsController {
	@Resource
	private InteractMachineGoodsService interactMachineGoodsService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@RequestBody InteractMachineGoods interactMachineGoods) {
		interactMachineGoodsService.save(interactMachineGoods);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		interactMachineGoodsService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72InteractMachineGoods interactMachineGoods) {
		interactMachineGoodsService.update(interactMachineGoods);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72InteractMachineGoods> detail(@RequestParam String id) {
		Inno72InteractMachineGoods interactMachineGoods = interactMachineGoodsService.findById(id);
		return ResultGenerator.genSuccessResult(interactMachineGoods);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72InteractMachineGoodsVo>> list(String interactId, String machineId) {

		List<Inno72InteractMachineGoodsVo> list = interactMachineGoodsService.selectMachineGoods(interactId, machineId);
		return ResultGenerator.genSuccessResult(list);
	}
}
