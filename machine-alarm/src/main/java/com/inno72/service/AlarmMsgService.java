package com.inno72.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.model.Inno72AlarmMsg;
import com.inno72.model.Inno72CheckUserPhone;


/**
 * Created by CodeGenerator on 2018/07/19.
 */
public interface AlarmMsgService extends Service<Inno72AlarmMsg> {

	public void saveAlarmMsg(String system,String machineCode, String detail,List<Inno72CheckUserPhone> inno72CheckUserPhones);
}
