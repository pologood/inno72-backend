package com.inno72.alarmMsg.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.inno72.alarmMsg.mapper.Inno72AlarmMsgDetailMapper;
import com.inno72.alarmMsg.mapper.Inno72AlarmMsgMapper;
import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.model.Inno72AlarmMsgDetail;
import com.inno72.alarmMsg.service.AlarmMsgService;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

@Service
@Transactional
public class AlarmMsgServiceImpl extends AbstractService<Inno72AlarmMsg> implements AlarmMsgService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private Inno72AlarmMsgDetailMapper inno72AlarmMsgDetailMapper;

	@Resource
	private Inno72AlarmMsgMapper inno72AlarmMsgMapper;

	@Resource
	private Inno72CheckUserMapper inno72CheckUserMapper;
	@Override
	public Result<String> readDetail(Inno72AlarmMsg alarmMsg) {
		String alarmMsgId = alarmMsg.getId();
		if(StringUtil.isEmpty(alarmMsgId)){
			return Results.failure("参数缺失");
		}
		Inno72AlarmMsg msg = inno72AlarmMsgMapper.selectByPrimaryKey(alarmMsgId);
		if(msg == null){
			return Results.failure("参数有误");
		}
		int isRead = msg.getIsRead();
		if(isRead != 1){
			msg.setIsRead(1);
			inno72AlarmMsgMapper.updateByPrimaryKeySelective(msg);
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72AlarmMsg> findDataByPage(Inno72AlarmMsg alarmMsg) {
		Inno72CheckUser user = UserUtil.getUser();
		String userId = user.getId();
		Map<String,Object> map = new HashMap<>();
		String machineCode = alarmMsg.getMachineCode();
		map.put("checkUserId",userId);
		map.put("machineCode",machineCode);
		int[] mainTypes = alarmMsg.getMainTypes();
		if(mainTypes != null && mainTypes.length>0){
			map.put("mainTypes",mainTypes);
		}
		List<Inno72AlarmMsg> list = inno72AlarmMsgMapper.selectByPage(map);
		return list;
	}

	@Override
	public Result<String> initData() {
		List<Inno72AlarmMsg> list = inno72AlarmMsgMapper.selectAlarmUser();
		int dataNum = 0;
		if(list != null && list.size()>0){
			for(Inno72AlarmMsg alarmMsg:list){
				String alarmMsgId = alarmMsg.getId();
				List<Inno72CheckUserMachine> inno72CheckUserMachineList = alarmMsg.getInno72CheckUserMachineList();
				if(inno72CheckUserMachineList != null && inno72CheckUserMachineList.size()>0){
					for(Inno72CheckUserMachine checkUserMachine:inno72CheckUserMachineList){
						String checkUserId = checkUserMachine.getCheckUserId();
						dataNum = dataNum+this.saveDetail(checkUserId,alarmMsgId);
					}
				}else{
					logger.info("没有用户");
				}
			}
		}
		return ResultGenerator.genSuccessResult("初始化消息详情数量为："+dataNum);
	}

	@Override
	public Result<Integer> unReadCount(Inno72AlarmMsg alarmMsg) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String checkUserId = checkUser.getId();
		Map<String,Object> map = new HashMap<>();
		String machineCode = alarmMsg.getMachineCode();
		map.put("checkUserId",checkUserId);
		map.put("machineCode",machineCode);
		int[] mainTypes = alarmMsg.getMainTypes();
		if(mainTypes != null && mainTypes.length>0){
			map.put("mainTypes",mainTypes);
		}
		int unReadCount = inno72AlarmMsgMapper.selectUnReadCount(map);
		return ResultGenerator.genSuccessResult(unReadCount);
	}

	public int saveDetail(String checkUserId,String alarmMsgId){
		Map<String,Object> map = new HashMap<>();
		map.put("checkUserId",checkUserId);
		map.put("alarmMsgId",alarmMsgId);
		Inno72AlarmMsgDetail inno72AlarmMsgDetail = inno72AlarmMsgDetailMapper.selectByParam(map);
		if(inno72AlarmMsgDetail == null){
			LocalDateTime now = LocalDateTime.now();
			Inno72AlarmMsgDetail detail = new Inno72AlarmMsgDetail();
			detail.setId(StringUtil.getUUID());
			detail.setAlarmMsgId(alarmMsgId);
			detail.setCheckUserId(checkUserId);
			detail.setIsRead(1);
			detail.setCreateTime(now);
			detail.setReadTime(now);
			inno72AlarmMsgDetailMapper.insertSelective(detail);
			return 1;
		}
		return 0;
	}
}
