package com.inno72.alarmMsg.service;

import java.util.List;

import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.common.Result;
import com.inno72.common.Service;

import tk.mybatis.mapper.entity.Condition;

public interface AlarmMsgService extends Service<Inno72AlarmMsg> {
	Result<String> readDetail(Inno72AlarmMsg alarmMsg);

	List<Inno72AlarmMsg> findDataByPage(Inno72AlarmMsg alarmMsg);

	Result<String> initData();

	Result<Integer> unReadCount(Inno72AlarmMsg alarmMsg);

	Result<String> readAll(Inno72AlarmMsg alarmMsg);
}
