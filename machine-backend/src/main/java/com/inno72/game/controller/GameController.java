package com.inno72.game.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.game.model.Inno72Game;
import com.inno72.game.service.GameService;
import com.inno72.gameUser.model.Inno72GameUser;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;


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
    		gameService.save(game);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
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
    		gameService.update(game);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
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
    
    
    @RequestMapping(value = "/matchMachine", method = { RequestMethod.POST, RequestMethod.GET })
    public Result<String> matchMachine(@RequestParam(required=false) String gameId,@RequestParam(required=false) String machineIds) {
    	gameService.matchMachine(gameId, machineIds);
    	try {
    		gameService.matchMachine(gameId, machineIds);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
        return ResultGenerator.genSuccessResult();
    }
}
