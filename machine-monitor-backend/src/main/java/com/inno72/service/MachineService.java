package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;
import com.inno72.model.MachineLogInfo;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 *
 * @author
 */
public interface MachineService extends Service<Inno72Machine> {

    Result<List<MachineLogInfo>> findMachineNetStatusOpenList(List<MachineLogInfo> machineLogInfos);

    Result<List<MachineLogInfo>> findMachineNetStatusCloseList(List<MachineLogInfo> machineLogInfos);


}
