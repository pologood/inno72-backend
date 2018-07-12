package com.inno72.machine.controller;

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
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.service.LocaleService;
import com.inno72.machine.vo.Inno72LocaleVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
@RestController
@RequestMapping("/machine/locale")
@CrossOrigin
public class LocaleController {
	@Resource
	private LocaleService localeService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(@Valid Inno72Locale locale, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
			} else {
				localeService.save(locale);
			}
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		try {
			return localeService.delById(id);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72Locale locale) {
		try {
			localeService.update(locale);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("操作失败！");
		}
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72LocaleVo> detail(@RequestParam String id) {
		Inno72LocaleVo locale = localeService.findById(id);
		return ResultGenerator.genSuccessResult(locale);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list(@RequestParam(required = false) String code,
			@RequestParam(required = false) String keyword) {
		List<Inno72LocaleVo> list = localeService.findByPage(code, keyword);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	@RequestMapping(value = "/getList", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<List<Inno72Locale>> getList(Inno72Locale locale) {
		List<Inno72Locale> list = localeService.getList(locale);
		return ResultGenerator.genSuccessResult(list);
	}
}
