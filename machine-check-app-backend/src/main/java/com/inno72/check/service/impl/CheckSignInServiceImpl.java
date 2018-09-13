package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.vo.PointLog;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {

    @Resource
    private Inno72CheckSignInMapper checkSignInMapper;

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private MongoOperations mongoTpl;
    @Override
    public Result<String> add(Inno72CheckSignIn signIn) {
    	String machineId = signIn.getMachineId();
    	if(StringUtil.isEmpty(machineId)){
    		return Results.failure("参数缺失");
		}
        signIn.setCreateTime(LocalDateTime.now());
        signIn.setId(StringUtil.getUUID());
        signIn.setCheckUserId(UserUtil.getUser().getId());
        int count = checkSignInMapper.insertSelective(signIn);
        if(count != 1){
            Results.failure("打卡失败");
        }
		Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		PointLog pointLog = new PointLog();
		pointLog.setType(CommonConstants.LOG_TYPE_MACHINE_SIGN);
		pointLog.setMachineCode(machine.getMachineCode());
		pointLog.setPointTime(DateUtil.toTimeStr(LocalDateTime.now(),DateUtil.DF_FULL_S1));
		pointLog.setDetail("机器打卡");
		mongoTpl.save(pointLog);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<List<Inno72CheckSignIn>> findThisMonth(String machineId) {
        List<Inno72CheckSignIn> list = checkSignInMapper.selectTishMonth(UserUtil.getUser().getId(),machineId);
        return ResultGenerator.genSuccessResult(list);
    }

    @Override
    public Result<List<MachineSignInVo>> findMachineSignList() {
        List<MachineSignInVo> list = checkSignInMapper.selectMachineSignList(UserUtil.getUser().getId());
        if(list != null && list.size()>0){
            for(MachineSignInVo vo:list){
                List<Inno72CheckSignIn> signInList = vo.getSignInList();
                if(signInList != null && signInList.size()>0){
                    vo.setSignInStatus(1);
                }else{
                    vo.setSignInStatus(-1);
                }
                vo.setSignInList(null);
            }
        }

        return ResultGenerator.genSuccessResult(list);
    }
}
