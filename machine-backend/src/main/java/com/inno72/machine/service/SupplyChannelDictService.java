package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.machine.model.Inno72SupplyChannelDict;

/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface SupplyChannelDictService extends Service<Inno72SupplyChannelDict> {

	List<Inno72SupplyChannelDict> getAll();
}
