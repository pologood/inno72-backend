package com.inno72.socketio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOStartHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	private SocketServerHandler socketServerHandler() {

		return new SocketServerHandler() {

			@Override
			public String process(String key, String data, Map<String, List<String>> params) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void heartBeatResponse(String key, String data, Map<String, List<String>> params) {
				// TODO Auto-generated method stub

			}

			@Override
			public void connectNotify(String key, Map<String, List<String>> data) {
				log.info("socket连接上");
			}

			@Override
			public boolean isExceptionClose(String key, Map<String, List<String>> data) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int exceptionCloseWaitTimeSeconds() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void closeNotify(String key, boolean exception, Map<String, List<String>> data) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getCurrentSessionId(Map<String, List<String>> data) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}

	@Bean
	public SocketServer socketServer() {
		return new SocketServer("0.0.0.0", 1234, socketServerHandler());
	}

}
