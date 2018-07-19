package com.inno72.check.service;

import java.util.List;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.vo.Inno72CheckFaultVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/07/19.
 */
public interface CheckFaultService extends Service<Inno72CheckFault> {

	List<Inno72CheckFault> findByPage(String keyword, String status, String type, String startTime, String endTime);

	Result<String> faultAnswer(String id, String remark);

	Inno72CheckFaultVo selectFaultDetail(String id);

}
