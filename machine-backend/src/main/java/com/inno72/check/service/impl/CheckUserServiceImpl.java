package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckUserMachineMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.check.model.Inno72CheckUserPhone;
import com.inno72.check.service.CheckUserService;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.project.vo.Inno72AdminAreaVo;
import com.inno72.project.vo.Inno72MachineVo;
import com.inno72.system.model.Inno72User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
@Service
@Transactional
public class CheckUserServiceImpl extends AbstractService<Inno72CheckUser> implements CheckUserService {
	private static Logger logger = LoggerFactory.getLogger(CheckUserServiceImpl.class);
	
    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;
    @Resource
    private Inno72CheckUserMachineMapper inno72CheckUserMachineMapper;
    
    Pattern phone=Pattern.compile("^(1[0-9])\\d{9}$");
    Pattern cardNo=Pattern.compile("^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$");
    

	@Override
	public Result<String>  saveModel(Inno72CheckUserVo model) {
		try {
			logger.info("----------------巡检人员添加--------------");
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isBlank(model.getName())) {
	   	        return Results.failure("请填写姓名");
	   		 }
			if (StringUtil.isBlank(model.getEnterprise())) {
	   	        return Results.failure("请填写公司");
	   		 }
			Matcher match1=phone.matcher(model.getPhone()); 
	   	    if (!match1.matches()) {
	   	       return Results.failure("手机号格式有误");
	   		}
	   	    Matcher match2=cardNo.matcher(model.getCardNo()); 
	   	    if (!match2.matches()) {
	   	       return Results.failure("身份证号格式有误");
	   		}
	   	    int n =inno72CheckUserMapper.getCount(model.getCardNo());
	   	    if (n>0){
	   	    	return Results.failure("身份证号已存在");
	   	    }
			
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			String checkUserId = StringUtil.getUUID();
			model.setId(checkUserId);
			model.setCreateId(mUserId);
			model.setUpdateId(mUserId);
			model.setCreateTime(LocalDateTime.now());
			model.setUpdateTime(LocalDateTime.now());
			//获取寻选择机器
			List<Inno72CheckUserMachine> insertUserMachineList= new ArrayList<>();
			List<Inno72MachineVo> machines=model.getMachines();
			if (null !=machines && machines.size()>0) {
				for (Inno72MachineVo inno72MachineVo : machines) {
					Inno72CheckUserMachine userMachine = new Inno72CheckUserMachine();
					userMachine.setId(StringUtil.getUUID());
					userMachine.setCheckUserId(checkUserId);
					userMachine.setMachineId(inno72MachineVo.getMachineId());
					insertUserMachineList.add(userMachine);
				}
				inno72CheckUserMachineMapper.insertUserMachineList(insertUserMachineList);
			}
			super.save(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		
		return Results.success();
	}

	@Override
	public Result<String>  delById(String id) {
		try {
			logger.info("----------------巡检人员删除--------------");
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			//删除条件
		
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			Inno72CheckUser model=inno72CheckUserMapper.selectByPrimaryKey(id);
			model.setIsDelete(1);
			model.setUpdateId(mUserId);
			model.setUpdateTime(LocalDateTime.now());
			//删除关联关系
			inno72CheckUserMachineMapper.deleteByUserId(id);
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		return Results.success();
	}

	@Override
	public Result<String>  updateModel(Inno72CheckUserVo model) {
		try {
			logger.info("----------------巡检人员更新--------------");
			SessionData session = CommonConstants.SESSION_DATA;
			Inno72User mUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
			if (mUser == null) {
				logger.info("登陆用户为空");
				return Results.failure("未找到用户登录信息");
			}
			if (StringUtil.isNotBlank(model.getCardNo())) {
				Inno72CheckUser old = inno72CheckUserMapper.selectByPrimaryKey(model.getId());
				int n =inno72CheckUserMapper.getCount(model.getCardNo());
		   	    if (n>0 && !model.getCardNo().equals(old.getCardNo())){
		   	    	return Results.failure("更新身份证号已存在");
		   	    }
			}
			String mUserId = Optional.ofNullable(mUser).map(Inno72User::getId).orElse(null);
			model.setUpdateId(mUserId);
			model.setUpdateTime(LocalDateTime.now());
			
			List<Inno72MachineVo> machines=model.getMachines();
			if (null !=machines && machines.size()>0) {
				List<Inno72CheckUserMachine> insertUserMachineList= new ArrayList<>();
				
				for (Inno72MachineVo inno72MachineVo : machines) {
					Inno72CheckUserMachine userMachine = new Inno72CheckUserMachine();
					userMachine.setId(StringUtil.getUUID());
					userMachine.setCheckUserId(model.getId());
					userMachine.setMachineId(inno72MachineVo.getMachineId());
					insertUserMachineList.add(userMachine);
				}
				//先删除关联关系
				inno72CheckUserMachineMapper.deleteByUserId(model.getId());
				
				inno72CheckUserMachineMapper.insertUserMachineList(insertUserMachineList);
			}
			
			super.update(model);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return Results.failure("操作失败");
		}
		
		return Results.success();
	}

	@Override
	public Inno72CheckUserVo findDetail(String id) {
		
		return inno72CheckUserMapper.selectUserDetail(id);
	}

	@Override
	public List<Inno72CheckUser> findByPage(String keyword) {
		return inno72CheckUserMapper.selectByPage(keyword);
	}
	
	@Override
	public List<Map<String, Object>> getUserMachinDetailList(String userId) {
		return inno72CheckUserMachineMapper.selectUserMachinDetailList(userId);
	}

	@Override
	public List<Inno72CheckUserPhone> selectPhoneByMachineCode(Inno72CheckUserPhone inno72CheckUserPhone) {

		String machineCode = inno72CheckUserPhone.getMachineCode();
		List<Inno72CheckUserPhone> inno72CheckUserPhones = inno72CheckUserMapper.selectPhoneByMachineCode(machineCode);
		return inno72CheckUserPhones;
	}

	@Override
	public List<Inno72AdminAreaVo> selectAreaMachineList(String code,String level) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		
		/*if (StringUtil.isEmpty(code)) {
			params.put("level", 1);
	   	}
		if (level.equals("1")) {
			params.put("num", 2);
		}else if (level.equals("2")) {
			params.put("num", 4);
		}else if (level.equals("3")) {
			params.put("num", 6);
		}else if (level.equals("4")) {
			params.put("num", 9);
		}*/
		int num = StringUtil.getAreaCodeNum(code);
		if (num < 4) {
			num = 3;
		}
		String likeCode = code.substring(0, num);
		params.put("code", likeCode);
		params.put("num", num);
		List<Inno72AdminAreaVo> list = inno72CheckUserMapper.selectMachineList(params);
		/*if (level.equals("5")) {
			list = inno72CheckUserMapper.selectMachineList(params);
		}else{
			list = inno72CheckUserMapper.selectAreaMachineList(params);
			for (Inno72AdminAreaVo inno72AdminAreaVo : list) {
				List<Inno72MachineVo> machines=inno72AdminAreaVo.getMachines();
				inno72AdminAreaVo.setTotalNum(machines.size()+"");
			}
		}*/
		
	   	return list;
	}
    
    
    

}
