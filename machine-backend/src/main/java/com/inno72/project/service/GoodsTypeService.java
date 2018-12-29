package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72GoodsType;

/**
 * Created by CodeGenerator on 2018/12/29.
 */
public interface GoodsTypeService extends Service<Inno72GoodsType> {

	Result<String> saveModel(Inno72GoodsType model);

	Result<String> updateModel(Inno72GoodsType model);

	List<Inno72GoodsType> findByPage(String code, String keyword);

}
