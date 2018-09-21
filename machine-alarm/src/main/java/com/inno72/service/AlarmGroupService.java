package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72AlarmGroup;

public interface AlarmGroupService {

	public Inno72AlarmGroup selectByParam(String areaCode);
}
