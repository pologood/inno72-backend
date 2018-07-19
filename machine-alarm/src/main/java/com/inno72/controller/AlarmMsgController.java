package com.inno72.controller;

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
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.service.AlarmMsgService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/19.
 */
@RestController
@RequestMapping("/alarm/msg")
public class AlarmMsgController {
	@Resource
	private AlarmMsgService alarmMsgService;

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> add(Inno72AlarmMsg alarmMsg) {
		alarmMsgService.save(alarmMsg);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> delete(@RequestParam String id) {
		alarmMsgService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> update(Inno72AlarmMsg alarmMsg) {
		alarmMsgService.update(alarmMsg);
		return ResultGenerator.genSuccessResult();
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Inno72AlarmMsg> detail(@RequestParam String id) {
		Inno72AlarmMsg alarmMsg = alarmMsgService.findById(id);
		return ResultGenerator.genSuccessResult(alarmMsg);
	}

	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView list() {
		System.out.println("===================");
		Condition condition = new Condition(Inno72AlarmMsg.class);
		List<Inno72AlarmMsg> list = alarmMsgService.findByPage(condition);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}
}
