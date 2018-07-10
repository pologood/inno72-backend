package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Merchant;
import com.inno72.project.vo.Inno72MerchantVo;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MerchantService extends Service<Inno72Merchant> {

	Result<String> saveModel(Inno72Merchant model);
	
	Result<String> delById(String id);

	Result<String> updateModel(Inno72Merchant model);
	
	List<Inno72Merchant> getList(Inno72Merchant model);

	List<Inno72MerchantVo> findByPage(String code, String keyword);

	

}
