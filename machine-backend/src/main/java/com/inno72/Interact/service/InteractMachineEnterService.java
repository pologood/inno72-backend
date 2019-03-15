package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72MachineEnter;
import com.inno72.Interact.vo.MachineEnterVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2019/03/15.
 */
public interface InteractMachineEnterService extends Service<Inno72MachineEnter> {

	List<MachineEnterVo> findByPage(String interactId, Integer status, String machineCode);

	Result<Object> updateBatchEnter(String interactId, String enterType);

	Result<Object> updateEnter(String machineId, String enterType);

}
