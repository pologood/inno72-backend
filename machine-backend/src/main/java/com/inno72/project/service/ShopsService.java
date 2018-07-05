package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Service;
import com.inno72.project.model.Inno72Shops;


/**
 * Created by CodeGenerator on 2018/07/04.
 */
public interface ShopsService extends Service<Inno72Shops> {

	List<Inno72Shops> getList(Inno72Shops model);

	List<Inno72Shops> findByPage(Inno72Shops model);

}
