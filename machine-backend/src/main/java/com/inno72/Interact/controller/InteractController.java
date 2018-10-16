package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.model.Inno72Interact;
import com.inno72.Interact.service.InteractService;
import com.inno72.Interact.vo.Inno72InteractVo;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.Interact.vo.InteractRuleVo;
import com.inno72.Interact.vo.TreeVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/project/interact")
@CrossOrigin
public class InteractController {
	@Resource
	private InteractService interactService;

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String keyword, Integer status, String orderBy) {
		List<InteractListVo> list = interactService.findByPage(keyword, status, orderBy);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> add(Inno72Interact interact, Integer type) {
		return interactService.save(interact, type);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> update(Inno72Interact interact, Integer type) {
		return interactService.update(interact, type);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		interactService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72InteractVo> detail(@RequestParam String id) {
		Inno72InteractVo interact = interactService.findDetailById(id);
		return ResultGenerator.genSuccessResult(interact);
	}

	@RequestMapping(value = "/rule", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> updateRule(@RequestBody InteractRuleVo interactRule) {
		return interactService.updateRule(interactRule);
	}

	@RequestMapping(value = "/next", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> next(@RequestParam String interactId, @RequestParam String type) {
		return interactService.next(interactId, type);
	}

	@RequestMapping(value = "/merchantTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<TreeVo>> merchantTree(@RequestParam String interactId) {
		List<TreeVo> list = interactService.merchantTree(interactId);
		return ResultGenerator.genSuccessResult(list);
	}

	@RequestMapping(value = "/machineTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<TreeVo>> machineTree(@RequestParam String interactId,
			@RequestParam(required = false) String keyword) {
		List<TreeVo> list = interactService.machineTree(interactId, keyword);
		return ResultGenerator.genSuccessResult(list);
	}

}
