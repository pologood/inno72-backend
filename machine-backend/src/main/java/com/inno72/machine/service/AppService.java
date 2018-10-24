package com.inno72.machine.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.machine.model.Inno72App;
import com.inno72.machine.vo.AppVersionHistory;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
public interface AppService extends Service<Inno72App> {

	Result<List<AppVersionHistory>> findAppVersionList(String appPackageName, String keyword);

	Result<String> saveHistory(AppVersionHistory history);

}
