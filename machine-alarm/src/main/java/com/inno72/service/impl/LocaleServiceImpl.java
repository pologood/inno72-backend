package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72LocaleMapper;
import com.inno72.model.Inno72Locale;
import com.inno72.model.MachineLocaleInfo;
import com.inno72.service.LocaleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 * @author
 */
@Service
@Transactional
public class LocaleServiceImpl extends AbstractService<Inno72Locale> implements LocaleService {

    @Resource
    private Inno72LocaleMapper inno72LocaleMapper;


    @Override
    public List<MachineLocaleInfo> selectLocaleByMachineCode(List<MachineLocaleInfo> list) {

        List<String> machineCodes = new ArrayList<>();
        for (MachineLocaleInfo machineLocaleInfo : list) {
            String machineCode = machineLocaleInfo.getMachineCode();
            machineCodes.add(machineCode);
        }
        List<MachineLocaleInfo> machineLocaleInfos = inno72LocaleMapper.selectLocaleByMachineCode(machineCodes);
        return machineLocaleInfos;
    }


    @Override
    public List<Inno72Locale> findByPage(Object condition) {
        return null;
    }
}
