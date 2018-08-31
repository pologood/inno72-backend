package com.inno72.machine.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Task;
import com.inno72.machine.vo.Inno72TaskVo;
import com.inno72.project.vo.Inno72AdminAreaVo;

/**
 * Created by CodeGenerator on 2018/08/24.
 */
public interface TaskService extends Service<Inno72Task> {

	Result<String> saveTsak(Inno72TaskVo task);

	Result<String> updateTsak(Inno72TaskVo task);

	Result<String> delById(String id);

	List<Inno72AdminAreaVo> selectAreaMachineList(String code, String level, String machineCode);

	Inno72TaskVo findDetail(String id);

	List<Inno72TaskVo> findByPage(String type, String status);

	List<Map<String, Object>> selectAppList();

	Result<String> updateStatus(String id, Integer status, Integer doType);

}
