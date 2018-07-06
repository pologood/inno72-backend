package com.inno72.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.redis.IRedisUtil;
import com.inno72.socketio.core.SocketHolder;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;

/**
 * @Auther: wxt
 * @Date: 2018/7/5 19:02
 * @Description:发送消息到客户端
 */
@RestController
@RequestMapping("/sendMsgToClient")
public class SendMsgToClientController {

	@Resource
	private IRedisUtil redisUtil;

	@RequestMapping(value = "/sendEvent", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<String> sendMsg(@RequestParam String machineId, @RequestParam String eventType,
			@RequestParam String subEventType, @RequestParam String data) {

		// 组装数据
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("eventType", eventType);
		jsonObject.put("subEventType", subEventType);
		jsonObject.put("data", data);
		// 加密并压缩
		String result = GZIPUtil.compress(AesUtils.encrypt(jsonObject.toString()));
		// 从redis中取出sessionId
		String machinKey = CommonConstants.REDIS_BASE_PATH + machineId;
		String sessionId = redisUtil.get(machinKey);

		SocketHolder.send(sessionId, result);

		return ResultGenerator.genSuccessResult();
	}
}
