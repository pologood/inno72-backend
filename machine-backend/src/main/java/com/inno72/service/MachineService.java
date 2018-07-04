package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface MachineService extends Service<Inno72Machine> {

	/**
	 * 初始化机器
	 * 
	 * @param deviceId
	 * @return
	 */
	Result<String> initMeachine(String deviceId);

}
