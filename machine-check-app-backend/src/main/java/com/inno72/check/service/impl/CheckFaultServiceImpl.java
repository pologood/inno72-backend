package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckFaultImageMapper;
import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultImage;
import com.inno72.check.service.CheckFaultService;
import com.inno72.common.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("checkFaultService")
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {

    @Resource
    private Inno72CheckFaultMapper inno72CheckFaultMapper;

    @Resource
    private Inno72CheckFaultImageMapper inno72CheckFaultImageMapper;

    @Override
    public Result addCheckFault(Inno72CheckFault checkFault) {
        checkFault.setId(StringUtil.getUUID());
        checkFault.setSubmitTime(LocalDateTime.now());
        inno72CheckFaultMapper.insertSelective(checkFault);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result finish(Inno72CheckFault checkFault) {
        Map<String,Object> map = new HashMap<>();
        Inno72CheckFault upCheckFault = new Inno72CheckFault();
        upCheckFault.setId(checkFault.getId());
        upCheckFault.setFinishRemark(checkFault.getFinishRemark());
        upCheckFault.setFinishTime(LocalDateTime.now());
        upCheckFault.setFinishUser(checkFault.getFinishUser());
        inno72CheckFaultMapper.updateByPrimaryKeySelective(upCheckFault);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public List<Inno72CheckFault> findForPage(Condition condition) {
        condition.orderBy("submitTime").desc();
        List<Inno72CheckFault> list = mapper.selectByConditionByPage(condition);
        if(list != null && list.size()>0){
            for(Inno72CheckFault checkFault:list){
                String id = checkFault.getId();
                List<Inno72CheckFaultImage> images = inno72CheckFaultImageMapper.selectByFaultId(id);
                checkFault.setImageList(images);
            }
        }
        return list;
    }

    @Override
    public Result<String> upload(MultipartFile file) {
        return UploadUtil.uploadImage(file,"check");
    }


}
