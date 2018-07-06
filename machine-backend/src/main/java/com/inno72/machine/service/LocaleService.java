package com.inno72.machine.service;


import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.vo.Inno72LocaleVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface LocaleService extends Service<Inno72Locale> {

	Inno72LocaleVo findById(String id);
	
	List<Inno72Locale> getList(Inno72Locale locale);

	List<Inno72LocaleVo> findByPage(String code, String keyword);

	Result<String> delById(String id);


}