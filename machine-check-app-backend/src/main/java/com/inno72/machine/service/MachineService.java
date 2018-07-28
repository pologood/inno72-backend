package com.inno72.machine.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;

import java.util.List;

public interface MachineService extends Service<Inno72Machine> {

    Result<String> setMachine(String machineId, String localeId);

    Result<List<Inno72Machine>> getMachineList();

    Result<List<Inno72AdminArea>> findFirstLevelArea();

    Result<Inno72AdminArea> findByFirstLevelCode(String code);

    Result<List<Inno72Locale>> selectLocaleByAreaCode(String areaCode);
}
