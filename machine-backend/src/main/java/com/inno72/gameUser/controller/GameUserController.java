package com.inno72.gameUser.controller;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.gameUser.model.Inno72GameUser;
import com.inno72.gameUser.service.GameUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

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
    public ModelAndView list() {
        Condition condition = new Condition(Inno72GameUser.class);
        List<Inno72GameUser> list = gameUserService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

}
