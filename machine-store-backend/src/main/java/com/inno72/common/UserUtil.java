package com.inno72.common;

import java.util.Optional;

import com.inno72.store.model.Inno72Storekeeper;

public class UserUtil {

	public static Inno72Storekeeper getKepper() {
		SessionData session = CommonConstants.SESSION_DATA;
		Inno72Storekeeper storekeeper = Optional.ofNullable(session).map(SessionData::getStorekeeper).orElse(null);
		System.out.println(storekeeper.getName());
		return storekeeper;
	}
}
