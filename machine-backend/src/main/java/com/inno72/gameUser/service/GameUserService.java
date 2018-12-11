package com.inno72.gameUser.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Service;
import com.inno72.gameUser.model.Inno72GameUser;

public interface GameUserService extends Service<Inno72GameUser> {
	List<Map<String, Object>> findForPage(String code, String sex, String time, String keyword);
}
