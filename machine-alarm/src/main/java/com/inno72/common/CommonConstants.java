package com.inno72.common;

public class CommonConstants {

	// 掉货异常
	public final static String MACHINE_DROPGOODS_EXCEPTION = "machineDropGoodsException";

	// 机器网络异常
	public final static String MACHINE_NET_EXCEPTION = "machineCloseNetException";

	// 机器缺货提示
	public final static String MACHINE_LACKGOODS_EXCEPTION = "machineLackGoodsException";

	// 机器货道
	public final static String SYS_MACHINE_CHANNEL = "machineChannel";

	// 机器掉货
	public final static String SYS_MACHINE_DROPGOODS = "machineDropGoods";

	// 机器缺货
	public final static String SYS_MACHINE_LACKGOODS = "machineLackGoods";

	// 网络
	public final static String SYS_MACHINE_NET = "machineCloseNet";

	// 商品缺货类型
	public final static int LACKGOODS_TWENTYPERCENT = 2;

	public final static int LACKGOODS_TENPERCENT = 1;

	// 机器状态是正常的
	public final static int MACHINESTATUS_NUMAUL = 4;

	// 报警开关是开着
	public final static int OPENSTATUS_OPEN = 0;

	// 缺货百分之十
	public static final int TEN_PERSENT = 10;
	// 缺货百分之二十
	public static final int TWENTY_PERSENT = 20;

	// 网络关闭
	public final static Integer NET_CLOSE = 0;
	// 网络开启
	public final static Integer NET_OPEN = 1;

	public final static Integer RESULT_SUCCESS = 0;

	public final static Integer MACHINE_DOOR_OPEN = 1;

	/** 心跳Redis前缀 */
	public final static String MACHINE_ALARM_HEART_BEF = "machine-alarm:heart:";

	/** 心跳时间Redis前缀 */
	public final static String MACHINE_ALARM_HEART_TIME_BEF = "machine-alarm:heart-time:";

	/** 网络连接Redis前缀 */
	public final static String MACHINE_ALARM_CONNECT_BEF = "machine-alarm:connect:";

	/** 网络连接时间Redis前缀 */
	public final static String MACHINE_ALARM_CONNECT_TIME_BEF = "machine-alarm:connect-time:";

	/***
	 * 手机号token前缀
	 */
	public static final String CHECK_LOGIN_TYPE_KEY_PREF = "machine-check-app-backend:phone:";

	/** 日志：缺货 */
	public final static String LOG_TYPE_LACKGOODS = "61";

	/** 日志：掉货 */
	public final static String LOG_TYPE_DROPGOODS = "62";

	/** 日志：心跳 */
	public final static String LOG_TYPE_HEART = "63";

	/** 日志：网络 */
	public final static String LOG_TYPE_CONNECT = "64";

}
