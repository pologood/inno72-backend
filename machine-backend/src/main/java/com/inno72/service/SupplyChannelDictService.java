package com.inno72.service;
import com.inno72.model.Inno72SupplyChannelDict;
import com.inno72.common.Service;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface SupplyChannelDictService extends Service<Inno72SupplyChannelDict> {

    List<Inno72SupplyChannelDict> getAll();
}
