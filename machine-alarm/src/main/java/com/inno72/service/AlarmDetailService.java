package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.AlarmDetailBean;

public interface AlarmDetailService{

    public Result<String> add(AlarmDetailBean bean);

}
