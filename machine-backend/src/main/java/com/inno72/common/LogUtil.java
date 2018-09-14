package com.inno72.common;

import java.time.LocalDateTime;

import com.inno72.log.PointLogContext;
import com.inno72.log.vo.LogType;

public class LogUtil {
	/**
	 * @param msg
	 *            消息体 msg[0] type 日志类型 msg[1] machineCode 机器code msg[2] detail
	 *            详情
	 */
	public static void logger(String... msg) {
		new PointLogContext(LogType.POINT).machineCode(msg[1])
				.pointTime(DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S1)).type(msg[0]).detail(msg[2])
				.tag("").bulid();
	}
}
