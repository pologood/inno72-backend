package com.inno72.mapper;


import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Locale;
import com.inno72.model.MachineLocaleInfo;

/**
 * @author
 */
@org.apache.ibatis.annotations.Mapper
public interface Inno72LocaleMapper extends Mapper<Inno72Locale> {

    /**
     * find locale by machineCode
     * @param :machineCodes
     * @return :List
     */
    List<MachineLocaleInfo> selectLocaleByMachineCode(List<String> machineCodes);


}