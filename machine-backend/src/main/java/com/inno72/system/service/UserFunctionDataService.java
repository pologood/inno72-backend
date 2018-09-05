package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72FunctionData;
import com.inno72.system.model.Inno72UserFunctionData;
import com.inno72.system.vo.FunctionTreeResultVo;
import com.inno72.system.vo.UserAreaDataVo;

/**
 * Created by CodeGenerator on 2018/09/03.
 */
public interface UserFunctionDataService extends Service<Inno72UserFunctionData> {

	Result<FunctionTreeResultVo> findAllTree(String userId);

	Result<String> updateFunctionData(UserAreaDataVo userData);

	List<Inno72FunctionData> list(String userId);

}
