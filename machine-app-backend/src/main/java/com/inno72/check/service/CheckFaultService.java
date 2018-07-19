package com.inno72.check.service;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.common.Result;
import com.inno72.common.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

public interface CheckFaultService extends Service<Inno72CheckFault> {
    Result addCheckFault(Inno72CheckFault checkFault);

    Result finish(Inno72CheckFault checkFault);

    List<Inno72CheckFault> findForPage(Condition condition);

    Result<String> upload(MultipartFile file);
}
