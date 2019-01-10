package com.inno72.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Goods;
import com.inno72.project.vo.Inno72GoodsVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface GoodsService extends Service<Inno72Goods> {

	List<Inno72Goods> getList(Inno72Goods model);

	List<Inno72Goods> findByPage(String code, String keyword);

	Result<String> delById(String id);

	Inno72GoodsVo findId(String id);

	Result<String> uploadImage(MultipartFile file);

	Result<String> saveModel(Inno72Goods model);

	Result<String> updateModel(Inno72Goods model);

	Result<String> isExist(String code, String sellerId, String Id, int type);

}
