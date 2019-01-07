package com.inno72.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.MachineMonitorBackendProperties;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.SendMessageBean;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.SocketService;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;
import com.inno72.vo.AppMsgVo;

@RestController
@RequestMapping("/sendMsgToClient")
public class SendMsgToClientController {
	private static Logger logger = LoggerFactory.getLogger(SendMsgToClientController.class);

	@Resource
	private IRedisUtil redisUtil;

	@Autowired
	private SocketService socketService;
	@Autowired
	private MongoOperations mongoTpl;

	@Autowired
	private MachineMonitorBackendProperties machineMonitorBackendProperties;

	@RequestMapping(value = "/sendMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> sendMsg(@RequestBody SendMessageBean... msgs) {
		Map<String, Object> map = new HashMap<>();
		String url = machineMonitorBackendProperties.get("sendMsgUrl");
		for (SendMessageBean msg : msgs) {
			logger.info("客户端发送消息：{}", JSON.toJSONString(msg));
			String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(msg)));
			AppMsgVo vo = new AppMsgVo();
			vo.setData(result);
			vo.setIsQueue(1);
			vo.setTargetCode(msg.getMachineId());
			vo.setTargetType("machine");
			try {
				String r = HttpClient.post(url, JSON.toJSONString(vo));
				if (!StringUtil.isEmpty(r)) {
					JSONObject $_result = JSON.parseObject(result);
					if ($_result.getInteger("code") == 0) {
						map.put(msg.getMachineId(), "发送成功");
					} else {
						map.put(msg.getMachineId(), "发送失败");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put(msg.getMachineId(), "发送失败");
			}
		}
		return ResultGenerator.genSuccessResult(map);
	}

	@RequestMapping(value = "/sendMsgStr", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> sendMsgStr(@RequestBody Map<String, Object> param) {
		Map<String, Object> map = new HashMap<>();
		logger.info("客户端发送消息：{}", JSON.toJSONString(param));
		String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(param.get("msg"))));
		String url = machineMonitorBackendProperties.get("sendMsgUrl");
		String machineCode = (String) param.get("machineCode");
		AppMsgVo vo = new AppMsgVo();
		vo.setData(result);
		vo.setIsQueue(1);
		vo.setTargetCode(machineCode);
		vo.setTargetType("machine");
		try {
			String r = HttpClient.post(url, JSON.toJSONString(vo));
			if (!StringUtil.isEmpty(r)) {
				JSONObject $_result = JSON.parseObject(result);
				if ($_result.getInteger("code") == 0) {
					map.put(machineCode, "发送成功");
				} else {
					map.put(machineCode, "发送失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(machineCode, "发送失败");
		}
		return ResultGenerator.genSuccessResult(map);
	}

}
