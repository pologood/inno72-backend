package com.inno72.socketio;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class SocketListener {

	private SocketServerHandler handler;

	Logger logger = LoggerFactory.getLogger(SocketListener.class);

	public SocketListener(SocketServerHandler handler) {
		this.handler = handler;
	}

	ConnectListener connect() {
		return new ConnectListener() {
			@Override
			public void onConnect(SocketIOClient client) {
				logger.info("【{}】连接到服务器", client.getRemoteAddress());
				String id = client.getSessionId().toString();
				logger.info("连接ID ：{}", id);
				SocketHolder.bind(id, client);
				handler.connectNotify(id, client.getHandshakeData().getUrlParams());
			}
		};
	}

	DisconnectListener disconnect() {
		return new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				String key = client.getSessionId().toString();
				logger.info("{}断开连接", key);
				client.disconnect();
				SocketHolder.remove(key);
				if (handler.isExceptionClose(key, client.getHandshakeData().getUrlParams())) {
					logger.info("{}异常断开，{}秒后重新检查", key, handler.exceptionCloseWaitTimeSeconds());
					new java.util.Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							String sessionId = handler.getCurrentSessionId(client.getHandshakeData().getUrlParams());
							if (sessionId == null || SocketServer.getClient(sessionId) == null) {
								logger.info("{}未重新连接到服务器", key);
								handler.closeNotify(key, true, client.getHandshakeData().getUrlParams());
							}
						}
					}, handler.exceptionCloseWaitTimeSeconds() * 1000);
				} else {
					handler.closeNotify(key, false, client.getHandshakeData().getUrlParams());
				}
			}
		};
	}

	DataListener<String> message() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				logger.info("连接ID【{}】接收到【{}】发送的数据【{}】", client.getSessionId(), client.getRemoteAddress(), data);
				String result = handler.process(client.getSessionId().toString(), data,
						client.getHandshakeData().getUrlParams());
				client.sendEvent("message", result);
			}
		};
	}

	DataListener<String> heart() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest arg2) throws Exception {
				logger.info("收到消息心跳返回:" + data);
				handler.heartBeatResponse(client.getSessionId().toString(), data,
						client.getHandshakeData().getUrlParams());
			}
		};
	}

	String handlerClassName() {
		return this.handler.getClass().getName();
	}

}
