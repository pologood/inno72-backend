package com.inno72.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.DateUtil;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72CheckSignInMapper;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72AlarmParam;
import com.inno72.model.Inno72CheckSignIn;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72Machine;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.AlarmParamService;
import com.inno72.service.MachineService;

/**
 * Created by CodeGenerator on 2018/06/29.
 * @author
 */
@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {


    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Resource
    private Inno72CheckSignInMapper inno72CheckSignInMapper;

    @Resource
    private AlarmParamService alarmParamService;

	@Resource
	private IRedisUtil redisUtil;

    @Override
    public List<Inno72Machine> findByPage(Object condition) {
        return null;
    }

    @Override
    public Inno72Machine findByCode(String machineCode) {
        return inno72MachineMapper.findMachineByMachineCode(machineCode);
    }

    @Override
    public List<Inno72Machine> findAlarmAllMachine() {
        return inno72MachineMapper.findAlarmAllMachine();
    }

	@Override
	public void getSignMachineList() {
		Inno72AlarmParam alarmParam = alarmParamService.findByAlarmType(10);
		if(alarmParam != null){
			LocalDate localDate = LocalDate.now();
			List<String> dateList = DateUtil.getMonthFullDay(localDate);
			for(String findTime:dateList){
				boolean flag = redisUtil.sismember("signKey",findTime);
				if(!flag){
					LocalDate findDate = DateUtil.toDate(findTime,DateUtil.DF_ONLY_YMD_S1);
					LocalDate nowDate = LocalDate.now();
					if(nowDate.isAfter(findDate) || nowDate.isEqual(findDate)){
						List<Inno72Machine> machineList = inno72MachineMapper.selectSignMachineList(findTime);
						if(machineList != null && machineList.size()>0){
							String signTime=findTime+" 10:00:00";
							for(Inno72Machine machine:machineList){
								List<Inno72CheckUser> checkUserList = machine.getCheckUserList();
								if(checkUserList != null && checkUserList.size()>0){
									Random random = new Random();
									int index = random.nextInt(checkUserList.size());
									Inno72CheckSignIn inno72CheckSignIn = new Inno72CheckSignIn();
									inno72CheckSignIn.setId(StringUtil.getUUID());
									inno72CheckSignIn.setCheckUserId(checkUserList.get(index).getId());
									inno72CheckSignIn.setMachineId(machine.getId());
									inno72CheckSignIn.setType("1,2,3,4,5");
									inno72CheckSignIn.setStatus(0);
									int second = random.nextInt(36000);
									Date date = DateUtil.addSecondOfDate(DateUtil.toDateOld(signTime,DateUtil.DF_ONLY_YMDHM),second);
									inno72CheckSignIn.setCreateTime(DateUtil.UDateToLocalDateTime(date));
									inno72CheckSignInMapper.insertSelective(inno72CheckSignIn);
								}
							}
						}
						redisUtil.sadd("signKey",findTime);
					}
				}
			}
		}
	}
}
