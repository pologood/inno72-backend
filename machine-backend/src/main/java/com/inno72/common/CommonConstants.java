package com.inno72.common;

public class CommonConstants {

	/** 钉钉消息加密用的token **/
	public final static String DD_TOKEN = "123456";
	/** 钉钉消息加密用的key **/
	public final static String AES_KEY = "1111111111111111111111111111111111111111111";
	/** 钉钉企业id **/
	public final static String DD_CORPID = "dingd04d2d6ca18d0fd535c2f4657eb6378f";

	/** 用户登录信息_缓存KEY前缀 **/
	public static final String USER_LOGIN_CACHE_KEY_PREF = "machine-backend:login_user:";

	/** 用户登录TOKEN_缓存KEY前缀 **/
	public static final String USER_LOGIN_TOKEN_CACHE_KEY_PREF = "machine-backend:login_user_token:";

	/** 被踢出用户集合 **/
	public static final String CHECK_OUT_USER_TOKEN_SET_KEY = "machine-backend:checkout_user_token_set";

	/** 用户sessionData有效期 **/
	public static final int SESSION_DATA_EXP = 3600 * 24;

	/** 用户登录对象 **/
	public static SessionData SESSION_DATA = new SessionData();

	/** OSS基础路径 **/
	public static final String OSS_PATH = "backend";
	/** OSS基础路径 **/
	public static final String ALI_OSS = "https://inno72.oss-cn-beijing.aliyuncs.com/";

	/** 每次下载条数 **/
	public final static int ONE_THOUSAND = 10000;

	/** 计划ID key **/
	public static final String REDIS_ACTIVITY_PLAN_CACHE_KEY = "machine_plan:";

	/** 企业微信获取Token **/
	public static final String qyWeChatGetsAccessTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}";

	/** 企业微信发消息 **/
	public final static String qyWeChatMsgSendUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";

	/** 企业微信添加用户 **/
	public final static String qyWeChatAddUserUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token={0}";

	/** 企业微信用户USERID转换OpenID **/
	public final static String qyWeChatConvertOpenidUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token={0}";

	/** 企业微信用户邀请成员 **/
	public final static String qyWeChatInviteUserUrl = "https://qyapi.weixin.qq.com/cgi-bin/batch/invite?access_token={0}";
	/** 默认活动计划ID key **/
	public static final String REDIS_ACTIVITY_DEFAULT_PLAN_CACHE_KEY = "machine_default_plan:";
}
