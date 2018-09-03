package com.inno72.system.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72UserFunctionData;
import com.inno72.system.vo.FunctionTreeResultVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
public interface UserFunctionDataService extends Service<Inno72UserFunctionData> {

	Result<FunctionTreeResultVo> findAllTree(String userId);

}
