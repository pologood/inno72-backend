package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.StringUtil;
import com.inno72.mapper.Inno72CheckUserMapper;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.msg.MsgUtil;
import com.inno72.service.CheckUserService;


/**
 * Created by CodeGenerator on 2018/07/18.
 * @author
 */
@Service
@Transactional
public class CheckUserServiceImpl extends AbstractService<Inno72CheckUser> implements CheckUserService {

    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;

	@Autowired
	private MsgUtil msgUtil;

    @Override
    public List<Inno72CheckUserPhone> selectPhoneByMachineCode(Inno72CheckUserPhone inno72CheckUserPhone) {

        String machineCode = inno72CheckUserPhone.getMachineCode();
        List<Inno72CheckUserPhone> inno72CheckUserPhones = inno72CheckUserMapper.selectPhoneByMachineCode(machineCode);
        return inno72CheckUserPhones;
    }

	@Override
	public List<Inno72CheckUser> selectUnReadByParam(Map<String, Object> map) {
    	List<Inno72CheckUser> list = inno72CheckUserMapper.selectUnReadByParam(map);
		return list;
	}

	@Override
	public void sendSmsToCheck(List<Inno72CheckUser> checkUserList) {
		for(Inno72CheckUser checkUser:checkUserList){
			String phone = checkUser.getPhone();
			int unReadCount = checkUser.getUnReadCount();
			if(StringUtil.isNotEmpty(phone) && unReadCount>0){
				Map<String, String> params = new HashMap<>();
				msgUtil.sendSMS("未读消息code",params,phone,"machineAlarm-RedisReceiver");
			}
		}
	}

	@Override
    public List<Inno72CheckUser> findByPage(Object condition) {
        return null;
    }
}
