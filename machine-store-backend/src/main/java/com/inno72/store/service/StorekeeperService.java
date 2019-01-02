package com.inno72.store.service;
import com.inno72.common.Result;
import com.inno72.common.SessionData;
import com.inno72.store.model.Inno72Storekeeper;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StorekeeperService extends Service<Inno72Storekeeper> {

	Result<String> saveKeeper(Inno72Storekeeper storekeeper);

	Result<String> updateKeepper(Inno72Storekeeper storekeeper);

	Result<String> editUse(Inno72Storekeeper storekeeper);

	Result<SessionData> login(Inno72Storekeeper inno72Storekeeper);

	Result<String> getSmsCode(String mobile);

	Result<String> logout();

	Result<String> setPwd(String newPwd, String rePwd);

	Result<String> reSetPwd(String oldPwd, String newPwd, String rePwd);
}
