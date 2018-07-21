package com.inno72.check.service;
import java.util.List;

import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.check.vo.Inno72CheckFaultTypeVo;
import com.inno72.common.Result;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
public interface CheckFaultTypeService extends Service<Inno72CheckFaultType> {

	List<Inno72CheckFaultType> findByPage(String s);

	Result<String> saveModel(Inno72CheckFaultTypeVo model);

	Result<String> updateModel(Inno72CheckFaultTypeVo model);

	Inno72CheckFaultTypeVo selectTypeDetail(String code);

}
