package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;

import java.util.List;

/**
 * @author
 */
@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckUserMapper extends Mapper<Inno72CheckUser> {

    /**
     * find phone
     *
     * @param machineCode
     * @return
     */
    List<Inno72CheckUserPhone> selectPhoneByMachineCode(String machineCode);


}