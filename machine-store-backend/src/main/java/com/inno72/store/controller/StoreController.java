package com.inno72.store.controller;

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
import com.inno72.store.model.Inno72Store;
import com.inno72.store.service.StoreService;
import com.inno72.store.vo.StoreVo;

/**
 * Created by CodeGenerator on 2018/12/28.
 */
@RestController
@RequestMapping("/store/store")
@CrossOrigin
public class StoreController {
	@Resource
	private StoreService storeService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> add(StoreVo store) {
		return storeService.saveModel(store);
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		storeService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Object> update(StoreVo store) {
		return storeService.updateModel(store);
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72Store> detail(@RequestParam String id) {
		Inno72Store store = storeService.findById(id);
		return ResultGenerator.genSuccessResult(store);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(String keyword) {
		List<StoreVo> list = storeService.findByPage(keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
