package com.inno72.alarmMsg.mapper;


import java.util.List;
import java.util.Map;

import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AlarmMsgMapper extends Mapper<Inno72AlarmMsg> {
	List<Inno72AlarmMsg> selectAlarmUser();

	int selectUnReadCount(Map<String,Object> map);
}