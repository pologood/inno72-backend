package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Game;
import com.inno72.project.vo.Inno72GameVo;

/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface GameService extends Service<Inno72Game> {

	List<Inno72GameVo> findByPage(String code, String keyword);

	Result<String> delById(String id);

	List<Inno72Game> getList(Inno72Game model);

	Result<String> saveModel(Inno72Game model);

	Result<String> updateModel(Inno72Game model);

}
