package com.inno72.socketio;

import java.util.List;
import java.util.Map;

public abstract class SocketServerHandler {

	/**
	 * 数据处理
	 * 
	 * @param data
	 * @return
	 * @author Houkm 2017年9月1日
	 */
	public abstract String process(String key, String data, Map<String, List<String>> params);

	/**
	 * 处理客户端返回的心跳响应消息
	 * 
	 * @param key
	 * @param data
	 * @param params
	 * @author Houkm 2017年9月4日
	 */
	public abstract void heartBeatResponse(String key, String data, Map<String, List<String>> params);

	/**
	 * 连接事件
	 * 
	 * @param key
	 * @author Houkm 2017年9月4日
	 */
	public abstract void connectNotify(String key, Map<String, List<String>> data);

	/**
	 * 是否异常断开
	 * 
	 * @param key
	 * @return
	 * @author Houkm 2017年9月1日
	 */
	public abstract boolean isExceptionClose(String key, Map<String, List<String>> data);

	/**
	 * 异常断开时等待x秒后通知
	 * 
	 * @return
	 * @author Houkm 2017年9月1日
	 */
	public abstract int exceptionCloseWaitTimeSeconds();

	/**
	 * 异常断开时通知
	 * 
	 * @param key
	 * @author Houkm 2017年9月1日
	 */
	public abstract void closeNotify(String key, boolean exception, Map<String, List<String>> data);

	/**
	 * 应用中是否有此连接的绑定关系
	 * 
	 * @param key
	 * @author Houkm 2017年9月4日
	 */
	public abstract String getCurrentSessionId(Map<String, List<String>> data);

}
