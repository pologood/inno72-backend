package com.inno72.game.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.game.model.Inno72Game;
import com.inno72.game.service.GameService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/06/29.
*/
@RestController
@RequestMapping("/game/game")
@CrossOrigin
public class GameController {
    @Resource
    private GameService gameService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72Game game) {
    	try {
    		return gameService.saveModel(game);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
    	try {
    		return gameService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72Game game) {
    	try {
    		return gameService.updateModel(game);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72Game> detail(@RequestParam String id) {
        Inno72Game game = gameService.findById(id);
        return ResultGenerator.genSuccessResult(game);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list(@RequestParam(required=false) String code,@RequestParam(required=false) String keyword) {
        List<Inno72Game> list = gameService.findByPage(code,keyword);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
    
    @RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<List<Inno72Game>> getList(Inno72Game model) {
        List<Inno72Game> list = gameService.getList(model);
        
        return ResultGenerator.genSuccessResult(list);
    }
   
}
