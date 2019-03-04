package com.inno72.check.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.vo.Inno72CheckFaultVo;
import com.inno72.common.Result;
import com.inno72.common.Service;

/**
 * Created by CodeGenerator on 2018/07/19.
 */
public interface CheckFaultService extends Service<Inno72CheckFault> {

	List<Inno72CheckFault> findByPage(String keyword, String status, String workType, String source, String type,
			String startTime, String endTime);

	Result<String> faultAnswer(String id, String remark, String userId);

	Inno72CheckFaultVo selectFaultDetail(String id);

	Result<String> saveModel(Inno72CheckFault model);

	Result<String> updateStatus(String id, int status);

	List<Inno72CheckUser> selectMachineUserList(String keyword, String machineId);

	Result<String> listExcel(HttpServletResponse response, String keyword, String status, String workType,
			String source, String type, String startTime, String endTime);

}
