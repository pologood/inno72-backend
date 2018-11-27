package com.inno72.machine.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Service;
import com.inno72.machine.model.Inno72MachineLocaleDetail;

/**
 * Created by CodeGenerator on 2018/11/08.
 */
public interface MachineLocaleDetailService extends Service<Inno72MachineLocaleDetail> {

	List<Map<String, Object>> findListByPage(String code, String keyword);

	List<Map<String, Object>> findMachineLocaleDetail(String machineId);

}
