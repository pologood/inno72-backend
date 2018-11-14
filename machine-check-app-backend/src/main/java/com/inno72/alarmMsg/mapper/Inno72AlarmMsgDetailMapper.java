package com.inno72.alarmMsg.mapper;


import java.util.List;
import java.util.Map;

import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.model.Inno72AlarmMsgDetail;
import com.inno72.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AlarmMsgDetailMapper extends Mapper<Inno72AlarmMsgDetail> {


	Inno72AlarmMsgDetail selectByParam(Map<String,Object> map);
}