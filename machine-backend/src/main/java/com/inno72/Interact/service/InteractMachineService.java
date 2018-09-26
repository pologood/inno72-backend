package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72InteractMachine;
import com.inno72.Interact.vo.InteractMachineTime;
import com.inno72.Interact.vo.MachineVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractMachineService extends Service<Inno72InteractMachine> {

	List<MachineVo> getList(String keyword, String startTime, String endTime);

	Result<String> save(InteractMachineTime model);

	List<MachineVo> getHavingMachines(String interactId, String keyword);

}
