package com.inno72.app.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class PushMsgInfo {
	private Integer pushType;
	private Map<String, Object> msgInfo;

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public Map<String, Object> getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(Map<String, Object> msgInfo) {
		this.msgInfo = msgInfo;
	}

	// public static void main(String[] args) {
	// PushMsgInfo info = new PushMsgInfo();
	// info.setPushType(1);
	// Map<String, Object> msgInfo = new HashMap<>();
	// msgInfo.put("packageName", "com.test");
	// msgInfo.put("versionCode", 10);
	// msgInfo.put("downloadUrl", "http://www.baidu.com");
	// msgInfo.put("startStatus", 1);
	// info.setMsgInfo(msgInfo);
	// System.out.println(JSON.toJSONString(info));
	// }
	//

	public static void main(String[] args) {
		PushMsgInfo info = new PushMsgInfo();
		info.setPushType(2);
		Map<String, Object> msgInfo = new HashMap<>();
		List<Map<String, Object>> packs = new ArrayList<>();
		Map<String, Object> a1 = new HashMap<>();
		a1.put("packageName", "com.test");
		a1.put("startTime", "2018-06-22 12:48");
		a1.put("endTime", "2018-06-22 23:59");
		a1.put("logType", 1);
		Map<String, Object> a2 = new HashMap<>();
		a2.put("packageName", "com.test1");
		a2.put("startTime", "2018-06-22 12:48");
		a2.put("endTime", "2018-06-22 23:59");
		a2.put("logType", 2);
		packs.add(a1);
		packs.add(a2);
		msgInfo.put("packInfo", packs);
		info.setMsgInfo(msgInfo);
		System.out.println(JSON.toJSONString(info));
	}

}
