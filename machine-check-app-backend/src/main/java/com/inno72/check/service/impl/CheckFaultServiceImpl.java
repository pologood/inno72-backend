package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckFaultImageMapper;
import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultImage;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckFaultService;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.msg.MsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("checkFaultService")
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {

    @Resource
    private Inno72CheckFaultMapper inno72CheckFaultMapper;

    @Resource
    private Inno72CheckFaultImageMapper inno72CheckFaultImageMapper;

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;

    @Autowired
    private MsgUtil msgUtil;

    @Override
    public Result addCheckFault(Inno72CheckFault checkFault) {
        String id = StringUtil.getUUID();
        checkFault.setId(id);
        checkFault.setSubmitTime(LocalDateTime.now());
        inno72CheckFaultMapper.insertSelective(checkFault);
        String [] images = checkFault.getImages();
        if(images != null && images.length>0){
            List<Inno72CheckFaultImage> imageList = new ArrayList<>();
            for(int i=0;i<images.length;i++){
                Inno72CheckFaultImage image = new Inno72CheckFaultImage();
                image.setId(StringUtil.getUUID());
                image.setFaultId(id);
                image.setImage(images[i]);
                image.setCreateTime(LocalDateTime.now());
                imageList.add(image);
            }
            inno72CheckFaultImageMapper.insertList(imageList);

        }
        Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(checkFault.getMachineId());
        String pushCode = "push_check_app_code";
        Map<String,String> params = new HashMap<>();
        params.put("machineCode",machine.getMachineCode());
        params.put("machineId",machine.getId());
        List<Inno72CheckUser> checkUserList = inno72CheckUserMapper.selectByMachineId(machine.getId());
        if(checkUserList != null && checkUserList.size()>0){
        }
        String alias = "54321";
        msgUtil.sendPush("push_monitor_code", params, "54321", "项目名-类名", "title", "content");
        msgUtil.sendPush(pushCode,params,alias,"互动管家","故障","您有一条故障信息");
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
