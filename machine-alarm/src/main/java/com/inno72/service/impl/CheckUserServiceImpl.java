package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72CheckUserMapper;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;
import com.inno72.service.CheckUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
@Service
@Transactional
public class CheckUserServiceImpl extends AbstractService<Inno72CheckUser> implements CheckUserService {
    private static Logger logger = LoggerFactory.getLogger(CheckUserServiceImpl.class);

    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;

    @Override
    public List<Inno72CheckUserPhone> selectPhoneByMachineCode(Inno72CheckUserPhone inno72CheckUserPhone) {

        String machineCode = inno72CheckUserPhone.getMachineCode();
        List<Inno72CheckUserPhone> inno72CheckUserPhones = inno72CheckUserMapper.selectPhoneByMachineCode(machineCode);
        return inno72CheckUserPhones;
    }

    @Override
    public List<Inno72CheckUser> findByPage(Object condition) {
        return null;
    }
}
