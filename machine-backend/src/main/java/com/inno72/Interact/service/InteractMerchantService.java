package com.inno72.Interact.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.inno72.Interact.model.Inno72InteractMerchant;
import com.inno72.Interact.vo.InteractMerchantVo;
import com.inno72.Interact.vo.Merchant;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Merchant;

/**
 * Created by CodeGenerator on 2018/09/19.
 */
public interface InteractMerchantService extends Service<Inno72InteractMerchant> {

	Result<Object> save(InteractMerchantVo model);

	Inno72Merchant findMerchantsById(String id);

	List<Merchant> getList(String interactId);

	Result<String> deleteById(String interactId, String merchantId);

	void findMachineSellerId(String activityId, String activityType, HttpServletResponse response);

	List<Map<String, Object>> getMerchantUserList(String keyword);

	List<Map<String, Object>> checkMerchant(String merchantAccountId, String channel);

}
