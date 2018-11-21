package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72MerchantUser;
import com.inno72.project.vo.Inno72MerchantUserVo;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
public interface Inno72MerchantUserService extends Service<Inno72MerchantUser> {
	public Result<String> saveOrUpdate(Inno72MerchantUser user);

	Result getCode();

	Result alterStatus(String id, String status);

	List<Inno72MerchantUserVo> findByPage(String keyword);

	Result resetPwd(Inno72MerchantUser user);

	Result<List<Inno72MerchantUser>> getList(Inno72MerchantUser user);
}
