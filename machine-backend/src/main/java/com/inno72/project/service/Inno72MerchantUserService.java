package com.inno72.project.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72MerchantUser;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
public interface Inno72MerchantUserService extends Service<Inno72MerchantUser> {
	public Result<String> saveOrUpdate(Inno72MerchantUser user);

	Result getCode();

	Result alterStatus(String id, String status);
}
