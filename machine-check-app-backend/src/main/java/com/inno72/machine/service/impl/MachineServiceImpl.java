package com.inno72.machine.service.impl;

import com.inno72.check.mapper.Inno72CheckUserMachineMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.check.vo.FaultVo;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72AdminAreaMapper;
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Resource
    private Inno72CheckUserMachineMapper inno72CheckUserMachineMapper;

    @Resource
    private Inno72AdminAreaMapper inno72AdminAreaMapper;

    @Resource
    private Inno72LocaleMapper inno72LocaleMapper;
    @Override
    public Result<String> setMachine(String machineId, String localeId) {
        Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
        if(machine == null){
            return Results.failure("机器不存在");
        }
        if(machine.getMachineStatus().equals(3)){
            Inno72CheckUser checkUser = UserUtil.getUser();
            machine.setLocaleId(localeId);
            machine.setMachineStatus(4);
            machine.setUpdateId(checkUser.getId());
            machine.setUpdateTime(LocalDateTime.now());
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
        }else if(machine.getMachineStatus().equals(4)){
            return ResultGenerator.genSuccessResult();
        }else{
            return Results.failure("机器状态有误");
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<List<Inno72Machine>> getMachineList() {
        String chekUserId = UserUtil.getUser().getId();
        List<Inno72Machine> list = inno72MachineMapper.machineList(chekUserId);
        if(list != null && list.size()>0){
            list.forEach(inno72Machine -> {
                List<FaultVo> faultVoList = inno72Machine.getFaultVoList();
                if(faultVoList != null && faultVoList.size()>0){
                    inno72Machine.setFaultStatus(-1);
                }else{
                    inno72Machine.setFaultStatus(1);
                }
                inno72Machine.setFaultVoList(null);
                List<Inno72CheckSignIn> signInList = inno72Machine.getSignInList();
                if(signInList != null && signInList.size()>0){
                    inno72Machine.setSignInStatus(1);
                }else{
                    inno72Machine.setSignInStatus(-1);
                }
                inno72Machine.setSignInList(null);
            });
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 查询一级地址
     */
    @Override
    public Result<List<Inno72AdminArea>> findFirstLevelArea() {
        List<Inno72AdminArea> list = inno72AdminAreaMapper.selectFistLevelArea();
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 查询单个一级地址及以下地址
     */
    @Override
    public Result<Inno72AdminArea> findByFirstLevelCode(String code) {
        Inno72AdminArea adminArea = inno72AdminAreaMapper.selectByFirstLevelCode(code);
        return ResultGenerator.genSuccessResult(adminArea);
    }

    @Override
    public Result<List<Inno72Locale>> selectLocaleByAreaCode(String areaCode) {
        List<Inno72Locale> list = inno72LocaleMapper.selectLocaleByAreaCode(areaCode);
        return ResultGenerator.genSuccessResult(list);
    }

}
