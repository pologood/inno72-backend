package com.inno72.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inno72.common.CommonConstants;
import com.inno72.model.SocketAction;
import com.inno72.model.SocketConnectionBean;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.SocketService;

@Service
public class SocketServiceImpl implements SocketService {

	@Resource
	private IRedisUtil redisUtil;

	@Override
	public void manageSocket(SocketAction action, String machineCode, String sessionId) {
		String key = CommonConstants.REDIS_CONNECTION_PATH;
		Set<Object> connects = redisUtil.smembers(key);
		SocketConnectionBean bean = get(machineCode, connects);
		switch (action) {
		case CONNECT:
			if (bean == null) {
				bean = new SocketConnectionBean();
				bean.setConnectTime(LocalDateTime.now());
				bean.setStatus(1);
				bean.setMachineCode(machineCode);
				bean.setSessionId(key);
				redisUtil.sadd(key, bean);
			} else {
				redisUtil.srem(key, bean);
				bean.setStatus(1);
				bean.setSessionId(key);
				if (bean.getStatus() == 3) {
					bean.setConnectTime(LocalDateTime.now());
					bean.setDisConnectTime(null);
				}
				redisUtil.sadd(key, bean);
			}

			break;
		case DISCONNECT:
			if (bean == null) {
				bean = new SocketConnectionBean();
				bean.setConnectTime(null);
				bean.setStatus(3);
				bean.setMachineCode(machineCode);
				bean.setSessionId(key);
				redisUtil.sadd(key, bean);
			} else {
				redisUtil.srem(key, bean);
				bean.setStatus(2);
				bean.setSessionId(key);
				bean.setDisConnectTime(LocalDateTime.now());
				redisUtil.sadd(key, bean);
			}
			break;
		case HERT:
			if (bean == null) {
				bean = new SocketConnectionBean();
				bean.setConnectTime(LocalDateTime.now());
				bean.setStatus(1);
				bean.setMachineCode(machineCode);
				bean.setSessionId(key);
				redisUtil.sadd(key, bean);
			} else {
				redisUtil.srem(key, bean);
				if (bean.getStatus() == 2) {
					LocalDateTime disConnetTime = bean.getDisConnectTime();
					if (disConnetTime != null) {
						LocalDateTime now = LocalDateTime.now();
						long time = java.time.Duration.between(disConnetTime, now).toMinutes();
						if (time > 5) {
							bean.setConnectTime(LocalDateTime.now());
						}
						bean.setDisConnectTime(null);
					}
					bean.setStatus(1);
					bean.setSessionId(sessionId);
					redisUtil.sadd(key, bean);
				} else if (bean.getStatus() == 3) {
					bean.setConnectTime(LocalDateTime.now());
					bean.setDisConnectTime(null);
					bean.setStatus(1);
					bean.setSessionId(sessionId);
					redisUtil.sadd(key, bean);
				}

			}
			return;
		default:
			break;
		}
	}

	public SocketConnectionBean get(String machineCode, Set<Object> connects) {
		for (Object ob : connects) {
			SocketConnectionBean bean = (SocketConnectionBean) ob;
			if (machineCode.equals(bean.getMachineCode())) {
				return bean;
			}
		}
		return null;
	}

}
