package com.inno72.check.service;
import java.util.List;

import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/07/20.
 */
public interface CheckFaultTypeService extends Service<Inno72CheckFaultType> {

	List<Inno72CheckFaultType> findByPage(String s);

}
