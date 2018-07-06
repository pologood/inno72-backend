package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.system.model.Inno72Function;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
public interface FunctionService extends Service<Inno72Function> {

	List<Inno72Function> findFunctionsByUserId(String id);

}
