package com.inno72.project.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.model.Inno72Activity;
import com.inno72.project.vo.Inno72ActivityVo;


/**
 * Created by CodeGenerator on 2018/06/29.
 */
public interface ActivityService extends Service<Inno72Activity> {


	Result<String> saveModel(Inno72Activity model);
	
	Result<String> delById(String id);

	Result<String> updateModel(Inno72Activity model);
	
	List<Inno72Activity> getList(Inno72Activity model);

	List<Inno72ActivityVo> findByPage(String code, String keyword);

	Inno72ActivityVo selectById(String id);

	

}
