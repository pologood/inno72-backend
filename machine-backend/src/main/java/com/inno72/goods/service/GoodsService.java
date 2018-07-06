package com.inno72.goods.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.goods.model.Inno72Goods;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface GoodsService extends Service<Inno72Goods> {

	Result<String> save(Inno72Goods goods, MultipartFile file);
	
	Result<String> update(Inno72Goods model, MultipartFile file);
		
	List<Inno72Goods> getList(Inno72Goods model);

	List<Inno72Goods> findByPage(String code, String keyword);

	Result<String> delById(String id);

}
