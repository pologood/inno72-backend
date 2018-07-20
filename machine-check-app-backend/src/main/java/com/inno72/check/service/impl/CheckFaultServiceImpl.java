package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckFaultImageMapper;
import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.mapper.Inno72CheckFaultRemarkMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultImage;
import com.inno72.check.model.Inno72CheckFaultRemark;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @Resource
    private Inno72CheckFaultRemarkMapper inno72CheckFaultRemarkMapper;

    @Autowired
    private MsgUtil msgUtil;

    @Override
    public Result addCheckFault(Inno72CheckFault checkFault) {
        String[] machineIds = checkFault.getMachineIds();
        StringBuffer mIds = new StringBuffer();
        if(machineIds != null && machineIds.length>0){
            for(int index=0;index<machineIds.length;index++){
                mIds.append(machineIds[index]);
                mIds.append(",");
                String id = StringUtil.getUUID();
                checkFault.setId(id);
                String time = DateUtil.toTimeStr(LocalDateTime.now(),DateUtil.DF_FULL_S2);
                checkFault.setCode("F"+StringUtil.createRandomCode(6)+time);
                checkFault.setSubmitTime(LocalDateTime.now());
                checkFault.setMachineId(machineIds[index]);
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

            }
            List<Inno72Machine>machines = inno72MachineMapper.selectByIds(mIds.substring(0,mIds.length()-1));
            if(machines != null && machines.size()>0){
                for(Inno72Machine machine:machines){
                    String pushCode = "push_check_app_code";
                    Map<String,String> params = new HashMap<>();
                    params.put("machineCode",machine.getMachineCode());
                    params.put("machineId",machine.getId());
                    List<Inno72CheckUser> checkUserList = inno72CheckUserMapper.selectByMachineId(machine.getId());
                    if(checkUserList != null && checkUserList.size()>0){
                        for(Inno72CheckUser checkUser:checkUserList){
                            String phone = checkUser.getPhone();
                            if(StringUtil.isNotEmpty(phone)){
                                msgUtil.sendPush(pushCode,params,phone,"互动管家","故障","您有一条故障信息");
                                msgUtil.sendSMS(pushCode,params,phone,"互动管家");
                            }
                        }
                    }
                }

            }

        }

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
    public List<Inno72CheckFault> findForPage(Integer status) {
        Inno72CheckUser checkUser = getUser();
        Map<String,Object> map = new HashMap<>();
        map.put("checkUserId",checkUser.getId());
        if(status != null){
            map.put("status",status);
        }
        List<Inno72CheckFault> list = inno72CheckFaultMapper.selectForPage(map);
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

    @Override
    public Result<String> editRemark(String faultId, String remark) {
        Inno72CheckFaultRemark faultRemark = new Inno72CheckFaultRemark();
        faultRemark.setId(StringUtil.getUUID());
        faultRemark.setFaultId(faultId);
        faultRemark.setUserId(getUser().getId());
        faultRemark.setType(1);
        faultRemark.setRemark(remark);
        faultRemark.setCreateTime(LocalDateTime.now());
        inno72CheckFaultRemarkMapper.insertSelective(faultRemark);
        return ResultGenerator.genSuccessResult();
    }

    public Inno72CheckUser getUser(){
        SessionData session = CommonConstants.SESSION_DATA;
        Inno72CheckUser checkUser = Optional.ofNullable(session).map(SessionData::getUser).orElse(null);
        return checkUser;
    }

}
