package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72CheckUser;
import com.inno72.model.Inno72CheckUserPhone;

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


	List<Inno72CheckUser> selectUnReadByParam(Map<String,Object> map);
}