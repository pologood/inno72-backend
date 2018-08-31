package com.inno72.app.service;

import java.util.Map;

import com.inno72.app.model.Inno72TaskMachine;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
public interface TaskService extends Service<Inno72TaskMachine> {

	Result<String> updateTaskStatus(Map<String, Object> msg);

}
