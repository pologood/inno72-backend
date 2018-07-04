package com.inno72.socketio.core;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * socket服务端
 * 
 * @author lzh
 *
 */
public class SocketServer {
	Logger logger = LoggerFactory.getLogger(SocketServer.class);

	private int port = 9902;
	private String hostname = "localhost";

	public SocketServer(String hostname, int port, SocketServerHandler handler) {
		this.port = port;
		this.hostname = hostname;
		this.listener = new SocketListener(handler);
		init();
	}

	private final SocketListener listener;

	private static SocketIOServer server = null;

	void init() {
		logger.info("准备启动SocketIO服务，端口{},业务实现Class{}", port, listener.handlerClassName());
		Configuration config = new Configuration();
		config.setHostname(hostname);
		config.setPort(port);
		// config.setPingTimeout(1000);
		// config.setPingInterval(1100);
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setTcpKeepAlive(false);
		config.setSocketConfig(socketConfig);
		server = new SocketIOServer(config);
		server.addConnectListener(listener.connect());
		server.addDisconnectListener(listener.disconnect());
		server.addEventListener("message", String.class, listener.message());
		server.addEventListener("monitor", String.class, listener.monitor());
		//deviceId message
		server.addEventListener("deviceIdMsg", String.class, listener.msg());

		server.start();
		Thread shutdownHookOne = new Thread() {
			@Override
			public void run() {
				server.stop();
				logger.info("SocketIO服务监听端口{}关闭", port);
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHookOne);
	}

	protected static SocketIOClient getClient(String sessionId) {
		if (sessionId != null && !"".equals(sessionId)) {
			return server.getClient(UUID.fromString(sessionId));
		}
		return null;
	}

}
