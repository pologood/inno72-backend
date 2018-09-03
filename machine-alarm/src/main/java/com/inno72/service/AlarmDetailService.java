package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.AlarmDetailBean;
import com.inno72.model.Inno72Machine;

import java.util.List;

public interface AlarmDetailService{

    public Result<String> add(AlarmDetailBean bean);

    public void addToMachineBean(List<Inno72Machine> list);

    public void addToExceptionMachineBean();

}
