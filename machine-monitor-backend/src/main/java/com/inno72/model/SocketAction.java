package com.inno72.model;

public enum SocketAction {
	// 连接
	CONNECT(1),
	// 断开
	DISCONNECT(2),
	// 心跳
	HERT(3);

	private int v;

	private SocketAction(int v) {
		this.v = v;
	}

	public int v() {
		return this.v;
	}

	public static SocketAction get(int v) {
		for (SocketAction c : SocketAction.values()) {
			if (c.v == v) {
				return c;
			}
		}
		return null;
	}
}