package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.vo.MachineAppVersionVo;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
public interface AppVersionService extends Service<Inno72App> {

	List<MachineAppVersionVo> appVersion(String appPackage, Integer versionCode, String keyword);

}
