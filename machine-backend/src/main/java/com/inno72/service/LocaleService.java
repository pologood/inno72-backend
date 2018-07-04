package com.inno72.service;
import com.inno72.model.Inno72Locale;
import com.inno72.vo.Inno72LocaleVo;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface LocaleService extends Service<Inno72Locale> {


	List<Inno72Locale> getList(Inno72Locale locale);

	List<Inno72LocaleVo> findByPage(String code, String keyword);

}
