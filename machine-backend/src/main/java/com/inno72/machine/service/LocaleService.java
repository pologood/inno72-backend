package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Tag;
import com.inno72.machine.vo.Inno72LocaleVo;
import com.inno72.machine.vo.MachineLocaleInfo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface LocaleService extends Service<Inno72Locale> {

	@Override
	Inno72LocaleVo findById(String id);

	List<Inno72LocaleVo> getList(String code, String keyword, String tag);

	List<Inno72LocaleVo> findByPage(String code, String keyword, String type);

	Result<String> delById(String id);

	List<MachineLocaleInfo> selectLocaleByMachineCode(List<MachineLocaleInfo> list);

	Result<String> saveModel(Inno72Locale model);

	Result<String> updateModel(Inno72Locale model);

	List<Inno72Tag> findTagsByPage(String keyword);

	List<Inno72Tag> getTaglist(String keyword);

}
