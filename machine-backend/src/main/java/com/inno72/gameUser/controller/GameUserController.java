package com.inno72.gameUser.controller;

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
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.model.Inno72GameUserChannel;
import com.inno72.gameUser.service.GameUserService;

@RestController
@CrossOrigin
@RequestMapping("/game/user")
public class GameUserController {

	@Resource
	private GameUserService gameUserService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72GameUser gameUser) {
		gameUserService.save(gameUser);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		gameUserService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72GameUser gameUser) {
		gameUserService.update(gameUser);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72GameUser> detail(@RequestParam String id) {
		Inno72GameUser gameUser = gameUserService.findById(id);
		return ResultGenerator.genSuccessResult(gameUser);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(Inno72GameUserChannel inno72GameUserChannel) {
		List<Inno72GameUserChannel> list = gameUserService.findForPage(inno72GameUserChannel);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

}
