package com.inno72.socketio;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

import com.alibaba.fastjson.JSON;
import com.inno72.model.MessageBean;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.socketio.core.SocketServer;
import com.inno72.socketio.core.SocketServerHandler;

@Configuration
public class SocketIOStartHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoOperations mongoTpl;

	@Resource
	private IRedisUtil redisUtil;

	private SocketServerHandler socketServerHandler() {

		return new SocketServerHandler() {

			@Override
			public String process(String key, String data, String param) {

				log.info("获取机器Id方法开始,sessionId=" + key + ",deviceId" + param);

				// 变量
				String machineId;

				// 根据从缓存中取机器Id，取不到从数据库中取
				if (redisUtil.exists(key) == false) {
					// 根据deviceId获取机器Id
					machineId = HttpClient.post("http://localhost:8880/machine/generateMachineId?deviceId=1", "");
					// 存入redis中
					redisUtil.set(key, machineId);
				} else {
					machineId = redisUtil.get(key);
				}

				log.info("获取机器Id方法结束,sessionId=" + key + ",machineId=" + machineId + ",deviceId" + param);

				MessageBean ms = JSON.parseObject(data, MessageBean.class);
				if (ms.getEventType() == MessageBean.EventType.CHECKSTATUS.v()) {
				}
				return machineId;
			}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				log.info("推送监控消息方法执行开始" + data);
				// 解压缩以及解密数据

				// 解析数据

				// 存入mongoDB数据库中

				log.info("推送监控消息方法执行结束" + data);
			}

			@Override
			public void connectNotify(String key, Map<String, List<String>> data) {
				log.info("socket连接上");
			}

			@Override
			public void closeNotify(String key, Map<String, List<String>> data) {
				log.info("socket 断开了");
			}

		};
	}

	@Bean
	public SocketServer socketServer() {
		return new SocketServer("0.0.0.0", 1237, socketServerHandler());
	}

	public static void main(String[] args) throws Exception {
		String string = HttpClient.post("http://172.16.18.240:8880/machine/generateMachineId?id=1", "");
		System.out.println("我的返回值是" + string);
	}

}
