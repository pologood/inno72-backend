package com.inno72.app.service;

import java.util.Map;

import com.inno72.common.Result;

public interface PushService {

	Result<String> pushMsg(Map<String, Object> msg);

}
