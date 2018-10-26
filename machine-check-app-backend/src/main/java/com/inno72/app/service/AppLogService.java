package com.inno72.app.service;

import java.util.Map;

import com.inno72.app.model.Inno72AppLog;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/07/22.
 */
public interface AppLogService extends Service<Inno72AppLog> {

	Result<String> sendLogInfo(Inno72AppLog log);

}
