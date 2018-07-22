package com.inno72.machine.service.impl;

import com.inno72.check.mapper.Inno72CheckUserMachineMapper;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Resource
    private Inno72CheckUserMachineMapper inno72CheckUserMachineMapper;
    @Override
    public Result<String> setMachine(String machineId, String localeId) {
        Inno72Machine machine = new Inno72Machine();
        Inno72CheckUser checkUser = UserUtil.getUser();
        machine.setId(machineId);
        machine.setLocaleId(localeId);
        machine.setMachineStatus(2);
        inno72MachineMapper.updateByPrimaryKeySelective(machine);
        Condition condition = new Condition(Inno72CheckUserMachine.class);
        condition.createCriteria().andEqualTo("checkUserId",checkUser.getId()).andEqualTo("machineId",machineId);
        List<Inno72CheckUserMachine> userMachines = inno72CheckUserMachineMapper.selectByCondition(condition);
        if(userMachines == null || userMachines.size()==0){
            Inno72CheckUserMachine userMachine = new Inno72CheckUserMachine();
            userMachine.setId(StringUtil.getUUID());
            userMachine.setCheckUserId(checkUser.getId());
            userMachine.setMachineId(machineId);
            inno72CheckUserMachineMapper.insertSelective(userMachine);
        }

        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<List<Inno72Machine>> getMachineList() {
        String chekUserId = UserUtil.getUser().getId();
        List<Inno72Machine> list = inno72MachineMapper.machineList(chekUserId);
        return ResultGenerator.genSuccessResult(list);
    }
}
