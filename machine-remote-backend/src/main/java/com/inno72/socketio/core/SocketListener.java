package com.inno72.socketio.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.inno72.common.CommonConstants;
import com.inno72.common.utils.StringUtil;

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
				String sessionId = client.getSessionId().toString();
				Map<String, List<String>> param = client.getHandshakeData().getUrlParams();
				String machineId = Optional.ofNullable(param.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				String deviceId = Optional.ofNullable(param.get(CommonConstants.DEVICE_ID)).map(a -> a.get(0))
						.orElse("");
				String connectType = Optional.ofNullable(param.get(CommonConstants.CONNECT_TYPE)).map(a -> a.get(0))
						.orElse("");
				if (StringUtil.isEmpty(connectType)) {
					client.disconnect();
					logger.info("connectType为空，强制断开连接。");
					return;
				}
				if (CommonConstants.CONNECT_TYPE_CLIENT.toString().equals(connectType)) {
					if (StringUtil.isEmpty(machineId)) {
						client.disconnect();
						logger.info("客户端连接到服务器，没有machineId，强制断开连接。");
						return;
					}
					if (StringUtil.isEmpty(deviceId)) {
						client.disconnect();
						logger.info("客户端连接到服务器，没有deviceId，强制断开连接。");
						return;
					}
					logger.info("客户端连接到服务器，机器id:{},设备id:{}", machineId, deviceId);
				} else if (CommonConstants.CONNECT_TYPE_MACHINE.toString().equals(connectType)) {
					if (StringUtil.isEmpty(machineId)) {
						client.disconnect();
						logger.info("机器连接到服务器，没有machineId，强制断开连接。");
						return;
					}
					logger.info("机器端连接到服务器，机器id:{}", machineId);
				} else {
					client.disconnect();
					logger.info("connectType错误，强制断开连接。");
					return;
				}

				SocketHolder.bind(sessionId, client);
				handler.connectNotify(sessionId, param);
			}
		};
	}

	DisconnectListener disconnect() {
		return new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				String sessionId = client.getSessionId().toString();
				Map<String, List<String>> param = client.getHandshakeData().getUrlParams();
				String machineId = Optional.ofNullable(param.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				String deviceId = Optional.ofNullable(param.get(CommonConstants.DEVICE_ID)).map(a -> a.get(0))
						.orElse("");
				String connectType = Optional.ofNullable(param.get(CommonConstants.CONNECT_TYPE)).map(a -> a.get(0))
						.orElse("");
				if (CommonConstants.CONNECT_TYPE_CLIENT.equals(connectType)) {
					logger.info("客户端断开连接，机器id:{},设备id:{}", machineId, deviceId);
				} else if (CommonConstants.CONNECT_TYPE_MACHINE.equals(connectType)) {
					logger.info("机器断开连接，机器id:{}", machineId);
				}
				client.disconnect();
				SocketHolder.remove(sessionId);
				handler.closeNotify(sessionId, client.getHandshakeData().getUrlParams());
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
				logger.info("连接ID【{}】接收到【{}】发送的数据", client.getSessionId(), client.getRemoteAddress());
				handler.process(client.getSessionId().toString(), data, client.getHandshakeData().getUrlParams());
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
				handler.monitorResponse(client.getSessionId().toString(), data,
						client.getHandshakeData().getUrlParams());
			}
		};
	}

	DataListener<byte[]> remote() {
		return new DataListener<byte[]>() {

			@Override
			public void onData(SocketIOClient client, byte[] data, AckRequest ackSender) throws Exception {
				handler.remoteResponse(client.getSessionId().toString(), data,
						client.getHandshakeData().getUrlParams());
			}

		};
	}

	DataListener<String> monitorEvent() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest arg2) throws Exception {
				handler.monitorEvent(client.getSessionId().toString(), data, client.getHandshakeData().getUrlParams());
			}
		};
	}

	DataListener<String> keyEvent() {
		return new DataListener<String>() {
			@Override
			public void onData(SocketIOClient client, String data, AckRequest arg2) throws Exception {
				handler.keyEvent(client.getSessionId().toString(), data, client.getHandshakeData().getUrlParams());
			}
		};
	}

	String handlerClassName() {
		return this.handler.getClass().getName();
	}
}
