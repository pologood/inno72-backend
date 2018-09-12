package com.inno72.socketio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inno72.common.CommonConstants;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72AppMsgMapper;
import com.inno72.model.Inno72AppMsg;
import com.inno72.redis.IRedisUtil;
import com.inno72.socketio.core.SocketHolder;
import com.inno72.socketio.core.SocketServer;
import com.inno72.socketio.core.SocketServerHandler;
import com.inno72.util.AesUtils;
import com.inno72.util.GZIPUtil;

@Configuration
public class SocketIOStartHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IRedisUtil redisUtil;

	@Resource
	private Inno72AppMsgMapper inno72AppMsgMapper;

	private SocketServerHandler socketServerHandler() {

		return new SocketServerHandler() {

			@Override
			public void process(String key, String data, Map<String, List<String>> params) {

			}

			@Override
			public void monitorResponse(String key, String data, Map<String, List<String>> params) {
				String deviceId = Optional.ofNullable(params.get(CommonConstants.DEVICE_ID)).map(a -> a.get(0))
						.orElse("");
				log.info("客户端发送心跳deviceId:{}", deviceId);
				String deviceIdKey = CommonConstants.REDIS_REMOTE_CLIENT_SESSION_PATH + deviceId;
				redisUtil.setex(deviceIdKey, 60, key);
			}

			@Override
			public void connectNotify(String sessionId, Map<String, List<String>> data) {
				String connectType = Optional.ofNullable(data.get(CommonConstants.CONNECT_TYPE)).map(a -> a.get(0))
						.orElse("");
				String machineId = Optional.ofNullable(data.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				if (CommonConstants.CONNECT_TYPE_CLIENT.equals(connectType)) {
					String deviceId = Optional.ofNullable(data.get(CommonConstants.DEVICE_ID)).map(a -> a.get(0))
							.orElse("");
					log.info("socket客户端连接到服务器，deviceId:{}------->机器machineId:{}", deviceId, machineId);
					String bindKey = CommonConstants.REDIS_REMOTE_BIND_PATH + deviceId;
					String rbindKey = CommonConstants.REDIS_REMOTE_RBIND_PATH + machineId;

					redisUtil.set(bindKey, machineId);
					redisUtil.sadd(rbindKey, deviceId);

					String deviceIdKey = CommonConstants.REDIS_REMOTE_CLIENT_SESSION_PATH + deviceId;
					redisUtil.setex(deviceIdKey, 60, sessionId);

					String result = GZIPUtil.compress(AesUtils.encrypt(machineId));
					Inno72AppMsg msg1 = new Inno72AppMsg();
					msg1.setId(StringUtil.uuid());
					msg1.setCreateTime(LocalDateTime.now());
					msg1.setMachineCode(machineId);
					msg1.setContent(result);
					msg1.setStatus(0);
					msg1.setMsgType(9);
					inno72AppMsgMapper.insert(msg1);
				} else {
					log.info("socket机器端连接到服务器，机器machineId:{}", machineId);
					String machinKey = CommonConstants.REDIS_REMOTE_MACHINE_SESSION_PATH + machineId;
					redisUtil.setex(machinKey, 60 * 60, sessionId);
				}
			}

			@Override
			public void closeNotify(String key, Map<String, List<String>> data) {
			}

			@Override
			public void remoteResponse(String key, byte[] data, Map<String, List<String>> urlParams) {
				String machineId = Optional.ofNullable(urlParams.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				log.info("收到机器{}发送的远程图片", machineId);
				String machinKey = CommonConstants.REDIS_REMOTE_MACHINE_SESSION_PATH + machineId;
				redisUtil.setex(machinKey, 60, key);
				String rbindKey = CommonConstants.REDIS_REMOTE_RBIND_PATH + machineId;
				Set<Object> clients = redisUtil.smembers(rbindKey);
				if (clients != null) {
					boolean r = false;
					for (Object client : clients) {
						if (client != null && !StringUtil.isEmpty(client.toString())) {
							String deviceIdKey = CommonConstants.REDIS_REMOTE_CLIENT_SESSION_PATH + client.toString();
							String clientSessionId = redisUtil.get(deviceIdKey);
							try {
								boolean result = SocketHolder.send(clientSessionId, "sendImg", data);
								if (result) {
									r = true;
								} else {
									redisUtil.srem(rbindKey, client.toString());
								}
							} catch (Exception e) {
								e.printStackTrace();
								redisUtil.srem(rbindKey, client.toString());
							}
							log.info("发送来自机器{}的远程图片给客户端{}", machineId, client);
						}
					}
					if (!r) {
						SocketHolder.close(key);
					}
				} else {
					SocketHolder.close(key);
				}
			}

			@Override
			public void monitorEvent(String key, String data, Map<String, List<String>> urlParams) {
				String machineId = Optional.ofNullable(urlParams.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				String connectType = Optional.ofNullable(urlParams.get(CommonConstants.CONNECT_TYPE)).map(a -> a.get(0))
						.orElse("");
				String deviceId = Optional.ofNullable(urlParams.get(CommonConstants.DEVICE_ID)).map(a -> a.get(0))
						.orElse("");
				if (CommonConstants.CONNECT_TYPE_CLIENT.equals(connectType)) {
					log.info("monitorEvent转发客户端deviceId:{}发送的指令:{}--->机器id:{}", deviceId, data, machineId);
					String machinKey = CommonConstants.REDIS_REMOTE_MACHINE_SESSION_PATH + machineId;
					String sessionId = redisUtil.get(machinKey);
					String result = GZIPUtil.compress(AesUtils.encrypt(data));
					SocketHolder.send(sessionId, "motionEvent", result);
				}
			}

			@Override
			public void keyEvent(String key, String data, Map<String, List<String>> urlParams) {
				String machineId = Optional.ofNullable(urlParams.get(CommonConstants.MACHINE_ID)).map(a -> a.get(0))
						.orElse("");
				String connectType = Optional.ofNullable(urlParams.get(CommonConstants.CONNECT_TYPE)).map(a -> a.get(0))
						.orElse("");
				String deviceId = Optional.ofNullable(urlParams.get(CommonConstants.DEVICE_ID)).map(a -> a.get(0))
						.orElse("");
				if (CommonConstants.CONNECT_TYPE_CLIENT.equals(connectType)) {
					log.info("keyEvent转发客户端deviceId:{}发送的指令:{}--->机器id:{}", deviceId, data, machineId);
					String machinKey = CommonConstants.REDIS_REMOTE_MACHINE_SESSION_PATH + machineId;
					String sessionId = redisUtil.get(machinKey);
					String result = GZIPUtil.compress(AesUtils.encrypt(data));
					SocketHolder.send(sessionId, "keyEvent", result);
				}
			}

		};
	}

	@Bean
	public SocketServer socketServer() {
		return new SocketServer("0.0.0.0", 1245, socketServerHandler());
	}

}
