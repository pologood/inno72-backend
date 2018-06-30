package com.inno72.socketio.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * 连接调用类
 * 
 * @author lzh
 *
 */
public class SocketHolder {

	private static Logger logger = LoggerFactory.getLogger(SocketHolder.class);

	public static int count() {
		return map.size();
	}

	/**
	 * 关闭连接
	 * 
	 * @param key
	 */
	public static void close(String key) {
		logger.info("主动关闭连接 --> {}", key);
		SocketIOClient channel = get(key);
		if (channel != null) {
			channel.disconnect();
		}
		remove(key);
	}

	static void bind(String key, SocketIOClient channel) {
		set(key, channel);
	}

	/**
	 * 主动发送message消息
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static boolean send(String key, String data) {
		logger.info("发送数据: {} --> {}", key, data);
		return send(get(key), "message", data);
	}

	/**
	 * 发送心跳消息
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public static boolean monitor(String key, String data) {
		logger.info("发送监控数据: {} --> {}", key, data);
		return send(get(key), "heart", data);
	}

	static boolean send(SocketIOClient channel, String event, String data) {
		if (channel != null && channel.isChannelOpen()) {
			channel.sendEvent(event, data);
			return true;
		} else {
			logger.info("连接不存在或以关闭, 不发送{}, {}", event, data);
			return false;
		}
	}

	private static ConcurrentMap<String, SocketIOClient> map = new ConcurrentHashMap<>();

	public static boolean exist(String key) {
		return map.containsKey(key);
	}

	static SocketIOClient get(String key) {
		return map.get(key);
	}

	static void set(String key, SocketIOClient channel) {
		map.put(key, channel);
	}

	static void remove(String key) {
		if (exist(key)) {
			map.remove(key);
		}
	}

}
