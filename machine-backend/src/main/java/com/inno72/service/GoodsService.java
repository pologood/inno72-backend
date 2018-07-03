package com.inno72.service;
import com.inno72.model.Inno72Goods;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface GoodsService extends Service<Inno72Goods> {

	void save(Inno72Goods goods, MultipartFile file);

	List<Inno72Goods> findByPage(Inno72Goods model);

	List<Inno72Goods> getList(Inno72Goods model);

}
