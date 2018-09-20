package com.inno72.Interact.service;

import java.util.List;

import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Merchant;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractMerchantService extends Service<Inno72InteractMerchant> {

	Result<String> save(InteractMerchantVo model);

	Result<String> update(InteractMerchantVo model);

	Inno72Merchant findMerchantsById(String id);

	List<InteractMerchantVo> getList(String interactId);

}
