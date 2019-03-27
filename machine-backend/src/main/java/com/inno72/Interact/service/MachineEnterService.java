package com.inno72.Interact.service;

import com.inno72.common.Result;

/**
 * Created by CodeGenerator on 2018/11/27.
 */
public interface MachineEnterService {

	Result<Object> alipayAutomatUpload(String machineCode);

	Result<Object> jingdongAutomatUpload(String machineCode);

}
