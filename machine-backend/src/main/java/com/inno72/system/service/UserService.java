package com.inno72.system.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72User;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
public interface UserService extends Service<Inno72User> {

	Result<Inno72User> getUserByUserId(String userId);

}
