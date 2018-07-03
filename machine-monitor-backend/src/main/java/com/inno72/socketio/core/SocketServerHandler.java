package com.inno72.socketio.core;

import java.util.List;
import java.util.Map;

/**
 * 消息处理抽象类
 * 
 * @author lzh
 *
 */
public abstract class SocketServerHandler {

	/**
	 * message类型消息处理
	 * 
	 * @param key
	 * @param data
	 * @param param
	 * @return
	 */
	public abstract String process(String key, String data, String param);

	/**
	 * 监控消息处理
	 * 
	 * @param key
	 * @param data
	 * @param params
	 */
	public abstract void monitorResponse(String key, String data, Map<String, List<String>> params);

	/**
	 * 连接通知
	 * 
	 * @param key
	 * @param data
	 */
	public abstract void connectNotify(String key, Map<String, List<String>> data);

	/**
	 * 断开通知
	 * 
	 * @param key
	 * @param data
	 */
	public abstract void closeNotify(String key, Map<String, List<String>> data);

}
