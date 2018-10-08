package com.inno72.app.service;

import com.inno72.app.model.Inno72Machine;
import com.inno72.common.Service;

public interface MachineService extends Service<Inno72Machine> {
	public Inno72Machine getMachineByCode(String machineCode);
}
