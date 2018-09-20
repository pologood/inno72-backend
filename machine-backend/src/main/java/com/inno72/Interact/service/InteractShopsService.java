package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.vo.InteractShopsVo;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Shops;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractShopsService extends Service<Inno72InteractShops> {

	Result<String> save(InteractShopsVo model);

	Result<String> update(InteractShopsVo model);

	InteractShopsVo findShopsById(String id);

	List<Inno72Shops> getList(String merchantId);

}
