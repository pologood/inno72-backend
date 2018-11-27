package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.Interact.model.Inno72InteractGameRule;
import com.inno72.Interact.service.InteractGameRuleService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;

/**
 * Created by CodeGenerator on 2018/11/27.
 */
@RestController
@RequestMapping("/interact/game/rule")
public class InteractGameRuleController {
	@Resource
	private InteractGameRuleService interactGameRuleService;

	@RequestMapping(value = "/getGameRuleList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72InteractGameRule>> list(String interactId) {
		List<Inno72InteractGameRule> list = interactGameRuleService.getGameRuleList(interactId);
		return ResultGenerator.genSuccessResult(list);
	}

}
