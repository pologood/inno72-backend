package com.inno72.project.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.project.model.Inno72Game;
import com.inno72.project.service.GameService;
import com.inno72.project.vo.Inno72GameVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@RestController
@RequestMapping("/project/game")
@CrossOrigin
public class GameController {
	@Resource
	private GameService gameService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@Valid Inno72Game game, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
			} else {
				return gameService.saveModel(game);
			}
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		try {
			return gameService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72Game game) {
		try {
			return gameService.updateModel(game);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Game> detail(@RequestParam String id) {
		Inno72Game game = gameService.findById(id);
		return ResultGenerator.genSuccessResult(game);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String code,
			@RequestParam(required = false) String keyword) {
		List<Inno72GameVo> list = gameService.findByPage(code, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Game>> getList(Inno72Game model) {
		List<Inno72Game> list = gameService.getList(model);

		return ResultGenerator.genSuccessResult(list);
	}

}
