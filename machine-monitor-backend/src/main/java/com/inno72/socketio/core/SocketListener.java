package com.inno72.socketio.core;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

/**
 * socket消息监听
 * 
 * @author lzh
 *
 */
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
				Map<String, List<String>> param = client.getHandshakeData().getUrlParams();
				SocketHolder.bind(id, client);
				handler.connectNotify(id, param);
			}
		};
	}

	DisconnectListener disconnect() {
		return new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				String id = client.getSessionId().toString();
				logger.info("{}断开连接", id);
				client.disconnect();
				SocketHolder.remove(id);
				handler.closeNotify(id, client.getHandshakeData().getUrlParams());
			}
		};
	}

	/**
	 * 接收到客户端主动发送消息并回执
	 * 
	 * @return
	 */
	DataListener<String> message() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				logger.info("连接ID【{}】接收到【{}】发送的数据【{}】", client.getSessionId(), client.getRemoteAddress(), data);
				handler.process(client.getSessionId().toString(), data, client.getHandshakeData().getUrlParams());
			}
		};
	}

	/**
	 * 接收到客户端主动发送消息并回执
	 *
	 * @return
	 */
	DataListener<String> msg() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
				logger.info("连接ID【{}】接收到【{}】发送的数据【{}】", client.getSessionId(), client.getRemoteAddress(), data);
				String result = handler.deviceIdMsg(client.getSessionId().toString(), data,
						client.getHandshakeData().getUrlParams());
				client.sendEvent("deviceIdMsg", result);
			}
		};
	}

	/**
	 * 接收到主动发起查询监控的消息，不需要回执
	 * 
	 * @return
	 */
	DataListener<String> monitor() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest arg2) throws Exception {
				logger.info("收到监控返回数据:{}", data);
				handler.monitorResponse(client.getSessionId().toString(), data,
						client.getHandshakeData().getUrlParams());
			}
		};
	}

	String handlerClassName() {
		return this.handler.getClass().getName();
	}

}
