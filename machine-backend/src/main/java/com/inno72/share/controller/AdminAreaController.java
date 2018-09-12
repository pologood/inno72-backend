package com.inno72.share.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.share.model.Inno72AdminArea;
import com.inno72.share.service.AdminAreaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/admin/area")
@CrossOrigin
public class AdminAreaController {
	@Resource
	private AdminAreaService adminAreaService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 添加三级区域
	 * @param adminArea
	 * @return
	 */
	@RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String> add(@RequestBody Inno72AdminArea adminArea) {
		logger.info("添加区域接口参数：{}", JSON.toJSON(adminArea));
		Result<String> result = adminAreaService.saveArea(adminArea);
		logger.info("添加区域接口结果：{}", JSON.toJSON(result));
		return result;
	}
	@RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String> delete(@RequestParam String id) {
		adminAreaService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	/**
	 * 修改三级区域
	 * @param adminArea
	 * @return
	 */
	@RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<String> update(@RequestBody Inno72AdminArea adminArea) {
		logger.info("修改区域接口参数：{}", JSON.toJSON(adminArea));
		Result<String> result = adminAreaService.updateArea(adminArea);
		return result;
	}

	/**
	 * 查询区域详情
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<Inno72AdminArea> detail(@RequestParam String code) {
		logger.info("查询区域详情接口参数：{}", code);
		Inno72AdminArea adminArea = adminAreaService.findById(code);
		return ResultGenerator.genSuccessResult(adminArea);
	}

	/**
	 * 三级区域地址列表
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/pageList", method = { RequestMethod.POST,  RequestMethod.GET})
	public ModelAndView pageList(@RequestParam String code) {
		logger.info("分页查询区域接口参数：{}", code);
		List<Inno72AdminArea> list = adminAreaService.findByPage(code);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

	/**
	 * 联动查询
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
	public Result<List<Inno72AdminArea>> list(String code) {
		logger.info("联动查询区域接口参数：{}", code);
		//联动查询
		List<Inno72AdminArea> list = adminAreaService.getLiset(code);
		return ResultGenerator.genSuccessResult(list);
	}
}
