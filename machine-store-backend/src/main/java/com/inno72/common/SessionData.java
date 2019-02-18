package com.inno72.common;

import com.inno72.store.model.Inno72Storekeeper;

public class SessionData {
	private String token;
	private Inno72Storekeeper storekeeper;

	public SessionData() {
		super();
	}

	public SessionData(String token, Inno72Storekeeper storekeeper) {
		super();
		this.token = token;
		this.storekeeper = storekeeper;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Inno72Storekeeper getStorekeeper() {
		return storekeeper;
	}

	public void setStorekeeper(Inno72Storekeeper storekeeper) {
		this.storekeeper = storekeeper;
	}
}
