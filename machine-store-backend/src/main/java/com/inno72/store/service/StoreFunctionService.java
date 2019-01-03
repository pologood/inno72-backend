package com.inno72.store.service;
import java.util.List;

import com.inno72.store.model.Inno72StoreFunction;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/12/28.
 */
public interface StoreFunctionService extends Service<Inno72StoreFunction> {

	List<Inno72StoreFunction> findAllFunction();
}
