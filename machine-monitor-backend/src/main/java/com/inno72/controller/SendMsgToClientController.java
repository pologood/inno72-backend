package com.inno72.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.utils.StringUtil;
import com.inno72.model.Inno72AppMsg;
import com.inno72.model.SendMessageBean;
import com.inno72.model.SystemStatus;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppMsgService;
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

	@Autowired
	private AppMsgService appMsgService;

	@Autowired
	private MongoOperations mongoTpl;

	@RequestMapping(value = "/sendMsg", method = { RequestMethod.POST, RequestMethod.GET })
	public Result<Map<String, Object>> sendMsg(@RequestBody SendMessageBean... msgs) {
		Map<String, Object> map = new HashMap<>();
		for (SendMessageBean msg : msgs) {
			logger.info("客户端发送消息：{}", JSON.toJSONString(msg));
			String result = GZIPUtil.compress(AesUtils.encrypt(JSON.toJSONString(msg)));
			Query querySystemStatus = new Query();
			querySystemStatus.addCriteria(Criteria.where("machineId").is(msg.getMachineId()));
			List<SystemStatus> l = mongoTpl.find(querySystemStatus, SystemStatus.class, "SystemStatus");
			logger.info(JSON.toJSONString(l));
			if (l != null && l.size() > 0) {
				SystemStatus status = l.get(0);
				LocalDateTime createTime = status.getCreateTime();
				Duration duration = Duration.between(createTime, LocalDateTime.now());
				long between = duration.toMinutes();
				logger.info(between + "");
				if (between > 2) {
					map.put(msg.getMachineId(), "发送失败");
				} else {
					Inno72AppMsg msg1 = new Inno72AppMsg();
					msg1.setId(StringUtil.uuid());
					msg1.setCreateTime(LocalDateTime.now());
					msg1.setMachineCode(msg.getMachineId());
					msg1.setContent(result);
					msg1.setStatus(0);
					appMsgService.save(msg1);
					map.put(msg.getMachineId(), "发送成功");
				}
			} else {
				map.put(msg.getMachineId(), "发送失败");
			}

		}
		return ResultGenerator.genSuccessResult(map);
	}

}
