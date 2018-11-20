package com.inno72.alarmMsg.controller;

import com.alibaba.fastjson.JSON;
import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.service.AlarmMsgService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/alarm/msg")
@CrossOrigin
@RestController
public class AlarmMsgController {


    @Resource
    private AlarmMsgService alarmMsgService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 查询消息列表
	 * @param alarmMsg
	 * @return
	 */
	@RequestMapping(value="list",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView list(@RequestBody Inno72AlarmMsg alarmMsg){
		logger.info("查询消息列表接口参数：{}", JSON.toJSON(alarmMsg));
        List<Inno72AlarmMsg> list= alarmMsgService.findDataByPage(alarmMsg);
		logger.info("查询消息列表接口结果：{}", JSON.toJSON(list));
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

	/**
	 * 读取详情
	 * @param alarmMsg
	 * @return
	 */
	@RequestMapping(value = "readDetail")
    public Result<String> readDetail(@RequestBody Inno72AlarmMsg alarmMsg){
		logger.info("消息设为已读接口参数：{}", JSON.toJSON(alarmMsg));
		Result<String> result = alarmMsgService.readDetail(alarmMsg);
		logger.info("消息设为已读接口结果：{}", JSON.toJSON(result));
    	return result;
	}

	/**
	 *初始化历史数据
	 * @return
	 */
	@RequestMapping(value = "initData")
	public Result<String> initData(){
		Result<String> result = alarmMsgService.initData();
		return result;
	}

	/**
	 * 获取未读消息数量
	 * @return
	 */
	@RequestMapping(value = "unReadCount")
	public Result<Integer> unReadCount(@RequestBody Inno72AlarmMsg alarmMsg){
		logger.info("消息未读数量接口参数：{}", JSON.toJSON(alarmMsg));
		Result<Integer> result = alarmMsgService.unReadCount(alarmMsg);
		logger.info("消息未读数量接口结果：{}", JSON.toJSON(result));
		return result;
	}

	/**
	 * 全部已读
	 * @param alarmMsg
	 * @return
	 */
	@RequestMapping(value="readAll")
	public Result<String> readAll(@RequestBody Inno72AlarmMsg alarmMsg){
		Result<String> result = alarmMsgService.readAll(alarmMsg);
		return result;
	}

}
