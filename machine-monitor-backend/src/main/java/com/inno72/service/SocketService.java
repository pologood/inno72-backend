package com.inno72.service;

import com.inno72.model.SocketAction;

public interface SocketService {
	public void manageSocket(SocketAction action, String machineCode, String sessionId);
}
