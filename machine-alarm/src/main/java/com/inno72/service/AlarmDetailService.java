package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.Inno72Machine;

public interface AlarmDetailService{

	public void addToMachineBean(List<Inno72Machine> list);

	public void addToExceptionMachineBean();

	public void sendExceptionMachineAlarm();

	public void updateMachineStart();
}
