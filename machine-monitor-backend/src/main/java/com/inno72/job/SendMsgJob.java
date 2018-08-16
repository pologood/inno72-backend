package com.inno72.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inno72.model.Inno72AppMsg;
import com.inno72.service.AppMsgService;
import com.inno72.socketio.core.SocketHolder;

import tk.mybatis.mapper.entity.Condition;

@Component
public class SendMsgJob {
	@Autowired
	private AppMsgService appMsgService;

	@Scheduled(fixedRate = 1000 * 5)
	public void sendMsg() {
		Condition condition = new Condition(Inno72AppMsg.class);
		condition.createCriteria().andEqualTo("status", 0);
		List<Inno72AppMsg> appMsgs = appMsgService.findByCondition(condition);
		for (Inno72AppMsg msg : appMsgs) {
			if (SocketHolder.have(msg.getMachineCode())) {
				boolean result = SocketHolder.send(msg.getMachineCode(), msg.getContent());
				if (result) {
					msg.setStatus(msg.getStatus() + 1);
					appMsgService.update(msg);
				}
			}

		}
	}
}
