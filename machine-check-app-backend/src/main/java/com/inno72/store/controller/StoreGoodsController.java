package com.inno72.store.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.store.model.Inno72StoreGoods;
import com.inno72.store.service.StoreGoodsService;

import tk.mybatis.mapper.entity.Condition;

/**
* Created by CodeGenerator on 2018/12/28.
*/
@RestController
@RequestMapping("/store/goods")
public class StoreGoodsController {
    @Resource
    private StoreGoodsService storeGoodsService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> add(Inno72StoreGoods storeGoods) {
        storeGoodsService.save(storeGoods);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> delete(@RequestParam String id) {
        storeGoodsService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<String> update(Inno72StoreGoods storeGoods) {
        storeGoodsService.update(storeGoods);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result<Inno72StoreGoods> detail(@RequestParam String id) {
        Inno72StoreGoods storeGoods = storeGoodsService.findById(id);
        return ResultGenerator.genSuccessResult(storeGoods);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public ModelAndView list() {
   	   Condition condition = new Condition( Inno72StoreGoods.class);
        List<Inno72StoreGoods> list = storeGoodsService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
