package com.inno72.job;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inno72.common.CommonConstants;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.datetime.LocalDateUtil;
import com.inno72.model.Inno72AppMsg;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppMsgService;
import com.inno72.socketio.core.SocketHolder;

import tk.mybatis.mapper.entity.Condition;

@Component
public class SendMsgJob {
	@Autowired
	private AppMsgService appMsgService;
	private static String time = null;

	@Resource
	private IRedisUtil redisUtil;

	@Scheduled(fixedRate = 1000 * 5)
	public void sendMsg() {
		if (time == null) {
			time = LocalDateUtil.transfer(LocalDate.now());
		}
		Condition condition = new Condition(Inno72AppMsg.class);
		condition.createCriteria().andEqualTo("status", 0).andGreaterThanOrEqualTo("createTime", time);
		List<Inno72AppMsg> appMsgs = appMsgService.findByCondition(condition);
		if (appMsgs != null && !appMsgs.isEmpty()) {
			time = LocalDateTimeUtil.transfer(LocalDateTime.now());
		}
		for (Inno72AppMsg msg : appMsgs) {
			String machinKey = CommonConstants.REDIS_SESSION_PATH + msg.getMachineCode();
			String sessionId = redisUtil.get(machinKey);
			if (SocketHolder.have(sessionId)) {
				boolean result = false;
				if (msg.getMsgType() == 2) {
					result = SocketHolder.send(sessionId, "taskInfo", msg.getContent());
				}
				if (msg.getMsgType() == 3) {
					result = SocketHolder.send(sessionId, "commandInfo", msg.getContent());
				} else {
					result = SocketHolder.send(sessionId, msg.getContent());
				}
				if (result) {
					msg.setStatus(msg.getStatus() + 1);
					appMsgService.update(msg);
				}
			}

		}
	}
}
