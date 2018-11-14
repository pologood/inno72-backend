package com.inno72.Interact.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.inno72.Interact.service.InteractService;
import com.inno72.Interact.vo.InteractListVo;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
@RestController
@RequestMapping("/AAA")
@CrossOrigin
public class AAAController {
	@Resource
	private InteractService interactService;

	@Resource
	private GameServiceFeignClient gameServiceFeignClient;

	@RequestMapping(value = "/list", method = { RequestMethod.POST })
	public ModelAndView list() {
		String s = gameServiceFeignClient.setLogged("sssss", "ssssssss");
		System.out.println(s);
		List<InteractListVo> list = null;
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

}
