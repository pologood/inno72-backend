package com.inno72.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.inno72.app.service.PushService;
import com.inno72.app.vo.PushMsgInfo;
import com.inno72.app.vo.PushMsgVo;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.msg.MsgUtil;

@Service
public class PushServiceImpl implements PushService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MsgUtil msgUtil;

	@Override
	public Result<String> pushMsg(Map<String, Object> msg) {
		String $msg = JSON.toJSONString(msg);
		PushMsgVo pushMsg = JSON.parseObject($msg, PushMsgVo.class);
		if (pushMsg == null || pushMsg.getPushType() == null || StringUtil.isEmpty(pushMsg.getAlias())
				|| StringUtil.isEmpty(pushMsg.getMachineCode())) {
			return Results.failure("推送消息格式错误");
		}
		String $info = null;
		if (pushMsg.getPushType() == PushMsgVo.Push_Type.INSTALL_APP.v()) {
			PushMsgInfo push = new PushMsgInfo();
			push.setPushType(pushMsg.getPushType());
			push.setMsgInfo(pushMsg.getMsgInfo());
			$info = JSON.toJSONString(push);
			logger.info("发送请求安装APPpush内容：{}", $info);
		} else if (pushMsg.getPushType() == PushMsgVo.Push_Type.SEND_LOG.v()) {
			PushMsgInfo push = new PushMsgInfo();
			push.setPushType(pushMsg.getPushType());
			push.setMsgInfo(pushMsg.getMsgInfo());
			$info = JSON.toJSONString(push);
			logger.info("发送请求发送日志push内容：{}", $info);
		} else if (pushMsg.getPushType() == PushMsgVo.Push_Type.SEND_IMG.v()) {
			PushMsgInfo push = new PushMsgInfo();
			push.setPushType(pushMsg.getPushType());
			$info = JSON.toJSONString(push);
			logger.info("发送请求发送图片push内容：{}", $info);
		} else if (pushMsg.getPushType() == PushMsgVo.Push_Type.SEND_ADB.v()) {
			PushMsgInfo push = new PushMsgInfo();
			push.setPushType(pushMsg.getPushType());
			$info = JSON.toJSONString(push);
			logger.info("发送请求发送ADBpush内容：{}", $info);
		} else {
			return Results.failure("pushType错误");
		}
		Map<String, String> params = new HashMap<>();
		params.put("msg", $info);
		msgUtil.sendPush("push_android_transmission_common", params, pushMsg.getAlias(), "machine-app-backend--pushMsg",
				pushMsg.getTitle(), pushMsg.getText());
		return Results.success();
	}

}
