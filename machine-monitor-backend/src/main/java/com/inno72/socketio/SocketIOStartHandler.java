package com.inno72.socketio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inno72.socketio.core.SocketServer;
import com.inno72.socketio.core.SocketServerHandler;

@Configuration
public class SocketIOStartHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	private SocketServerHandler socketServerHandler() {

		return new SocketServerHandler() {

			@Override
			public String process(String key, String data, Map<String, List<String>> params) {
				log.info("推送自定义消息" + data);

				return "jiqiid";
			}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				log.info("推送监控信息" + data);
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

}
