package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72AlarmParam;

public interface AlarmParamService extends Service<Inno72AlarmParam> {

	public Inno72AlarmParam findByAlarmType(Integer alarmType);
}
