package com.inno72.machine.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.vo.CommonVo;
import com.inno72.machine.vo.CutAppVo;
import com.inno72.machine.vo.SupplyRequestVo;

public interface MachineService extends Service<Inno72Machine> {

	Result<String> setMachine(SupplyRequestVo vo);

	List<Inno72Machine> getMachineList(CommonVo commonVo);

	Result<List<Inno72AdminArea>> findFirstLevelArea();

	Result<Inno72AdminArea> cityLevelArea(Inno72AdminArea adminArea);

	Result<List<Inno72Locale>> selectLocaleByAreaCode(String areaCode);

	Result<Map<String, Object>> selectMachineLocale(Inno72Machine inno72Machine);

	Result<Inno72Machine> getMachine(Inno72Machine inno72Machine);

	Result<List<Inno72Machine>> getSupplyMachineList(Inno72Machine inno72Machine);

	Result<String> cutApp(CutAppVo vo);
}
