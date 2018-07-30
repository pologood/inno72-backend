package com.inno72.service;


import com.inno72.common.Service;
import com.inno72.model.Inno72Locale;
import com.inno72.model.MachineLocaleInfo;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/06/29.
 * @author
 */
public interface LocaleService extends Service<Inno72Locale> {

    /**
     * find locale
     *
     * @param list
     * @return
     */
    List<MachineLocaleInfo> selectLocaleByMachineCode(List<MachineLocaleInfo> list);

}
