package com.inno72.service;

import com.inno72.model.SystemStatus;

public interface SocketService {

	void updateNetStatus(SystemStatus systemStatus);

	void recordHeart(String machineCode);
}
