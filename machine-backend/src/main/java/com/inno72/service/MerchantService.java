package com.inno72.service;
import com.inno72.model.Inno72Merchant;
import com.inno72.vo.Inno72MerchantVo;

import java.util.List;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface MerchantService extends Service<Inno72Merchant> {

	List<Inno72Merchant> getList(Inno72Merchant model);


	List<Inno72MerchantVo> findByPage(Inno72MerchantVo params);

}
