package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72CheckFault;
import com.inno72.model.Inno72CheckFaultRemark;

public interface CheckFaultService extends Service<Inno72CheckFault> {

	public Integer saveCheckFault(Inno72CheckFault checkFault);

	public Integer saveCheckFaultRemark(Inno72CheckFaultRemark checkFaultRemark);
}
