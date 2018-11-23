package com.inno72.Interact.service;

import java.util.List;
import java.util.Map;

import com.inno72.Interact.model.Inno72InteractShops;
import com.inno72.Interact.vo.InteractShopsVo;
import com.inno72.Interact.vo.ShopsVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractShopsService extends Service<Inno72InteractShops> {

	Result<String> save(InteractShopsVo model);

	ShopsVo findShopsById(String id);

	List<ShopsVo> getList(String interactId, String merchantId);

	Result<String> deleteById(String interactId, String shopsId);

	List<Map<String, Object>> checkShops(String sellerId);

	Result<String> update(String interactId, String shopsId, Integer isVip);

}
