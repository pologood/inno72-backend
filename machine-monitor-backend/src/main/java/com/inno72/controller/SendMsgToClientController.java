package com.inno72.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.SendMessageBean;
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
	private static Logger logger = LoggerFactory.getLogger(SendMsgToClientController.class);

	@Resource
	private IRedisUtil redisUtil;

	@RequestMapping(value = "/sendMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> sendMsg(@RequestBody SendMessageBean... msgs) {
		Map<String, Object> map = new HashMap<>();
		for (SendMessageBean msg : msgs) {
			logger.info("客户端发送消息：{}", JSON.toJSONString(msg));
			String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(msg)));
			String machinKey = CommonConstants.REDIS_BASE_PATH + msg.getMachineId();
			String sessionId = redisUtil.get(machinKey);
			if (!com.inno72.common.utils.StringUtil.isEmpty(sessionId)) {
				boolean sr = SocketHolder.send(sessionId, result);
				map.put(msg.getMachineId(), sr ? "发送成功" : "发送失败");
			} else {
				map.put(msg.getMachineId(), "发送失败");

			}
		}
		return ResultGenerator.genSuccessResult(map);
	}

}
