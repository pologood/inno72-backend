package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Dept;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
public interface DeptService extends Service<Inno72Dept> {

	Result<String> deleteAll();

}
