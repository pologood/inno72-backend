package com.inno72.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inno72.common.CommonConstants;
import com.inno72.model.Inno72AppMsg;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AppMsgService;
import com.inno72.socketio.core.SocketHolder;

import tk.mybatis.mapper.entity.Condition;

@Component
public class SendMsgJob {
	@Autowired
	private AppMsgService appMsgService;

	@Resource
	private IRedisUtil redisUtil;

	@Scheduled(fixedRate = 1000 * 5)
	public void sendMsg() {
		Condition condition = new Condition(Inno72AppMsg.class);
		condition.createCriteria().andEqualTo("status", 0);
		List<Inno72AppMsg> appMsgs = appMsgService.findByCondition(condition);
		for (Inno72AppMsg msg : appMsgs) {
			String machinKey = CommonConstants.REDIS_BASE_PATH + msg.getMachineCode();
			String sessionId = redisUtil.get(machinKey);
			if (SocketHolder.have(sessionId)) {
				boolean result = SocketHolder.send(sessionId, msg.getContent());
				if (result) {
					msg.setStatus(msg.getStatus() + 1);
					appMsgService.update(msg);
				}
			}

		}
	}
}
