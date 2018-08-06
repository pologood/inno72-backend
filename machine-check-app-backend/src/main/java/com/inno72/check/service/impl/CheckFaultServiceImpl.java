package com.inno72.check.service.impl;

import com.inno72.alarmMsg.mapper.Inno72AlarmMsgMapper;
import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.check.mapper.*;
import com.inno72.check.model.*;
import com.inno72.check.service.CheckFaultService;
import com.inno72.check.vo.CheckUserVo;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.msg.MsgUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
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
    private Inno72CheckFaultRemarkMapper inno72CheckFaultRemarkMapper;

    @Resource
    private Inno72CheckFaultTypeMapper inno72CheckFaultTypeMapper;

    @Resource
    private Inno72AlarmMsgMapper inno72AlarmMsgMapper;

    @Resource
    private MsgUtil msgUtil;

    @Override
    public Result<String> addCheckFault(Inno72CheckFault checkFault) {
        String[] machineIds = checkFault.getMachineIds();
        if(machineIds != null && machineIds.length>0){
            for (String machineId : machineIds) {
                String id = StringUtil.getUUID();
                checkFault.setId(id);
                String time = DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S2);
                checkFault.setCode("F" + StringUtil.createRandomCode(6) + time);
                checkFault.setSubmitTime(LocalDateTime.now());
                checkFault.setSubmitUser(UserUtil.getUser().getName());
                checkFault.setMachineId(machineId);
                inno72CheckFaultMapper.insertSelective(checkFault);
                String[] images = checkFault.getImages();
                if (images != null && images.length > 0) {
                    for (String image1 : images) {
                        Inno72CheckFaultImage image = new Inno72CheckFaultImage();
                        image.setId(StringUtil.getUUID());
                        image.setFaultId(id);
                        image.setImage(ImageUtil.getLackImageUrl(image1));
                        image.setCreateTime(LocalDateTime.now());
                        inno72CheckFaultImageMapper.insertSelective(image);
                    }
                }

            }
            Map<String,Object> queryMap = new HashMap<>();
            queryMap.put("checkUserId",UserUtil.getUser().getId());
            queryMap.put("machineIds",machineIds);
            List<Inno72Machine>machines = inno72MachineMapper.selectByParam(queryMap);
            Inno72CheckFaultType checkFaultType = inno72CheckFaultTypeMapper.selectByPrimaryKey(checkFault.getType());
            if(checkFaultType == null){
                return Results.failure("故障类型有误");
            }
            if(machines != null && machines.size()>0){
                String title = "您负责的机器出现故障";
                String remark = checkFault.getRemark();
                String appName = "machine_check_app_backend";
                String pushCode = "push_check_app_fault";
                String smsCode = "sms_check_app_fault";
                String faultType = checkFaultType.getName();

                for(Inno72Machine machine:machines){
                    String machineCode = machine.getMachineCode();
                    String localeStr = machine.getLocaleStr();
                    Map<String,String> params = new HashMap<>();
                    String messgeInfo = "【互动管家】您负责的机器，"+localeStr+"，"+machineCode+"出现故障，故障类型："+faultType+"，故障描述："+remark+"，请及时处理。";
                    params.put("machineCode",machineCode);
                    params.put("machineId",machine.getId());
                    params.put("localeStr",localeStr);
                    params.put("faultType",faultType);
                    params.put("remark",remark);
                    params.put("messgeInfo",messgeInfo);
                    List<CheckUserVo> checkUserList = machine.getCheckUserVoList();
                    if(checkUserList != null && checkUserList.size()>0){
                        for(CheckUserVo checkUser:checkUserList){
                            String phone = checkUser.getPhone();
                            if(StringUtil.isNotEmpty(phone)){
                                msgUtil.sendPush(pushCode,params,phone,appName,title,messgeInfo);
                                msgUtil.sendSMS(smsCode,params,phone,appName);
                            }
                        }

                    }
                    Inno72AlarmMsg alarmMsg = new Inno72AlarmMsg();
                    alarmMsg.setId(StringUtil.getUUID());
                    alarmMsg.setCreateTime(LocalDateTime.now());
                    alarmMsg.setMachineCode(machineCode);
                    alarmMsg.setSystem("machineChannel");
                    alarmMsg.setTitle(title);
                    alarmMsg.setType(1);
                    alarmMsg.setDetail(messgeInfo);
                    inno72AlarmMsgMapper.insertSelective(alarmMsg);
                }

            }

        }

        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<String> finish(Inno72CheckFault checkFault) {
        Inno72CheckFault upCheckFault = new Inno72CheckFault();
        upCheckFault.setId(checkFault.getId());
        upCheckFault.setFinishRemark(checkFault.getFinishRemark());
        upCheckFault.setFinishTime(LocalDateTime.now());
        upCheckFault.setStatus(1);
        upCheckFault.setFinishUser(UserUtil.getUser().getName());
        inno72CheckFaultMapper.updateByPrimaryKeySelective(upCheckFault);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public List<Inno72CheckFault> findForPage(Integer status) {
        Inno72CheckUser checkUser = UserUtil.getUser();
        Map<String,Object> map = new HashMap<>();
        map.put("checkUserId",checkUser.getId());
        if(status != null && status !=-1){
            map.put("status",status);
        }
        List<Inno72CheckFault> list = inno72CheckFaultMapper.selectForPage(map);
        if(list != null && list.size()>0){
            for(Inno72CheckFault checkFault:list){
                checkFault.setTitle("您所管理的机器出现了故障");
                List<Inno72CheckFaultImage> imageList = checkFault.getImageList();
                if(imageList != null && imageList.size()>0){
                    for(Inno72CheckFaultImage image:imageList){
                        String imageUrl = image.getImage();
                        image.setImage(ImageUtil.getLongImageUrl(imageUrl));
                    }
                }
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
        faultRemark.setUserId(UserUtil.getUser().getId());
        faultRemark.setType(1);
        faultRemark.setRemark(remark);
        faultRemark.setCreateTime(LocalDateTime.now());
        inno72CheckFaultRemarkMapper.insertSelective(faultRemark);
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<Inno72CheckFault> getDetail(String faultId) {
        Inno72CheckFault fault = inno72CheckFaultMapper.selectDetail(faultId);
        List<Inno72CheckFaultImage> imageList = fault.getImageList();
        if(imageList != null && imageList.size()>0){
            for(Inno72CheckFaultImage image:imageList){
                String imageUrl = image.getImage();
                image.setImage(ImageUtil.getLongImageUrl(imageUrl));
            }
        }
        List<Inno72CheckFaultRemark> remarkList = fault.getRemarkList();
        if(remarkList != null && remarkList.size()>0){
            for(Inno72CheckFaultRemark remark:remarkList){
                int type = remark.getType();
                if(type==1){
                    remark.setName("巡检人员");
                }else if(type==2){
                    remark.setName("运营人员");
                }
            }
        }
        return ResultGenerator.genSuccessResult(fault);
    }

    @Override
    public Result<List<Inno72CheckFaultType>> getTypeList(String parentCode) {
        Condition condition = new Condition(Inno72CheckFaultType.class);
        if(StringUtil.isEmpty(parentCode)){
            condition.createCriteria().andEqualTo("level",1);
        }else{
            condition.createCriteria().andEqualTo("parentCode",parentCode);
        }
        List<Inno72CheckFaultType> list = inno72CheckFaultTypeMapper.selectByCondition(condition);
        return ResultGenerator.genSuccessResult(list);
    }


}
