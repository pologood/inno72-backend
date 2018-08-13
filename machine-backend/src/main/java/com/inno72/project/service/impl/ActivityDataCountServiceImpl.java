package com.inno72.project.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.StringUtil;
import com.inno72.project.mapper.Inno72ActivityDataCountMapper;
import com.inno72.project.model.Inno72ActivityDataCount;
import com.inno72.project.service.ActivityDataCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ActivityDataCountServiceImpl extends AbstractService<Inno72ActivityDataCount> implements ActivityDataCountService {

    @Resource
    private Inno72ActivityDataCountMapper inno72ActivityDataCountMapper;

    @Override
    public void addData() {
        List<Inno72ActivityDataCount> list = inno72ActivityDataCountMapper.selectList();
        if(list != null && list.size()>0){
            for (Inno72ActivityDataCount dataCount:list) {
                dataCount.setId(StringUtil.getUUID());
                inno72ActivityDataCountMapper.insertSelective(dataCount);
            }
        }
    }

    @Override
    public Result<List<Inno72ActivityDataCount>> findList(Inno72ActivityDataCount inno72ActivityDataCount) {
        Map<String,Object> map = new HashMap<>();
        String activityId = inno72ActivityDataCount.getActivityId();
        if(StringUtil.isNotEmpty(activityId)){
            map.put("activityId",activityId);
        }
        String activityPlanId = inno72ActivityDataCount.getActivityPlanId();
        if(StringUtil.isNotEmpty(activityPlanId)){
            map.put("activityPlanId",activityPlanId);
        }
        List<Inno72ActivityDataCount> list = inno72ActivityDataCountMapper.selectByParam(map);
        return ResultGenerator.genSuccessResult(list);
    }
}
