package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.Interact.service.InteractGameRuleService;
import com.inno72.Interact.vo.InteractGameRuleVo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;

/**
 * Created by CodeGenerator on 2018/11/27.
 */
@RestController
@RequestMapping("/interact/game/rule")
@CrossOrigin
public class InteractGameRuleController {
	@Resource
	private InteractGameRuleService interactGameRuleService;

	@RequestMapping(value = "/getGameRuleList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<InteractGameRuleVo>> list(String interactId) {
		List<InteractGameRuleVo> list = interactGameRuleService.getGameRuleList(interactId);
		return ResultGenerator.genSuccessResult(list);
	}

}
