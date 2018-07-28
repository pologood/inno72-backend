package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
public interface CheckUserService extends Service<Inno72CheckUser> {

    List<Inno72CheckUserPhone> selectPhoneByMachineCode(Inno72CheckUserPhone inno72CheckUserPhone);

}
