package com.inno72.check.service;
import java.util.List;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.vo.Inno72CheckUserVo;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.project.vo.Inno72AdminAreaVo;


/**
 * Created by CodeGenerator on 2018/07/18.
 */
public interface CheckUserService extends Service<Inno72CheckUser> {

	Result<String> saveModel(Inno72CheckUserVo model);

	Result<String> delById(String id);

	Result<String> updateModel(Inno72CheckUserVo model);

	List<Inno72CheckUser> findByPage(String keyword);

	List<Inno72AdminAreaVo> selectAreaMachineList(String code, String level);

}
