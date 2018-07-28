package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckUserMapper extends Mapper<Inno72CheckUser> {

    List<Inno72CheckUserPhone> selectPhoneByMachineCode(String machineCode);


}