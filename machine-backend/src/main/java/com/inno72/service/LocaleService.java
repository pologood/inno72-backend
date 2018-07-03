package com.inno72.service;
import com.inno72.model.Inno72Locale;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface LocaleService extends Service<Inno72Locale> {

	List<Inno72Locale> findByPage(Inno72Locale locale);

	List<Inno72Locale> getList(Inno72Locale locale);

}
