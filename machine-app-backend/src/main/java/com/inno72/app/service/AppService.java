package com.inno72.app.service;

import java.util.List;

import com.inno72.app.model.Inno72App;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/07/13.
 */
public interface AppService extends Service<Inno72App> {

	Result<List<Inno72App>> getAppByBlong(String belong);

}
