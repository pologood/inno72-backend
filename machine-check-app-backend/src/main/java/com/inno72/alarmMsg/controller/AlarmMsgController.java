package com.inno72.alarmMsg.controller;

import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.service.AlarmMsgService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
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

	/**
	 * 查询消息列表
	 * @param alarmMsg
	 * @return
	 */
	@RequestMapping(value="list",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView list(@RequestBody Inno72AlarmMsg alarmMsg){
        List<Inno72AlarmMsg> list= alarmMsgService.findDataByPage(alarmMsg);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }

	/**
	 * 读取详情
	 * @param alarmMsg
	 * @return
	 */
	@RequestMapping(value = "readDetail")
    public Result<String> readDetail(@RequestBody Inno72AlarmMsg alarmMsg){
		Result<String> result = alarmMsgService.readDetail(alarmMsg);
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
	public Result<Integer> unReadCount(){
		Result<Integer> result = alarmMsgService.unReadCount();
		return result;
	}

}
