package com.inno72.service.impl;

import java.time.LocalDateTime;
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
import com.inno72.mapper.Inno72SmsMapper;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.model.Inno72Sms;
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

    @Resource
    private Inno72SmsMapper inno72SmsMapper;

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
		Map<String, String> params = new HashMap<>();
    	for(Inno72CheckUser checkUser:checkUserList){
			String phone = checkUser.getPhone();
			int unReadCount = checkUser.getUnReadCount();
			if(StringUtil.isNotEmpty(phone) && unReadCount>0){
				String text = "【互动管家】尊敬的用户，您的互动管家中有"+unReadCount+"条未处理的消息 请尽快处理！";
				params.put("msg",text);
				msgUtil.sendSMS("check_msg_count_sms",params,phone,"machineAlarm-RedisReceiver");
				Inno72Sms inno72Sms = new Inno72Sms();
				inno72Sms.setId(StringUtil.getUUID());
				inno72Sms.setUserType(1);
				inno72Sms.setPhone(phone);
				inno72Sms.setText(text);
				inno72Sms.setCreateTime(LocalDateTime.now());
				inno72SmsMapper.insertSelective(inno72Sms);
			}
		}
	}

	@Override
    public List<Inno72CheckUser> findByPage(Object condition) {
        return null;
    }
}
