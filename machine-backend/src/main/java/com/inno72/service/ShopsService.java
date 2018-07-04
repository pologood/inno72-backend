package com.inno72.service;
import com.inno72.model.Inno72Shops;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface ShopsService extends Service<Inno72Shops> {

	List<Inno72Shops> getList(Inno72Shops model);

	List<Inno72Shops> findByPage(Inno72Shops model);

}
