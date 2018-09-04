package com.inno72.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;


/**
 * Created by CodeGenerator on 2018/07/18.
 * @author
 */
public interface CheckUserService extends Service<Inno72CheckUser> {

    /**
     * find phone
     *
     * @param inno72CheckUserPhone
     * @return
     */
    List<Inno72CheckUserPhone> selectPhoneByMachineCode(Inno72CheckUserPhone inno72CheckUserPhone);

}
