package com.inno72.check.service;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.common.Result;
import com.inno72.common.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

public interface CheckFaultService extends Service<Inno72CheckFault> {
    Result<String> addCheckFault(Inno72CheckFault checkFault);

    Result<String> finish(Inno72CheckFault checkFault);

    List<Inno72CheckFault> findForPage(Integer status);

    Result<String> upload(MultipartFile file);

    Result<String> editRemark(String faultId, String remark);

    Result<Inno72CheckFault> getDetail(String faultId);

    Result<List<Inno72CheckFaultType>> getTypeList(String parentCode);
}
